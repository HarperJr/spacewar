package com.harper.spacewar.main.scene

import com.harper.spacewar.main.Spacewar

abstract class Scene(protected val spacewar: Spacewar) {
    abstract fun prepareScene()

    abstract fun onUpdated()
}