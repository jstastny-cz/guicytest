package org.guicytest.jupiter

import org.guicytest.commons.MyAnn
import org.guicytest.commons.MyClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GuicyExtension::class)
class JupiterSampleInjectionTest {

    @MyAnn("name1")
    lateinit var o: MyClass

    @MyAnn("name2")
    lateinit var o2: MyClass

    @Test
    fun test() {
        println("test1")
        Assertions.assertEquals(o.name, "name1")
    }

    @Test
    fun test2() {
        println("test2")
        Assertions.assertEquals(o2.name, "name2")
    }

    @Test
    fun testMethod(@MyAnn("name3") value: MyClass?) {
        Assertions.assertEquals(value?.name, "name3")
    }
}
