package org.guicytest.testng

import org.testng.*
import org.testng.annotations.DataProvider
import org.testng.annotations.ITestAnnotation
import org.testng.annotations.Parameters
import java.lang.reflect.Constructor
import java.lang.reflect.Method

open class GuicyListener:IAnnotationTransformer{


    override fun transform(annotation:ITestAnnotation, testClass:Class<Any>?, testConstructor:Constructor<Any>?, testMethod:Method?){
        enableParameterInjection(annotation, testClass, testConstructor, testMethod)
    }

    fun enableParameterInjection(annotation:ITestAnnotation, testClass:Class<Any>?, testConstructor: Constructor<Any>?, testMethod:Method?) {
        var dpName = annotation.dataProvider
        var dpClazz = annotation.dataProviderClass
        if (testMethod==null) return;
        var noOfParams = testMethod.parameterTypes.size
        if(testMethod.getAnnotation(Parameters::class.java)==null
            && testMethod.getAnnotation(DataProvider::class.java)==null
            && ((dpName == null || dpName.isEmpty()) && dpClazz == null) && noOfParams > 0) {
            annotation.dataProviderClass = this::class.java
            annotation.dataProvider = "DATA_PROVIDER"
        }
    }

    @DataProvider(name = "DATA_PROVIDER")
    fun dataProvider(method: Method): Array<Array<Any?>> {
        val values = Array<Array<Any?>>(1) { arrayOfNulls(method.parameterTypes.size) }
        val parameterValues = arrayOfNulls<Any?>(method.parameterTypes.size)
        values[0] = parameterValues
        return values
    }

}