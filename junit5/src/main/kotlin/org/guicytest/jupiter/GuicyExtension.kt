package org.guicytest.jupiter

import com.google.inject.Guice
import org.guicytest.guice.GuicyModule
import org.junit.jupiter.api.extension.*

class GuicyExtension : TestInstancePostProcessor, AfterAllCallback, ParameterResolver {

    private val guicyModule = GuicyModule();
    private val injector = Guice.createInjector(guicyModule)!!
    override fun postProcessTestInstance(p0: Any?, p1: ExtensionContext?) {
        injector.injectMembers(p0)
    }

    override fun afterAll(p0: ExtensionContext?) {
        guicyModule.dispose()
    }

    override fun resolveParameter(p0: ParameterContext?, p1: ExtensionContext?): Any? {
        val parameter = p0?.parameter
        var result: Any? = null
        parameter?.annotations?.forEach { it ->
            run {
                if (result != null) return@forEach
                result = guicyModule.modules.filter { m -> m.getAnnotationClass().equals(it.annotationClass.java) }
                        .get(0).provide(it)
            }
        }
        return result
    }

    override fun supportsParameter(p0: ParameterContext?, p1: ExtensionContext?): Boolean {
        val annotations = (p0?.parameter?.annotations ?: emptyArray()).map { a -> a.annotationClass.java }
        val filtered = guicyModule.modules.filter { m -> annotations.contains(m.getAnnotationClass()) }
        return filtered.isNotEmpty();
    }

}