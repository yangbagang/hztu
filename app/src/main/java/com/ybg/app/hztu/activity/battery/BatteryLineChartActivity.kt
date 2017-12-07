package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_battery_line_chart.*

class BatteryLineChartActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private var battery: Battery? = null

    private var sumItem = "bv"
    private var sumPeriod = 2

    private var xValues: MutableList<String> = ArrayList()
    private var yValues: MutableList<Float> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_line_chart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
        }


        lc_chart.setXValues(xValues)
        lc_chart.setYValues(yValues)

        initEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_detail -> {
                if (battery != null) {
                    BatteryHistoryActivity.start(this@BatteryLineChartActivity, battery!!)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initEvent() {
        //统计项
        tv_item_bv.setOnClickListener { setItemValue(0) }
        tv_item_bt.setOnClickListener { setItemValue(1) }
        tv_item_br.setOnClickListener { setItemValue(2) }
        tv_item_bi.setOnClickListener { setItemValue(3) }
        //统计周期
        tv_period_2.setOnClickListener { setItemPeriod(2) }
        tv_period_3.setOnClickListener { setItemPeriod(3) }
        tv_period_4.setOnClickListener { setItemPeriod(4) }
        tv_period_5.setOnClickListener { setItemPeriod(5) }
        tv_period_6.setOnClickListener { setItemPeriod(6) }
        //统计
        getBatterySunData()
    }

    private fun setItemValue(valueIndex: Int) {
        when(valueIndex) {
            0 -> {
                sumItem = "bv"
                setItemChecked(tv_item_bv)
                setItemUnChecked(tv_item_bt)
                setItemUnChecked(tv_item_br)
                setItemUnChecked(tv_item_bi)
            }
            1 -> {
                sumItem = "bt"
                setItemUnChecked(tv_item_bv)
                setItemChecked(tv_item_bt)
                setItemUnChecked(tv_item_br)
                setItemUnChecked(tv_item_bi)
            }
            2 -> {
                sumItem = "br"
                setItemUnChecked(tv_item_bv)
                setItemUnChecked(tv_item_bt)
                setItemChecked(tv_item_br)
                setItemUnChecked(tv_item_bi)
            }
            3 -> {
                sumItem = "bi"
                setItemUnChecked(tv_item_bv)
                setItemUnChecked(tv_item_bt)
                setItemUnChecked(tv_item_br)
                setItemChecked(tv_item_bi)
            }
        }
        getBatterySunData()
    }

    private fun setItemPeriod(periodIndex: Int) {

        when(periodIndex) {
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
        getBatterySunData()
    }

    private fun setItemChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_selected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.selectedColor))
    }

    private fun setItemUnChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_unselected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.unSelectedColor))
    }

    private fun getBatterySunData() {
        if (!userApplication.hasLogin()) return
        if (battery == null) return
        SendRequest.getBatterySumList(this@BatteryLineChartActivity, userApplication.token, battery!!.id,
                sumItem, sumPeriod, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val list = GsonUtil.createGson().fromJson<List<ChartItem>>(data, object : TypeToken<List<ChartItem>>(){}.type)
                xValues.clear()
                yValues.clear()
//                if (list.isEmpty()) {
//                    ToastUtil.show(userApplication, "没有相关数据")
//                } else {
//                    list.forEach {
//                        xValues.add(it.xValue)
//                        yValues.add(it.yValue)
//                    }
//                }
                xValues.add("1")
                xValues.add("2")
                xValues.add("3")
                xValues.add("4")
                xValues.add("5")
                xValues.add("6")
                xValues.add("7")
                xValues.add("8")
                xValues.add("9")
                xValues.add("10")

                yValues.add(10f)
                yValues.add(8f)
                yValues.add(15f)
                yValues.add(12f)
                yValues.add(20f)
                yValues.add(7f)
                yValues.add(15f)
                yValues.add(12f)
                yValues.add(20f)
                yValues.add(7f)

                lc_chart.invalidate()
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
