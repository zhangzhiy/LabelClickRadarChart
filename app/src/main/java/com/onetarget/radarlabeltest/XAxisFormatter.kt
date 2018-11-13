package com.onetarget.radarlabeltest

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

/**
 * Created by joker on 2018/11/2.
 */
class XAxisFormatter(private var list: List<String>?) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        if (list == null || list!!.isEmpty()) return ""
        val position = Math.abs(value).toInt() % list!!.size
        return if (position < list!!.size) {
            if (list!!.size > 1) {
                list!![position]
            } else {
                if (value == 0f) {
                    list!![0]
                } else {
                    ""
                }
            }
        } else value.toString()
    }
}