package com.ybg.app.hztu.activity.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.activity.user.LoginActivity
import com.ybg.app.hztu.activity.user.UpdateActivity
import com.ybg.app.hztu.adapter.BatteryItemAdapter
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    private lateinit var batteryItemAdapter: BatteryItemAdapter
    private var batteryList = ArrayList<Battery>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryItemAdapter = BatteryItemAdapter(this@MainActivity)
        batteryItemAdapter.setDataList(batteryList)
        rv_battery_list.adapter = batteryItemAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        rv_battery_list.layoutManager = LinearLayoutManager(this@MainActivity, layoutManager, false)
        rv_battery_list.itemAnimator = DefaultItemAnimator()
        rv_battery_list.addItemDecoration(SpaceItemDecoration(2))
    }

    override fun onStart() {
        super.onStart()
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
            SendRequest.userLogout(this@MainActivity, userApplication.token, object : JsonCallback(){
                override fun onSuccess(code: Int, response: String) {
                    super.onSuccess(code, response)
                    logout()
                }

                override fun onJsonSuccess(data: String) {
                    super.onJsonSuccess(data)
                    logout()
                }
            })
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
        SendRequest.getBatteryList(this@MainActivity, userApplication.token, object : JsonCallback(){
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

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}