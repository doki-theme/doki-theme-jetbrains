package io.acari.DDLC

import java.lang.RuntimeException

class DDLCException(override val message: String): RuntimeException(message)