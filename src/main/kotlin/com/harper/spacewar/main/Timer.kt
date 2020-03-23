package com.harper.spacewar.main

import com.harper.spacewar.logging.Logger

class Timer(private val ticksPerSecond: Float) {
    private val logger = Logger.getLogger<Timer>()

    var time: Float = 0f
        private set

    private var prevTimeMillis: Long = System.currentTimeMillis()
    private var partialTicks: Int = 0
    private var timeDelta: Float = 0f

    fun update() {
        val sysTimeMillis = getSysTimeMillis()
        this.timeDelta = (sysTimeMillis - this.prevTimeMillis).toFloat() / (1000f / ticksPerSecond)
        this.prevTimeMillis = sysTimeMillis
        this.time += this.timeDelta
        this.partialTicks = this.time.toInt()
        this.time -= this.partialTicks.toFloat()

        //logger.debug("Time elapsed ${this.time}")
    }

    private fun getSysTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
