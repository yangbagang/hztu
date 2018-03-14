package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.sctdroid.app.uikit.CurveView
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_battery_line_chart.*
import java.util.*

class BatteryLineChartActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private var battery: Battery? = null

    private var sumItem = "bv"
    private var sumPeriod = 2
    private var sumScale = 10

    private var xValues: MutableList<String> = ArrayList()
    private var yValues: MutableList<Float> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_line_chart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
        }

        initEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initEvent() {
        //统计项
        tv_item_bv.setOnClickListener { setItemValue(0) }
        tv_item_bt.setOnClickListener { setItemValue(1) }
        tv_item_br.setOnClickListener { setItemValue(2) }
        //统计周期
        tv_period_2.setOnClickListener { setItemPeriod(2) }
        tv_period_3.setOnClickListener { setItemPeriod(3) }
        tv_period_4.setOnClickListener { setItemPeriod(4) }
        tv_period_5.setOnClickListener { setItemPeriod(5) }
        tv_period_6.setOnClickListener { setItemPeriod(6) }
        //统计
        getBatterySumData()
    }

    private fun setItemValue(valueIndex: Int) {
        when (valueIndex) {
            0 -> {
                sumItem = "bv"
                sumScale = 100
                setItemChecked(tv_item_bv)
                setItemUnChecked(tv_item_bt)
                setItemUnChecked(tv_item_br)
            }
            1 -> {
                sumItem = "bt"
                sumScale = 10
                setItemUnChecked(tv_item_bv)
                setItemChecked(tv_item_bt)
                setItemUnChecked(tv_item_br)
            }
            2 -> {
                sumItem = "br"
                sumScale = 1000
                setItemUnChecked(tv_item_bv)
                setItemUnChecked(tv_item_bt)
                setItemChecked(tv_item_br)
            }
        }
        getBatterySumData()
    }

    private fun setItemPeriod(periodIndex: Int) {

        when (periodIndex) {
            2 -> {
                sumPeriod = 2
                setItemChecked(tv_period_2)
                setItemUnChecked(tv_period_3)
                setItemUnChecked(tv_period_4)
                setItemUnChecked(tv_period_5)
                setItemUnChecked(tv_period_6)
            }
            3 -> {
                sumPeriod = 3
                setItemUnChecked(tv_period_2)
                setItemChecked(tv_period_3)
                setItemUnChecked(tv_period_4)
                setItemUnChecked(tv_period_5)
                setItemUnChecked(tv_period_6)
            }
            4 -> {
                sumPeriod = 4
                setItemUnChecked(tv_period_2)
                setItemUnChecked(tv_period_3)
                setItemChecked(tv_period_4)
                setItemUnChecked(tv_period_5)
                setItemUnChecked(tv_period_6)
            }
            5 -> {
                sumPeriod = 5
                setItemUnChecked(tv_period_2)
                setItemUnChecked(tv_period_3)
                setItemUnChecked(tv_period_4)
                setItemChecked(tv_period_5)
                setItemUnChecked(tv_period_6)
            }
            6 -> {
                sumPeriod = 6
                setItemUnChecked(tv_period_2)
                setItemUnChecked(tv_period_3)
                setItemUnChecked(tv_period_4)
                setItemUnChecked(tv_period_5)
                setItemChecked(tv_period_6)
            }
        }
        getBatterySumData()
    }

    private fun setItemChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_selected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.selectedColor))
    }

    private fun setItemUnChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_unselected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.unSelectedColor))
    }

    private fun getBatterySumData() {
        if (!userApplication.hasLogin()) return
        if (battery == null) return
        SendRequest.getBatterySumList(this@BatteryLineChartActivity, userApplication.token, battery!!.id,
                sumItem, sumPeriod, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val list = GsonUtil.createGson().fromJson<List<ChartItem>>(data, object : TypeToken<List<ChartItem>>() {}.type)
                xValues.clear()
                yValues.clear()
                if (list.isEmpty()) {
                    ToastUtil.show(userApplication, "没有相关数据")
                } else {
                    list.forEach {
                        xValues.add(it.xValue)
                        yValues.add(it.yValue)
                        println("xValue=${it.xValue}, yValue=${it.yValue}")
                    }

                    lc_chart.setAdapter(object : CurveView.Adapter() {
                        override fun getLevel(position: Int): Int = (yValues[position] * sumScale).toInt()

                        override fun getCount(): Int = yValues.size

                        override fun getXAxisText(i: Int): String = xValues[i]

                        override fun getMaxLevel(): Int {
                            var max = yValues.max()!!.toInt()
                            if (yValues.min() == yValues.max()) {
                                max = yValues.max()!!.toInt() + 1
                            }
                            return max
                        }

                        override fun getMinLevel(): Int = (yValues.min()!! * sumScale).toInt()

                        override fun onCreateMarks(position: Int): MutableSet<CurveView.Mark> {
                            val marks = HashSet<CurveView.Mark>()
                            val mark = CurveView.Mark("${yValues[position]}", CurveView.Gravity.BOTTOM or CurveView.Gravity.CENTER_HORIZONTAL, 0, 20, 0, 0);

                            marks.add(mark)
                            return marks;
                        }
                    })

                    lc_chart.invalidate()
                }
            }
        })
    }

    internal data class ChartItem(var xValue: String, var yValue: Float)

    companion object {
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, BatteryLineChartActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
