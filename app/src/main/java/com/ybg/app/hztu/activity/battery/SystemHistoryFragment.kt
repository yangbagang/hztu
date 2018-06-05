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
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.LineChartItem
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.scroll.ScrollableView
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.DeviceItemAdapter
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout


@SuppressLint("ValidFragment")
class SystemHistoryFragment(var uid: String, var key: String) : Fragment(), ScrollableView {

    private val userApplication = UserApplication.instance!!

    private lateinit var deviceItemAdapter: DeviceItemAdapter
    private var batteryList = ArrayList<LineChartItem>()

    private var hasMore = true
    private val pageSize = 20//每页取20条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var freshLayout: BGARefreshLayout
    private lateinit var historyListView: RecyclerView

    override fun setRefreshEnable(enable: Boolean) {
        freshLayout.setRefreshEnable(enable)
    }

    override fun setLoadingEnable(enable: Boolean) {
        freshLayout.setLoadingEnable(enable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_system_history, container, false)
        initView(view)
        return view
    }

    private fun initView(rootView: View) {
        if (activity == null) return
        freshLayout = rootView.findViewById(R.id.rl_fresh_layout)
        historyListView = rootView.findViewById(R.id.rv_battery_list)

        deviceItemAdapter = DeviceItemAdapter(activity!!)
        deviceItemAdapter.setDataList(batteryList)
        historyListView.adapter = deviceItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        historyListView.layoutManager = LinearLayoutManager(activity!!, layoutManager, false)
        historyListView.itemAnimator = DefaultItemAnimator()
        historyListView.addItemDecoration(SpaceItemDecoration(2))

        freshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(activity!!, true))
        freshLayout.setDelegate(mDelegate)

        freshLayout.beginRefreshing()
    }

    private fun getDeviceHistory() {
        if (!userApplication.hasLogin()) return
        SendRequest.getDeviceKeyDataList(activity!!, userApplication.token, uid, key, pageSize,
                pageNum, jsonCallBack)
    }

    private val jsonCallBack = object : JsonCallback() {
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
            freshLayout.endRefreshing()
            ToastUtil.show(userApplication, jsonBean.message)
        }
    }

    /**
     * 模拟请求网络数据
     */
    private val mShowHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val gson = Gson()
            var list: List<LineChartItem> = gson.fromJson<List<LineChartItem>>(msg.obj.toString(), object : TypeToken<List<LineChartItem>>() {

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
            println(batteryList.size)
            deviceItemAdapter.setDataList(batteryList)
            deviceItemAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 监听 刷新或者上拉
     */
    private val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            pageNum = 1
            getDeviceHistory()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                getDeviceHistory()
            } else {
                ToastUtil.show(userApplication, "没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
    }

}