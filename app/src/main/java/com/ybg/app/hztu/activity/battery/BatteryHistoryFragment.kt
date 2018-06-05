package com.ybg.app.hztu.activity.battery

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.scroll.ScrollableView
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.BatteryItemAdapter
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout

@SuppressLint("ValidFragment")
class BatteryHistoryFragment(var batteryId: Long) : Fragment(), ScrollableView {

    override fun setRefreshEnable(enable: Boolean) {
        freshLayout.setRefreshEnable(enable)
    }

    override fun setLoadingEnable(enable: Boolean) {
        freshLayout.setLoadingEnable(enable)
    }

    private val userApplication = UserApplication.instance!!

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    private var hasMore = true
    private val pageSize = 20//每页取20条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var freshLayout: BGARefreshLayout
    private lateinit var listView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_battery_history, container, false)

        initView(view)

        return view
    }

    private fun initView(rootView: View) {
        freshLayout = rootView.findViewById(R.id.rl_fresh_layout)
        listView = rootView.findViewById(R.id.rv_battery_list)

        if (activity == null) return

        batteryItemAdapter = BatteryItemAdapter(activity!!)
        batteryItemAdapter.setDataList(batteryList)
        listView.adapter = batteryItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        listView.layoutManager = LinearLayoutManager(activity!!, layoutManager, false)
        listView.itemAnimator = DefaultItemAnimator()
        listView.addItemDecoration(SpaceItemDecoration(2))

        freshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(activity!!, true))
        freshLayout.setDelegate(mDelegate)

        freshLayout.beginRefreshing()
    }

    private fun getBatteryHistory() {
        if (!userApplication.hasLogin()) return
        SendRequest.getBatteryHistoryDataList(activity!!, userApplication.token, batteryId,
                pageSize, pageNum, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
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
                freshLayout.endRefreshing()
                ToastUtil.show(userApplication, jsonBean.message)
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
                    freshLayout.endRefreshing()
                    batteryList.clear()
                    batteryList.addAll(list)
                }
                TYPE_LOADMORE -> {
                    freshLayout.endLoadingMore()
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
}