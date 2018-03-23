package org.guicytest.commons

import org.guicytest.guice.annotation.GuicyModule
import org.guicytest.guice.api.IGuicyModule

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class MyAnn(val name: String)

class MyClass(val name: String) {

}

@GuicyModule
class MyModule : IGuicyModule<MyAnn, MyClass> {
    override fun getAnnotationClass(): Class<MyAnn> {
        return MyAnn::class.java
    }

    override fun getDataObjectClass(): Class<MyClass> {
        return MyClass::class.java
    }

    override fun provide(annotation: MyAnn): MyClass {
        return MyClass(annotation.name)
    }

    override fun dispose() {

    }
}
