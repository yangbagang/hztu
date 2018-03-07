package com.ybg.app.hztu.activity.battery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_battery_detail.*

class SystemDetailActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!
    private var battery: Battery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            battery = intent.extras.get("battery") as Battery
            if (battery != null) {
                // 填充数据
                ll_battery_values.removeAllViews()
                ll_battery_values.addView(getValueView("ID", "${battery!!.id}"))
            }
        }
    }

    private fun getValueView(label: String, value: String): View {
        val item = layoutInflater.inflate(R.layout.battery_value_content, null)
        val itemLabel = item.findViewById<TextView>(R.id.tv_item_label)
        val itemValue = item.findViewById<TextView>(R.id.tv_item_value)
        itemLabel.text = label
        itemValue.text = value
        return item
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
                    SystemLineChartActivity.start(this@SystemDetailActivity, battery!!)
                }
            }
            R.id.action_detail -> {
                if (battery != null) {
                    SystemHistoryActivity.start(this@SystemDetailActivity, battery!!)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, battery: Battery) {
            val starter = Intent(context, SystemDetailActivity::class.java)
            starter.putExtra("battery", battery)
            context.startActivity(starter)
        }
    }
}
