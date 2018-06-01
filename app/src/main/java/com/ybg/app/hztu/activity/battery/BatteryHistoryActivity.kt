package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.BatteryItemAdapter
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout
import kotlinx.android.synthetic.main.activity_battery_history.*

@Deprecated("delete")
class BatteryHistoryActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    private var battery: Battery? = null

    private var hasMore = true
    private val pageSize = 20//每页取20条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        batteryItemAdapter = BatteryItemAdapter(this@BatteryHistoryActivity)
        batteryItemAdapter.setDataList(batteryList)
        rv_battery_list.adapter = batteryItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        rv_battery_list.layoutManager = LinearLayoutManager(this@BatteryHistoryActivity, layoutManager, false)
        rv_battery_list.itemAnimator = DefaultItemAnimator()
        rv_battery_list.addItemDecoration(SpaceItemDecoration(2))

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
        }

        rl_fresh_layout.setRefreshViewHolder(BGANormalRefreshViewHolder(this@BatteryHistoryActivity, true))
        rl_fresh_layout.setDelegate(mDelegate)
    }

    override fun onStart() {
        super.onStart()
        if (userApplication.hasLogin() && battery != null) {
            rl_fresh_layout.beginRefreshing()
        }
    }

    private fun logout() {
        userApplication.token = ""
        userApplication.userInfo = null
        finish()
    }

    private fun getBatteryHistory() {
        if (!userApplication.hasLogin()) return
        SendRequest.getBatteryDataList(this@BatteryHistoryActivity, userApplication.token, battery!!.id,
                pageSize, pageNum, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                if (pageNum == 1) {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_REFRESH
                    message.obj = data
                    mShowHandler.sendMessage(message)
                } else {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_LOADMORE
                    message.obj = data
                    mShowHandler.sendMessage(message)
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                super.onJsonFail(jsonBean)
                rl_fresh_layout.endRefreshing()
                ToastUtil.show(userApplication, jsonBean.message)
                if (jsonBean.message.contains("重新登录")) {
                    logout()
                }
            }
        })
    }

    /**
     * 模拟请求网络数据
     */
    private val mShowHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val gson = Gson()
            var list: List<Battery> = gson.fromJson<List<Battery>>(msg.obj.toString(), object : TypeToken<List<Battery>>() {

            }.type)

            hasMore = list.size == pageSize

            when (msg.what) {
                TYPE_REFRESH -> {
                    rl_fresh_layout.endRefreshing()
                    batteryList.clear()
                    batteryList.addAll(list)
                }
                TYPE_LOADMORE -> {
                    rl_fresh_layout.endLoadingMore()
                    batteryList.addAll(list)
                }
            }
            println(batteryList.size)
            batteryItemAdapter.setDataList(batteryList)
            batteryItemAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 监听 刷新或者上拉
     */
    private val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            pageNum = 1
            getBatteryHistory()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                getBatteryHistory()
            } else {
                ToastUtil.show(userApplication, "没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
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
            val starter = Intent(context, BatteryHistoryActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
