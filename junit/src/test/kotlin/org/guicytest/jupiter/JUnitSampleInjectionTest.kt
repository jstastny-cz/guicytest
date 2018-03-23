package org.guicytest.jupiter

import org.guicytest.commons.MyAnn
import org.guicytest.commons.MyClass
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class JUnitSampleInjectionTest {
    @Rule @JvmField var guice: GuicyRule = GuicyRule(this)

    @MyAnn("name1")
    lateinit var o: MyClass

    @MyAnn("name2")
    lateinit var o2: MyClass

    @Test
    fun test() {
        println("test1")
        Assert.assertEquals(o.name, "name1")
    }

    @Test
    fun test2() {
        println("test2")
        Assert.assertEquals(o2.name, "name2")
    }
}
