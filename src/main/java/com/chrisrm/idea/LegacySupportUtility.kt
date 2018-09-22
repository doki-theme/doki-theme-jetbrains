package com.chrisrm.idea

import com.chrisrm.idea.legacy.Runner

object LegacySupportUtility {

    fun invokeClassSafely(clasName: String, runSafely: Runner) {
        try {
            Class.forName(clasName)
            runSafely.run()// :|
        } catch (ignored: Throwable) {
        }
    }

    fun orRunLegacy(clasName: String,
                    runCurrent: Runner,
                    runLegacy: Runner
    ) {
        try {
            Class.forName(clasName)
            runCurrent.run()// :|
        } catch (ignored: Throwable) {
            runLegacy.run()
        }
    }

    fun <T> orGetLegacy(clazz: String,
                        runSafely: () -> T,
                        orElseGet: () -> T) =
            try {
                Class.forName(clazz)
                runSafely()
            } catch (ignored: Throwable) {
                orElseGet()
            }


    fun <C, T> invokeMethodSafely(clazz: Class<C>,
                                  method: String,
                                  runSafely: () -> T,
                                  orElseGet: () -> T,
                                  vararg paratemers: Class<*>) =
            try {
                clazz.getDeclaredMethod(method, *paratemers)
                runSafely()
            } catch (ignored: Throwable) {
                orElseGet()
            }

    fun <C, T> useFieldSafely(clazz: Class<C>,
                              method: String,
                              runSafely: () -> T,
                              orElseGet: () -> T) =
            try {
                clazz.getDeclaredField(method)
                runSafely()
            } catch (ignored: Throwable) {
                orElseGet()
            }


    fun <T> useClass(clazz: String,
                     runSafely: () -> T,
                     orElseGet: () -> T) =
            try {
                Class.forName(clazz)
                runSafely()
            } catch (ignored: Throwable) {
                orElseGet()
            }
}

