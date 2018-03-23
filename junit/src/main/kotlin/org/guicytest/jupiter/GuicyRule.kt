package org.guicytest.jupiter

import com.google.inject.Guice
import org.guicytest.guice.GuicyModule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class GuicyRule(var target: Any) : TestRule {
    override fun apply(p0: Statement?, p1: Description?): Statement? {
        return object : Statement() {
            override fun evaluate() {
                val gModule = GuicyModule();
                Guice.createInjector(gModule).injectMembers(target)
                p0?.evaluate()
                gModule.dispose()
            }

        }
    }
}