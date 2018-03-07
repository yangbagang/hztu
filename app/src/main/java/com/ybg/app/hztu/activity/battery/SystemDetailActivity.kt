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
import kotlinx.android.synthetic.main.activity_system_detail.*

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
                val uid = battery!!.uid
                if (uid.startsWith("WLCB")) {
                    insertBSFields()
                } else if (uid.startsWith("WLCD")) {
                    insertDCFields()
                } else if (uid.startsWith("WLCU")) {
                    insertUPSFields()
                }
            }
        }
    }

    private fun insertBSFields() {
        ll_battery_values.addView(getValueView("电池电流(BI)", "${battery!!.bi}"))
        ll_battery_values.addView(getValueView("电池组电压(BTV)", "${battery!!.btv}"))
        ll_battery_values.addView(getValueView("报警(ALM)", "${battery!!.alm}"))
    }

    private fun insertDCFields() {
        ll_battery_values.addView(getValueView("电池电流(BI)", "${battery!!.bi}"))
        ll_battery_values.addView(getValueView("R相市电电压(mepur)", "${battery!!.mepur}"))
        ll_battery_values.addView(getValueView("S相市电电压(mepus)", "${battery!!.mepus}"))
        ll_battery_values.addView(getValueView("T相市电电压(meput)", "${battery!!.meput}"))
        ll_battery_values.addView(getValueView("市电频率(mepf)", "${battery!!.mepf}"))
        ll_battery_values.addView(getValueView("电池组电压(btv)", "${battery!!.btv}"))
        ll_battery_values.addView(getValueView("电池充电电流(bcc)", "${battery!!.bcc}"))
        ll_battery_values.addView(getValueView("电池放电电流(bdc)", "${battery!!.bdc}"))
        ll_battery_values.addView(getValueView("正母线电压(mp)", "${battery!!.mp}"))
        ll_battery_values.addView(getValueView("负母线电压(mn)", "${battery!!.mn}"))
        ll_battery_values.addView(getValueView("合母电压(hmv)", "${battery!!.hmv}"))
        ll_battery_values.addView(getValueView("控母电压(kmv)", "${battery!!.kmv}"))
        ll_battery_values.addView(getValueView("输出电流(oi)", "${battery!!.oi}"))
        ll_battery_values.addView(getValueView("报警(alm)", "${battery!!.alm}"))
    }

    private fun insertUPSFields() {
        ll_battery_values.addView(getValueView("电池电流(BI)", "${battery!!.bi}"))
        ll_battery_values.addView(getValueView("R相市电电压(mepur)", "${battery!!.mepur}"))
        ll_battery_values.addView(getValueView("S相市电电压(mepus)", "${battery!!.mepus}"))
        ll_battery_values.addView(getValueView("T相市电电压(meput)", "${battery!!.meput}"))
        ll_battery_values.addView(getValueView("市电频率(mepf)", "${battery!!.mepf}"))
        ll_battery_values.addView(getValueView("R相旁路电压(bepur)", "${battery!!.bepur}"))
        ll_battery_values.addView(getValueView("S相旁路电压(bepus)", "${battery!!.bepus}"))
        ll_battery_values.addView(getValueView("T相旁路电压(beput)", "${battery!!.beput}"))
        ll_battery_values.addView(getValueView("旁路频率(bepf)", "${battery!!.bepf}"))
        ll_battery_values.addView(getValueView("R相逆变电压(iepur)", "${battery!!.iepur}"))
        ll_battery_values.addView(getValueView("S相逆变电压(iepus)", "${battery!!.iepus}"))
        ll_battery_values.addView(getValueView("T相逆变电压(ieput)", "${battery!!.ieput}"))
        ll_battery_values.addView(getValueView("逆变频率(iepf)", "${battery!!.iepf}"))
        ll_battery_values.addView(getValueView("R相输出电压(oepur)", "${battery!!.oepur}"))
        ll_battery_values.addView(getValueView("S相输出电压(oepus)", "${battery!!.oepus}"))
        ll_battery_values.addView(getValueView("T相输出电压(oeput)", "${battery!!.oeput}"))
        ll_battery_values.addView(getValueView("输出频率(oepf)", "${battery!!.oepf}"))
        ll_battery_values.addView(getValueView("R相输出电流(ocr)", "${battery!!.ocr}"))
        ll_battery_values.addView(getValueView("S相输出电流(ocs)", "${battery!!.ocs}"))
        ll_battery_values.addView(getValueView("T相输出电流(oct)", "${battery!!.oct}"))
        ll_battery_values.addView(getValueView("R相输出功率(opr)", "${battery!!.opr}"))
        ll_battery_values.addView(getValueView("S相输出功率(ops)", "${battery!!.ops}"))
        ll_battery_values.addView(getValueView("T相输出功率(opt)", "${battery!!.opt}"))
        ll_battery_values.addView(getValueView("输出总功率(otp)", "${battery!!.otp}"))
        ll_battery_values.addView(getValueView("RS输出功率因素(opfrs)", "${battery!!.opfrs}"))
        ll_battery_values.addView(getValueView("ST输出功率因素(opfst)", "${battery!!.opfst}"))
        ll_battery_values.addView(getValueView("TR输出功率因素(opftr)", "${battery!!.opftr}"))
        ll_battery_values.addView(getValueView("电池组电压(btv)", "${battery!!.btv}"))
        ll_battery_values.addView(getValueView("母线电压(bbv)", "${battery!!.bbv}"))
        ll_battery_values.addView(getValueView("电池充电电流(bcc)", "${battery!!.bcc}"))
        ll_battery_values.addView(getValueView("电池放电电流(bdc)", "${battery!!.bdc}"))
        ll_battery_values.addView(getValueView("报警(alm)", "${battery!!.alm}"))
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
