package com.onetarget.radarlabeltest

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet

class MainActivity : AppCompatActivity() {
    private lateinit var radarChart: LabelClickRadarChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radarChart = findViewById(R.id.radarchart)
        initRadarState()
        radarChart.setOnLabelClickListener(object : LabelClickRadarChart.OnLabelClickListener {
            override fun onLabelClick(xAxisIndex: Int) {
                Toast.makeText(this@MainActivity, axisArray[xAxisIndex], Toast.LENGTH_SHORT).show()
            }

        })
        bindCompositeRadar()
    }

    private fun initRadarState() {
        radarChart.description.isEnabled = false
        // 绘制线条宽度，圆形向外辐射的线条
        radarChart.webColor = Color.LTGRAY
        radarChart.webLineWidth = 1.5f
        // 内部线条宽度，外面的环状线条
        radarChart.webColorInner = Color.LTGRAY
        radarChart.webLineWidthInner = 1.0f
        // 所有线条WebLine透明度
        radarChart.webAlpha = 100

        val xAxis = radarChart.xAxis

        // X坐标值字体大小
        xAxis.textSize = 10f

        val yAxis = radarChart.yAxis

        yAxis.setDrawLabels(false)
        // Y坐标值标签个数
        yAxis.setLabelCount(6, false)
        // Y坐标值字体大小
        yAxis.textSize = 10f
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true)
    }

    private var axisArray = arrayOf("实践创新", "思想道德", "学业成长", "审美表现", "身心健康")

    private fun bindCompositeRadar() {

        val list = ArrayList<String>()
        val studentEntries = ArrayList<RadarEntry>()//个人
        val gradeEntries = ArrayList<RadarEntry>()//年级
        for (i in axisArray.indices) {
            studentEntries.add(RadarEntry((Math.random() * 10).toFloat(), axisArray[i]))
            gradeEntries.add(RadarEntry((Math.random() * 10).toFloat(), axisArray[i]))
            list.add(axisArray[i])
        }
        val xAxis = radarChart.xAxis
        xAxis.valueFormatter = XAxisFormatter(list)

        val studentSet = RadarDataSet(studentEntries, "个人")
        studentSet.color = Color.parseColor("#14d089")
        studentSet.fillColor = Color.parseColor("#14d089")
        studentSet.setDrawFilled(true)
        studentSet.fillAlpha = 85
        studentSet.lineWidth = 1f
        studentSet.isDrawHighlightCircleEnabled = true
        studentSet.setDrawHighlightIndicators(false)

        val gradeSet = RadarDataSet(gradeEntries, "年级平均值")
        gradeSet.color = Color.parseColor("#0072bd")
        gradeSet.fillColor = Color.parseColor("#0072bd")
        gradeSet.setDrawFilled(true)
        gradeSet.fillAlpha = 85
        gradeSet.lineWidth = 1f
        gradeSet.isDrawHighlightCircleEnabled = true
        gradeSet.setDrawHighlightIndicators(false)

        val sets = ArrayList<IRadarDataSet>()
        sets.add(studentSet)
        sets.add(gradeSet)

        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)

        radarChart.legend.isEnabled = true
        radarChart.data = data
        radarChart.invalidate()
    }
}
