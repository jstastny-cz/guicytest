package org.guicytest.guice.api

interface IGuicyModule<T : Annotation, S: Any> {
    fun getAnnotationClass(): Class<T>
    fun getDataObjectClass(): Class<S>
    fun provide(annotation: T): S
    fun dispose()
}