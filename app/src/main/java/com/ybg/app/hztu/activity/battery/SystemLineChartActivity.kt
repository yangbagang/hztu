package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.LineChartItem
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_system_line_chart.*
import java.util.*

class SystemLineChartActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private var battery: Battery? = null

    private var sumItem = "bi"
    private var sumPeriod = 2
    private var sumScale = 10

    private var chartItemList: MutableList<LineChartItem> = ArrayList<LineChartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_line_chart)

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
        tv_item_bi.setOnClickListener { setItemValue(0) }
        tv_item_btv.setOnClickListener { setItemValue(1) }
        //统计周期
        tv_period_2.setOnClickListener { setItemPeriod(2) }
        tv_period_3.setOnClickListener { setItemPeriod(3) }
        tv_period_4.setOnClickListener { setItemPeriod(4) }
        tv_period_5.setOnClickListener { setItemPeriod(5) }
        tv_period_6.setOnClickListener { setItemPeriod(6) }
        //统计
        getDeviceSumData()
    }

    private fun setItemValue(valueIndex: Int) {
        when (valueIndex) {
            0 -> {
                sumItem = "bi"
                sumScale = 100
                setItemChecked(tv_item_bi)
                setItemUnChecked(tv_item_btv)
            }
            1 -> {
                sumItem = "btv"
                sumScale = 10
                setItemUnChecked(tv_item_bi)
                setItemChecked(tv_item_btv)
            }
        }
        getDeviceSumData()
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
        getDeviceSumData()
    }

    private fun setItemChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_selected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.selectedColor))
    }

    private fun setItemUnChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_unselected_bg)
        item.setTextColor(ContextCompat.getColor(this, R.color.unSelectedColor))
    }

    private fun getDeviceSumData() {
        if (!userApplication.hasLogin()) return
        if (battery == null) return
        val uid = battery!!.uid
        if (uid.startsWith("WLCB")) {
            SendRequest.getBSSumList(this@SystemLineChartActivity, userApplication.token, battery!!.id,
                    sumItem, sumPeriod, jsonCallback)
        } else if (uid.startsWith("WLCD")) {
            SendRequest.getDCSumList(this@SystemLineChartActivity, userApplication.token, battery!!.id,
                    sumItem, sumPeriod, jsonCallback)
        } else if (uid.startsWith("WLCU")) {
            SendRequest.getUPSSumList(this@SystemLineChartActivity, userApplication.token, battery!!.id,
                    sumItem, sumPeriod, jsonCallback)
        }
    }

    private val jsonCallback = object : JsonCallback() {
        override fun onJsonSuccess(data: String) {
            super.onJsonSuccess(data)
            val list = GsonUtil.createGson().fromJson<List<LineChartItem>>(data, object :
                    TypeToken<List<LineChartItem>>() {}.type)
            chartItemList.clear()
            if (list.isEmpty()) {
                ToastUtil.show(userApplication, "没有相关数据")
            }
            list.forEach {
                chartItemList.add(LineChartItem(it.xValue, it.yValue))
                println("xValue=${it.xValue}, yValue=${it.yValue}")
            }

            lineChartView.data = chartItemList
        }
    }

    companion object {
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, SystemLineChartActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
