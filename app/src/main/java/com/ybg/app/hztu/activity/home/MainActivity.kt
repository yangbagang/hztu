package com.ybg.app.hztu.activity.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.hztu.R
import com.ybg.app.hztu.activity.battery.SystemMainActivity
import com.ybg.app.hztu.activity.user.LoginActivity
import com.ybg.app.hztu.activity.user.UpdateActivity
import com.ybg.app.hztu.adapter.SystemItemAdapter
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private lateinit var systemItemAdapter: SystemItemAdapter
    private var batteryList = ArrayList<Battery>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        systemItemAdapter = SystemItemAdapter(this@MainActivity)
        systemItemAdapter.setDataList(batteryList)
        lv_battery_list.adapter = systemItemAdapter
        lv_battery_list.setOnItemClickListener { adapterView, view, i, l ->
            val battery = batteryList[i]
            SystemMainActivity.start(this@MainActivity, battery)
        }

        if (userApplication.hasLogin()) {
            getBatteryList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (!userApplication.hasLogin()) return true
        if (id == R.id.action_edit) {
            UpdateActivity.start(this@MainActivity)
            return true
        } else if (id == R.id.action_logout) {
            SendRequest.userLogout(this@MainActivity, userApplication.token, object : JsonCallback() {
            })
            logout()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        userApplication.token = ""
        userApplication.userInfo = null
        LoginActivity.start(this@MainActivity)
        finish()
    }

    private fun getBatteryList() {
        if (!userApplication.hasLogin()) return
        batteryList.clear()
        SendRequest.getBatteryBSList(this@MainActivity, userApplication.token, jsonCallback)
        SendRequest.getBatteryDCList(this@MainActivity, userApplication.token, jsonCallback)
        SendRequest.getBatteryUPSList(this@MainActivity, userApplication.token, jsonCallback)
    }

    private val jsonCallback = object : JsonCallback() {
        override fun onJsonSuccess(data: String) {
            super.onJsonSuccess(data)
            val gson = Gson()
            batteryList.addAll(gson.fromJson<List<Battery>>(data, object : TypeToken<List<Battery>>() {}.type))
            systemItemAdapter.setDataList(batteryList)
            systemItemAdapter.notifyDataSetChanged()
        }

        override fun onJsonFail(jsonBean: JSonResultBean) {
            if (jsonBean.message.contains("重新登录")) {
                logout()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}
