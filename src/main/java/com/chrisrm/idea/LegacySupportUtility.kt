package com.chrisrm.idea

object LegacySupportUtility {

    fun invokeClassSafely(clasName: String, runSafely: ()-> Void) {
        try{
            Class.forName(clasName)
            runSafely()
        } catch (ignored: ClassNotFoundException){}
    }

    fun invokeClassSafely(clasName: String, runSafely: Runnable) {
        try{
            Class.forName(clasName)
            runSafely.run()// :|
        } catch (ignored: ClassNotFoundException){}
    }
}