package com.chrisrm.idea

import com.chrisrm.idea.legacy.Runner

object LegacySupportUtility {

    fun invokeClassSafely(clasName: String, runSafely: () -> Void) {
        try {
            Class.forName(clasName)
            runSafely()
        } catch (ignored: ClassNotFoundException) {
        }
    }

    fun invokeClassSafely(clasName: String, runSafely: Runner) {
        try {
            Class.forName(clasName)
            runSafely.run()// :|
        } catch (ignored: Exception) {
        }
    }
}