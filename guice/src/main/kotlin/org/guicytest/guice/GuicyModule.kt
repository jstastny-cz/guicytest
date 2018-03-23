package org.guicytest.guice

import com.google.inject.Binder
import com.google.inject.MembersInjector
import com.google.inject.Module
import com.google.inject.TypeLiteral
import com.google.inject.matcher.Matchers
import com.google.inject.spi.TypeEncounter
import com.google.inject.spi.TypeListener
import mu.KotlinLogging
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.guicytest.guice.annotation.GuicyModule
import org.guicytest.guice.api.IGuicyModule
import org.reflections.Reflections

private val LOG = KotlinLogging.logger {}

class GuicyModule : Module {

    var modules: List<IGuicyModule<Annotation, Any>> = mutableListOf()

    override fun configure(p0: Binder?) {
        LOG.debug{"Configuration of GuicyModule class."}
        val refl = Reflections("")
        modules += refl.getTypesAnnotatedWith(GuicyModule::class.java).map { it.newInstance() as IGuicyModule<Annotation, Any> }
        for (m in modules) {
            LOG.debug{"Configuring module $m."}
            p0?.bindListener(Matchers.any(), GuicyTypeListener(m))
            p0?.bindInterceptor(Matchers.any(), Matchers.any(), GuicyMethodInterceptor(m))
        }
    }

    fun dispose() {
        LOG.debug{"Disposing GuicyModule."}
        modules.forEach {
            it.dispose()
        }
    }

}

fun <T : Annotation, S : Any> provideValue(guicyModule: IGuicyModule<T, S>, annotation: T): S {
    return guicyModule.provide(annotation)
}

class GuicyMethodInterceptor<T : Annotation, S : Any>(private val guicyModule: IGuicyModule<T, S>) : MethodInterceptor {

    override fun invoke(p0: MethodInvocation?): Any? {
        p0?.method?.parameters?.forEachIndexed { index, value ->
            run {
                if (guicyModule.getAnnotationClass() != null) {
                    p0.arguments[index] = provideValue(guicyModule, value.getAnnotation(guicyModule.getAnnotationClass()))
                }
            }
        }
        return p0?.proceed()
    }
}

class GuicyTypeListener<T : Annotation, S : Any>(val guicyModule: IGuicyModule<T, S>) : TypeListener {

    override fun <I : Any?> hear(typeLiteral: TypeLiteral<I>?, encounter: TypeEncounter<I>?) {
        LOG.debug { "GuicyTypeListener created." }
        val bindAnn: Class<T> = guicyModule.getAnnotationClass()
        val rawTypeClass = typeLiteral?.rawType
        encounter?.register(MembersInjector<I> { instance ->
            LOG.debug { "injecting member $rawTypeClass" }
            rawTypeClass?.declaredFields?.filter { it.annotations.any({ bindAnn.isInstance(it) }) }?.forEach {
                val ann = it.annotations.find { bindAnn.isInstance(it) }
                if (ann != null) {
                    val accessibleOriginalValue = it.isAccessible
                    it.isAccessible = true
                    it.set(
                            instance,
                            provideValue(guicyModule, ann as T)
                    )
                    it.isAccessible = accessibleOriginalValue
                }
            }
        })
    }
}


