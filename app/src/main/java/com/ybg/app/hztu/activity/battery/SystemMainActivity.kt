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
import kotlinx.android.synthetic.main.activity_system_main.*

class SystemMainActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!
    private var battery: Battery? = null

    private var detailFragment: SystemDetailFragment? = null
    private var listFragment: BatteryListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                //展开状态，可以刷新、不可以加载更多
                detailFragment?.setRefreshEnable(true)
                detailFragment?.setLoadingEnable(false)
                listFragment?.setRefreshEnable(true)
                listFragment?.setLoadingEnable(false)
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠状态，可以加载更多，不可以刷新
                detailFragment?.setRefreshEnable(false)
                detailFragment?.setLoadingEnable(true)
                listFragment?.setRefreshEnable(false)
                listFragment?.setLoadingEnable(true)
            } else {
                //中间状态，不可以加载更多，不可以刷新
                detailFragment?.setRefreshEnable(false)
                detailFragment?.setLoadingEnable(false)
                listFragment?.setRefreshEnable(false)
                listFragment?.setLoadingEnable(false)
            }
        }

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
            if (battery != null) {
                tv_system_name.text = "${battery!!.uid}(${battery!!.name})"
                tv_system_value.text = "总告警数: 0, 新告警: 0"

                getSystemAddress(battery!!.lac, battery!!.cid, 0)
                tv_system_time.text = DateUtil.getTimeInterval(battery!!.createTime)
                tv_system_type.text = battery?.catalogName

                detailFragment = SystemDetailFragment(battery!!)
                listFragment = BatteryListFragment(battery!!)
            }
        }

        val list = ArrayList<Fragment>()
        if (detailFragment != null) {
            list.add(detailFragment!!)
        }
        if (listFragment != null) {
            list.add(listFragment!!)
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
                    ll_device_title1.visibility = View.VISIBLE
                    ll_device_title2.visibility = View.GONE
                } else {
                    v_item_2.setBackgroundResource(R.drawable.ic_item_selected)
                    v_item_1.setBackgroundResource(R.drawable.ic_item_normal)
                    ll_device_title1.visibility = View.GONE
                    ll_device_title2.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        vp_device.clearOnPageChangeListeners()
        super.onDestroy()
    }

    private fun getSystemAddress(lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(this@SystemMainActivity, userApplication.token, lac, cid, type, object :
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
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, SystemMainActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
