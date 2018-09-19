package com.chrisrm.idea

import java.lang.RuntimeException

class DDLCException(override val message: String): RuntimeException(message)