package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.TabFragmentPagerAdapter
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_battery_value.*

class BatteryValueActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!
    private var device: Battery? = null
    private var batteryId = 0L
    private var num = 0

    private var historyFragment: BatteryHistoryFragment? = null
    private var chartFragment: BatteryChartFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_value)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                //展开状态，可以刷新、不可以加载更多
                historyFragment?.setRefreshEnable(true)
                historyFragment?.setLoadingEnable(false)
                chartFragment?.setRefreshEnable(true)
                chartFragment?.setLoadingEnable(false)
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠状态，可以加载更多，不可以刷新
                historyFragment?.setRefreshEnable(false)
                historyFragment?.setLoadingEnable(true)
                chartFragment?.setRefreshEnable(false)
                chartFragment?.setLoadingEnable(true)
            } else {
                //中间状态，不可以加载更多，不可以刷新
                historyFragment?.setRefreshEnable(false)
                historyFragment?.setLoadingEnable(false)
                chartFragment?.setRefreshEnable(false)
                chartFragment?.setLoadingEnable(false)
            }
        }

        if (intent != null) {
            device = intent.extras.get("device") as Battery
            batteryId = intent.extras.getLong("batteryId")
            num = intent.extras.getInt("num")
            if (device != null) {
                tv_system_name.text = "${device!!.uid}(${device!!.name})"
                tv_system_value.text = "总告警数: 0, 新告警: 0"

                getSystemAddress(device!!.lac, device!!.cid, 0)
                tv_system_time.text = DateUtil.getTimeInterval(device!!.createTime)

                historyFragment = BatteryHistoryFragment(batteryId)
                chartFragment = BatteryChartFragment(batteryId)

                supportActionBar?.title = "${num}号电池历史数据"
                tv_system_type.text = "历史数据"
            }
        }

        val list = ArrayList<Fragment>()
        if (historyFragment != null) {
            list.add(historyFragment!!)
        }
        if (chartFragment != null) {
            list.add(chartFragment!!)
        }

        val adapter = TabFragmentPagerAdapter(supportFragmentManager, list)
        vp_device.adapter = adapter
        vp_device.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    v_item_1.setBackgroundResource(R.drawable.ic_item_selected)
                    v_item_2.setBackgroundResource(R.drawable.ic_item_normal)
                    tv_system_type.text = "历史数据"
                    supportActionBar?.title = "${num}号电池历史数据"
                    ll_battery_title.visibility = View.VISIBLE
                } else {
                    v_item_2.setBackgroundResource(R.drawable.ic_item_selected)
                    v_item_1.setBackgroundResource(R.drawable.ic_item_normal)
                    tv_system_type.text = "折线图"
                    supportActionBar?.title = "${num}号电池折线图"
                    ll_battery_title.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        vp_device.clearOnPageChangeListeners()
        super.onDestroy()
    }

    private fun getSystemAddress(lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(this@BatteryValueActivity, userApplication.token, lac, cid, type, object :
                JsonCallback() {
            override fun onJsonSuccess(data: String) {
                if (data != "" && data.contains(";")) {
                    val address = data.split(";")
                    tv_system_address1.text = address[0]
                    tv_system_address2.text = address[1]
                } else {
                    tv_system_address1.text = ""
                    tv_system_address2.text = ""
                }
            }
        })
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

    companion object {
        fun start(context: Context, device: Battery, batteryId: Long, num: Int) {
            val starter = Intent(context, BatteryValueActivity::class.java)
            starter.putExtra("device", device)
            starter.putExtra("batteryId", batteryId)
            starter.putExtra("num", num)
            context.startActivity(starter)
        }
    }

}