package com.harper.spacewar.logging

import org.apache.logging.log4j.LogManager

interface Logger {
    fun debug(message: String?)

    fun info(message: String?)

    fun error(throwable: Throwable)

    fun fatal(throwable: Throwable)

    companion object {
        val DEFAULT_LOGGER = Log4JLogger
        inline fun <reified T> getLogger(tree: LoggerTree = LoggerTree.DEFAULT): Logger {
            val defaultTag = T::class.simpleName!!
            return when (tree) {
                LoggerTree.DEFAULT -> {
                    Log4JLogger.provide(defaultTag)
                }
            }
        }
    }
}

interface LoggerProvider {
    fun provide(tag: String): Logger
}

enum class LoggerTree {
    DEFAULT
}

object Log4JLogger : LoggerProvider {
    override fun provide(tag: String): Logger {
        val logger = LogManager.getLogger(tag)
        return object : Logger {
            override fun debug(message: String?) = logger.debug(message)

            override fun info(message: String?) = logger.info(message)

            override fun error(throwable: Throwable) = logger.error(throwable)

            override fun fatal(throwable: Throwable) = logger.fatal(throwable)
        }
    }
}