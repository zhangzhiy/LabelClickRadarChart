package com.onetarget.radarlabeltest

import android.graphics.Rect


/**
 * Created by joker on 2018/11/2.
 */
class RadarPointBean(var startX: Float, var startY: Float, var rect: Rect) {

    fun isIn(x: Float, y: Float): Boolean {
        val endX = startX + Math.abs(rect.right - rect.left).toFloat() + DEF_PADDING.toFloat()
        val endY = startY - Math.abs(rect.bottom - rect.top).toFloat() - DEF_PADDING.toFloat()
        val startX = startX - DEF_PADDING
        val startY = startY + DEF_PADDING
        return startX < x && x < endX && startY > y && y > endY
    }

    companion object {
        private val DEF_PADDING = 25//为文字增加点击区域 相当于padding
    }

}
