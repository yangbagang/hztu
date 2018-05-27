package com.ybg.app.hztu.activity.battery

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.hztu.R

@SuppressLint("ValidFragment")
class SystemDetailFragment(var battery: Battery) : Fragment() {

    private var valueList: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_system_detail, container)
        valueList = view.findViewById(R.id.ll_battery_values)
        loadValue()
        return view
    }

    fun enableScollable(enable: Boolean) {

    }
    
    private fun loadValue() {
        when(battery.catalogId) {
            1 -> insertBSFields()
        }
    }

    private fun insertBSFields() {
        valueList?.addView(getValueView("电池电流(BI)", "${battery.bi}"))
        valueList?.addView(getValueView("电池组电压(BTV)", "${battery.btv}"))
        valueList?.addView(getValueView("报警(ALM)", "${battery.alm}"))
    }

    private fun insertDCFields() {
        valueList?.addView(getValueView("电池电流(BI)", "${battery.bi}"))
        valueList?.addView(getValueView("R相市电电压(mepur)", "${battery.mepur}"))
        valueList?.addView(getValueView("S相市电电压(mepus)", "${battery.mepus}"))
        valueList?.addView(getValueView("T相市电电压(meput)", "${battery.meput}"))
        valueList?.addView(getValueView("市电频率(mepf)", "${battery.mepf}"))
        valueList?.addView(getValueView("电池组电压(btv)", "${battery.btv}"))
        valueList?.addView(getValueView("电池充电电流(bcc)", "${battery.bcc}"))
        valueList?.addView(getValueView("电池放电电流(bdc)", "${battery.bdc}"))
        valueList?.addView(getValueView("正母线电压(mp)", "${battery.mp}"))
        valueList?.addView(getValueView("负母线电压(mn)", "${battery.mn}"))
        valueList?.addView(getValueView("合母电压(hmv)", "${battery.hmv}"))
        valueList?.addView(getValueView("控母电压(kmv)", "${battery.kmv}"))
        valueList?.addView(getValueView("输出电流(oi)", "${battery.oi}"))
        valueList?.addView(getValueView("报警(alm)", "${battery.alm}"))
    }

    private fun insertUPSFields() {
        valueList?.addView(getValueView("电池电流(BI)", "${battery.bi}"))
        valueList?.addView(getValueView("R相市电电压(mepur)", "${battery.mepur}"))
        valueList?.addView(getValueView("S相市电电压(mepus)", "${battery.mepus}"))
        valueList?.addView(getValueView("T相市电电压(meput)", "${battery.meput}"))
        valueList?.addView(getValueView("市电频率(mepf)", "${battery.mepf}"))
        valueList?.addView(getValueView("R相旁路电压(bepur)", "${battery.bepur}"))
        valueList?.addView(getValueView("S相旁路电压(bepus)", "${battery.bepus}"))
        valueList?.addView(getValueView("T相旁路电压(beput)", "${battery.beput}"))
        valueList?.addView(getValueView("旁路频率(bepf)", "${battery.bepf}"))
        valueList?.addView(getValueView("R相逆变电压(iepur)", "${battery.iepur}"))
        valueList?.addView(getValueView("S相逆变电压(iepus)", "${battery.iepus}"))
        valueList?.addView(getValueView("T相逆变电压(ieput)", "${battery.ieput}"))
        valueList?.addView(getValueView("逆变频率(iepf)", "${battery.iepf}"))
        valueList?.addView(getValueView("R相输出电压(oepur)", "${battery.oepur}"))
        valueList?.addView(getValueView("S相输出电压(oepus)", "${battery.oepus}"))
        valueList?.addView(getValueView("T相输出电压(oeput)", "${battery.oeput}"))
        valueList?.addView(getValueView("输出频率(oepf)", "${battery.oepf}"))
        valueList?.addView(getValueView("R相输出电流(ocr)", "${battery.ocr}"))
        valueList?.addView(getValueView("S相输出电流(ocs)", "${battery.ocs}"))
        valueList?.addView(getValueView("T相输出电流(oct)", "${battery.oct}"))
        valueList?.addView(getValueView("R相输出功率(opr)", "${battery.opr}"))
        valueList?.addView(getValueView("S相输出功率(ops)", "${battery.ops}"))
        valueList?.addView(getValueView("T相输出功率(opt)", "${battery.opt}"))
        valueList?.addView(getValueView("输出总功率(otp)", "${battery.otp}"))
        valueList?.addView(getValueView("RS输出功率因素(opfrs)", "${battery.opfrs}"))
        valueList?.addView(getValueView("ST输出功率因素(opfst)", "${battery.opfst}"))
        valueList?.addView(getValueView("TR输出功率因素(opftr)", "${battery.opftr}"))
        valueList?.addView(getValueView("电池组电压(btv)", "${battery.btv}"))
        valueList?.addView(getValueView("母线电压(bbv)", "${battery.bbv}"))
        valueList?.addView(getValueView("电池充电电流(bcc)", "${battery.bcc}"))
        valueList?.addView(getValueView("电池放电电流(bdc)", "${battery.bdc}"))
        valueList?.addView(getValueView("报警(alm)", "${battery.alm}"))
    }

    private fun getValueView(label: String, value: String): View {
        val item = layoutInflater.inflate(R.layout.battery_value_content, null)
        val itemLabel = item.findViewById<TextView>(R.id.tv_item_label)
        val itemValue = item.findViewById<TextView>(R.id.tv_item_value)
        itemLabel.text = label
        itemValue.text = value
        return item
    }
}