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
import com.ybg.app.hztu.adapter.RecyclerBaseAdapter
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout

@SuppressLint("ValidFragment")
class BatteryListFragment(var device: Battery) : Fragment(), ScrollableView {

    private val userApplication = UserApplication.instance!!
    private var hasMore = true
    private val pageSize = 15//每页取5条
    private var pageNum = 1//页码

    private val TYPE_REFRESH = 0//下拉刷新
    private val TYPE_LOADMORE = 1//上拉加载

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    private lateinit var freshLayout: BGARefreshLayout
    private lateinit var batteryRecyclerView: RecyclerView

    override fun setRefreshEnable(enable: Boolean) {
        println("setRefreshEnable")
    }

    override fun setLoadingEnable(enable: Boolean) {
        println("setLoadingEnable")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_battery_list, container)
        freshLayout = view.findViewById(R.id.rl_fresh_layout)
        batteryRecyclerView = view.findViewById(R.id.rv_battery_list)

        initView()

        return view
    }

    private fun initView() {
        if (activity == null) return
        batteryItemAdapter = BatteryItemAdapter(activity!!)
        batteryItemAdapter.setDataList(batteryList)
        batteryItemAdapter.setOnItemClickListener(systemItemClickListener)
        batteryRecyclerView.adapter = batteryItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        batteryRecyclerView.layoutManager = LinearLayoutManager(activity, layoutManager, false)
        batteryRecyclerView.itemAnimator = DefaultItemAnimator()
        batteryRecyclerView.addItemDecoration(SpaceItemDecoration(2))

        freshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(activity!!, true))
        freshLayout.setDelegate(mDelegate)

        freshLayout.beginRefreshing()
    }

    private fun getBatteryList() {
        if (!userApplication.hasLogin()) return
        SendRequest.getBatteryDataByUid(activity!!, userApplication.token, device.uid,
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
                freshLayout.endRefreshing()
            }
        })
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
            activity?.let {
                BatteryMainActivity.start(activity!!, device, battery.id)
            }
        }
    }

}