package com.chrisrm.idea

import com.chrisrm.idea.legacy.Runner

object LegacySupportUtility {

    fun invokeClassSafely(clasName: String, runSafely: Runner) {
        try {
            Class.forName(clasName)
            runSafely.run()// :|
        } catch (ignored: Exception) {
        }
    }

    fun <C, T> invokeMethodSafely(clazz: Class<C>,
                                  method: String,
                                  runSafely: () -> T,
                                  orElseGet: () -> T,
                                  vararg paratemers: Class<*>) =
            try {
                clazz.getDeclaredMethod(method, *paratemers)
                runSafely()
            } catch (ignored: Exception) {
                orElseGet()
            }
}


