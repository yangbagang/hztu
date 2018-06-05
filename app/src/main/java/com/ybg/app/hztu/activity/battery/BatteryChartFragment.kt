package com.ybg.app.hztu.activity.battery

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.LineChartItem
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.scroll.ScrollableView
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.chart.ScrollLineChartView
import java.util.*

@SuppressLint("ValidFragment")
class BatteryChartFragment(var batteryId: Long) : Fragment(), ScrollableView {

    override fun setRefreshEnable(enable: Boolean) {

    }

    override fun setLoadingEnable(enable: Boolean) {

    }

    private val userApplication = UserApplication.instance!!

    private var sumItem = "bv"
    private var sumPeriod = 2
    private var sumScale = 10

    private lateinit var itemBv: TextView
    private lateinit var itemBt: TextView
    private lateinit var itemBr: TextView
    private lateinit var period2: TextView
    private lateinit var period3: TextView
    private lateinit var period4: TextView
    private lateinit var period5: TextView
    private lateinit var period6: TextView
    private lateinit var lineChartView: ScrollLineChartView
    private lateinit var notFoundLabel: TextView

    private var chartItemList: MutableList<LineChartItem> = ArrayList<LineChartItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_battery_chart, container, false)

        initView(view)
        initEvent()

        return view
    }

    private fun initView(rootView: View) {
        itemBv = rootView.findViewById(R.id.tv_item_bv)
        itemBt = rootView.findViewById(R.id.tv_item_bt)
        itemBr = rootView.findViewById(R.id.tv_item_br)

        period2 = rootView.findViewById(R.id.tv_period_2)
        period3 = rootView.findViewById(R.id.tv_period_3)
        period4 = rootView.findViewById(R.id.tv_period_4)
        period5 = rootView.findViewById(R.id.tv_period_5)
        period6 = rootView.findViewById(R.id.tv_period_6)

        lineChartView = rootView.findViewById(R.id.lineChartView)
        notFoundLabel = rootView.findViewById(R.id.tv_no_data)
    }

    private fun initEvent() {
        //统计项
        itemBv.setOnClickListener { setItemValue(0) }
        itemBt.setOnClickListener { setItemValue(1) }
        itemBr.setOnClickListener { setItemValue(2) }
        //统计周期
        period2.setOnClickListener { setItemPeriod(2) }
        period3.setOnClickListener { setItemPeriod(3) }
        period4.setOnClickListener { setItemPeriod(4) }
        period5.setOnClickListener { setItemPeriod(5) }
        period6.setOnClickListener { setItemPeriod(6) }
        //统计
        getBatterySumData()
    }

    private fun setItemValue(valueIndex: Int) {
        when (valueIndex) {
            0 -> {
                sumItem = "bv"
                sumScale = 100
                setItemChecked(itemBv)
                setItemUnChecked(itemBt)
                setItemUnChecked(itemBr)
            }
            1 -> {
                sumItem = "bt"
                sumScale = 10
                setItemUnChecked(itemBv)
                setItemChecked(itemBt)
                setItemUnChecked(itemBr)
            }
            2 -> {
                sumItem = "br"
                sumScale = 1000
                setItemUnChecked(itemBv)
                setItemUnChecked(itemBt)
                setItemChecked(itemBr)
            }
        }
        getBatterySumData()
    }

    private fun setItemPeriod(periodIndex: Int) {

        when (periodIndex) {
            2 -> {
                sumPeriod = 2
                setItemChecked(period2)
                setItemUnChecked(period3)
                setItemUnChecked(period4)
                setItemUnChecked(period5)
                setItemUnChecked(period6)
            }
            3 -> {
                sumPeriod = 3
                setItemUnChecked(period2)
                setItemChecked(period3)
                setItemUnChecked(period4)
                setItemUnChecked(period5)
                setItemUnChecked(period6)
            }
            4 -> {
                sumPeriod = 4
                setItemUnChecked(period2)
                setItemUnChecked(period3)
                setItemChecked(period4)
                setItemUnChecked(period5)
                setItemUnChecked(period6)
            }
            5 -> {
                sumPeriod = 5
                setItemUnChecked(period2)
                setItemUnChecked(period3)
                setItemUnChecked(period4)
                setItemChecked(period5)
                setItemUnChecked(period6)
            }
            6 -> {
                sumPeriod = 6
                setItemUnChecked(period2)
                setItemUnChecked(period3)
                setItemUnChecked(period4)
                setItemUnChecked(period5)
                setItemChecked(period6)
            }
        }
        getBatterySumData()
    }

    private fun setItemChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_selected_bg)
        item.setTextColor(ContextCompat.getColor(activity!!, R.color.selectedColor))
    }

    private fun setItemUnChecked(item: TextView) {
        item.setBackgroundResource(R.drawable.square_unselected_bg)
        item.setTextColor(ContextCompat.getColor(activity!!, R.color.unSelectedColor))
    }

    private fun getBatterySumData() {
        if (!userApplication.hasLogin()) return
        if (activity == null) return
        SendRequest.getBatterySumList(activity!!, userApplication.token, batteryId,
                sumItem, sumPeriod, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val list = GsonUtil.createGson().fromJson<List<LineChartItem>>(data, object : TypeToken<List<LineChartItem>>() {}.type)
                chartItemList.clear()

                if (list.isEmpty()) {
                    notFoundLabel.visibility = View.VISIBLE
                    lineChartView.visibility = View.GONE
                } else {
                    notFoundLabel.visibility = View.GONE
                    lineChartView.visibility = View.VISIBLE
                }

                list.forEach {
                    chartItemList.add(LineChartItem(it.xValue, it.yValue))
                }

                lineChartView.data = chartItemList
            }
        })
    }

}