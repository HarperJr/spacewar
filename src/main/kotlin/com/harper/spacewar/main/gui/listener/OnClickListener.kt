package com.harper.spacewar.main.gui.listener

import com.harper.spacewar.main.gui.impl.element.GuiButton

interface OnClickListener {
    fun onClicked(button: GuiButton, x: Float, y: Float)
}
