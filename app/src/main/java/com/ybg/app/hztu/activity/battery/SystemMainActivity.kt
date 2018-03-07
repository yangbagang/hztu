package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.BatteryItemAdapter
import com.ybg.app.hztu.adapter.RecyclerBaseAdapter
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout
import kotlinx.android.synthetic.main.activity_system_main.*

class SystemMainActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!
    private var battery: Battery? = null

    private var hasMore = true
    private val pageSize = 15//每页取5条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        batteryItemAdapter = BatteryItemAdapter(this@SystemMainActivity)
        batteryItemAdapter.setDataList(batteryList)
        batteryItemAdapter.setOnItemClickListener(systemItemClickListener)
        rv_battery_list.adapter = batteryItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        rv_battery_list.layoutManager = LinearLayoutManager(this@SystemMainActivity, layoutManager, false)
        rv_battery_list.itemAnimator = DefaultItemAnimator()
        rv_battery_list.addItemDecoration(SpaceItemDecoration(2))

        rl_fresh_layout.setRefreshViewHolder(BGANormalRefreshViewHolder(this@SystemMainActivity, true))
        rl_fresh_layout.setDelegate(mDelegate)

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if(verticalOffset == 0) {
                //展开状态，可以刷新、不可以加载更多
                rl_fresh_layout.setRefreshEnable(true)
                rl_fresh_layout.setLoadingEnable(false)
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠状态，可以加载更多，不可以刷新
                rl_fresh_layout.setRefreshEnable(false)
                rl_fresh_layout.setLoadingEnable(true)
            } else {
                //中间状态，不可以加载更多，不可以刷新
                rl_fresh_layout.setRefreshEnable(false)
                rl_fresh_layout.setLoadingEnable(false)
            }
        }

        detailButton.setOnClickListener {
            if (battery != null) {
                SystemDetailActivity.start(this@SystemMainActivity, battery!!)
            }
        }

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
            if (battery != null) {
                //TODO 根据UID不同显示不同图标
                if (battery!!.uid.startsWith("WLCB")) {

                } else if (battery!!.uid.startsWith("WLCD")) {

                } else if (battery!!.uid.startsWith("WLCU")) {

                }

                if (battery!!.name != "") {
                    tv_system_name.text = battery!!.name
                } else {
                    tv_system_name.text = battery!!.uid
                }
                tv_system_value.text = String.format("BI: %f, BTV: %f", battery!!.bi, battery!!.btv)

                getSystemAddress(battery!!.lac, battery!!.cid, 0)
                tv_system_time.text = DateUtil.getTimeInterval(battery!!.createTime)
                // 填充数据
                rl_fresh_layout.beginRefreshing()
            }
        }
    }
    private fun getSystemAddress(lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(this@SystemMainActivity, userApplication.token, lac, cid, type, object :
                JsonCallback(){
            override fun onJsonSuccess(data: String) {
                tv_system_address.text = data
            }
        })
    }


    private fun getBatteryList() {
        if (!userApplication.hasLogin()) return
        SendRequest.getBatteryDataByUid(this@SystemMainActivity, userApplication.token, battery!!.uid,
                pageSize, pageNum, object : JsonCallback() {
            override fun onSuccess(code: Int, response: String) {
                if (pageNum == 1) {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_REFRESH
                    message.obj = response
                    mShowHandler.sendMessage(message)
                } else {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_LOADMORE
                    message.obj = response
                    mShowHandler.sendMessage(message)
                }
            }

            override fun onFailure(e: Throwable) {
                rl_fresh_layout.endRefreshing()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_chart -> {
                if (battery != null) {
                    SystemLineChartActivity.start(this@SystemMainActivity, battery!!)
                }
            }
            R.id.action_detail -> {
                if (battery != null) {
                    SystemHistoryActivity.start(this@SystemMainActivity, battery!!)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mShowHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val gson = Gson()
            val jSonResultBean = JSonResultBean.fromJSON(msg.obj.toString())
            var list: List<Battery> = java.util.ArrayList()
            if (jSonResultBean != null && jSonResultBean.isSuccess) {
                list = gson.fromJson<List<Battery>>(jSonResultBean.data, object : TypeToken<List<Battery>>() {

                }.type)
            }

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
            getBatteryList()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                getBatteryList()
            } else {
                ToastUtil.show(userApplication, "没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
    }

    private val systemItemClickListener = object : RecyclerBaseAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            val battery = batteryList[position]
            BatteryDetailActivity.start(this@SystemMainActivity, battery)
        }
    }

    companion object {
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, SystemMainActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
