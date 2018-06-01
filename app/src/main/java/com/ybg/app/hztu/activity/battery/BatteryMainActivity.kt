package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.ybg.app.base.bean.Battery

class BatteryMainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, BatteryMainActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}