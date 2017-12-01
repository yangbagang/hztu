package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_battery_history.*

class BatteryHistoryActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    private var battery: Battery? = null

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
    }

    override fun onStart() {
        super.onStart()
        if (userApplication.hasLogin() && battery != null) {
            getBatteryHistory()
        }
    }

    private fun logout() {
        userApplication.token = ""
        userApplication.userInfo = null
        finish()
    }

    private fun getBatteryHistory() {
        if (!userApplication.hasLogin()) return
        SendRequest.getBatteryDataList(this@BatteryHistoryActivity, userApplication.token, battery!!.id, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val gson = Gson()
                batteryList.clear()
                batteryList.addAll(gson.fromJson<List<Battery>>(data, object : TypeToken<List<Battery>>(){}.type))
                batteryItemAdapter.setDataList(batteryList)
                batteryItemAdapter.notifyDataSetChanged()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                super.onJsonFail(jsonBean)
                ToastUtil.show(userApplication, jsonBean.message)
                if (jsonBean.message.contains("重新登录")) {
                    logout()
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
            val starter = Intent(context, BatteryHistoryActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
