package org.guicytest.testng

import org.guicytest.commons.MyAnn
import org.guicytest.commons.MyClass
import org.guicytest.guice.GuicyModule
import org.testng.Assert
import org.testng.annotations.Guice
import org.testng.annotations.Test

@Guice(modules = [GuicyModule::class])
open class TestngSampleInjectionTest{ // needs to be non-final
    @MyAnn("name1")
    lateinit var o: MyClass

    @MyAnn("name2")
    lateinit var o2: MyClass

    @Test
    fun test() {
        Assert.assertEquals(o.name, "name1")
    }

    @Test
    fun test2() {
        Assert.assertEquals(o2.name, "name2")
    }

    @Test
    open fun testMethod(@MyAnn("name3") value: MyClass?) { // needs to be non-final
        Assert.assertEquals(value?.name, "name3")
    }
}