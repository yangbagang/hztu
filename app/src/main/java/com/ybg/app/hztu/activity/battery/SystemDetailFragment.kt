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
import com.ybg.app.base.scroll.ScrollableView
import com.ybg.app.hztu.R

@SuppressLint("ValidFragment")
class SystemDetailFragment(var battery: Battery) : Fragment(), ScrollableView {

    override fun setRefreshEnable(enable: Boolean) {
        println("setRefreshEnable: $enable")
    }

    override fun setLoadingEnable(enable: Boolean) {
        println("setLoadingEnable: $enable")
    }

    private var valueList: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_system_detail, container)
        valueList = view.findViewById(R.id.ll_battery_values)
        loadValue()
        return view
    }
    
    private fun loadValue() {
        when(battery.catalogId) {
            0 -> insertBSFields()
            1 -> insertUPS1Fields()
            2 -> insertUPS2Fields()
            3 -> insertUPS3Fields()
            4 -> insertDC1Fields()
            5 -> insertDC2Fields()
        }
    }

    private fun insertBSFields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
    }

    private fun insertDC1Fields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
        valueList?.addView(getValueView("R相市电电压", "${battery.mepur}", "V", "mepur"))
        valueList?.addView(getValueView("S相市电电压", "${battery.mepus}", "V", "mepus"))
        valueList?.addView(getValueView("T相市电电压", "${battery.meput}", "V", "meput"))
        valueList?.addView(getValueView("市电频率", "${battery.mepf}", "Hz", "mepf"))
        valueList?.addView(getValueView("电池充电电流", "${battery.bcc}", "A", "bcc"))
        valueList?.addView(getValueView("电池放电电流", "${battery.bdc}", "A", "bdc"))
        valueList?.addView(getValueView("正母线电压", "${battery.mp}", "V", "mp"))
        valueList?.addView(getValueView("负母线电压", "${battery.mn}", "V", "mn"))
        valueList?.addView(getValueView("合母电压", "${battery.hmv}", "V", "hmv"))
        valueList?.addView(getValueView("控母电压", "${battery.kmv}", "V", "kmv"))
        valueList?.addView(getValueView("输出电流", "${battery.oi}", "A", "oi"))
    }

    private fun insertDC2Fields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
        valueList?.addView(getValueView("R相市电电压", "${battery.mepur}", "V", "mepur"))
        valueList?.addView(getValueView("市电频率", "${battery.mepf}", "Hz", "mepf"))
        valueList?.addView(getValueView("电池充电电流", "${battery.bcc}", "A", "bcc"))
        valueList?.addView(getValueView("电池放电电流", "${battery.bdc}", "A", "bdc"))
        valueList?.addView(getValueView("正母线电压", "${battery.mp}", "V", "mp"))
        valueList?.addView(getValueView("负母线电压", "${battery.mn}", "V", "mn"))
        valueList?.addView(getValueView("合母电压", "${battery.hmv}", "V", "hmv"))
        valueList?.addView(getValueView("控母电压", "${battery.kmv}", "V", "kmv"))
        valueList?.addView(getValueView("输出电流", "${battery.oi}", "A", "oi"))
    }

    private fun insertUPS1Fields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
        valueList?.addView(getValueView("R相市电电压", "${battery.mepur}", "V", "mepur"))
        valueList?.addView(getValueView("S相市电电压", "${battery.mepus}", "V", "mepus"))
        valueList?.addView(getValueView("T相市电电压", "${battery.meput}", "V", "meput"))
        valueList?.addView(getValueView("市电频率", "${battery.mepf}", "Hz", "mepf"))
        valueList?.addView(getValueView("R相旁路电压", "${battery.bepur}", "V", "bepur"))
        valueList?.addView(getValueView("S相旁路电压", "${battery.bepus}", "V", "pepus"))
        valueList?.addView(getValueView("T相旁路电压", "${battery.beput}", "V", "beput"))
        valueList?.addView(getValueView("旁路频率", "${battery.bepf}", "Hz", "bepf"))
        valueList?.addView(getValueView("R相逆变电压", "${battery.iepur}", "V", "iepur"))
        valueList?.addView(getValueView("S相逆变电压", "${battery.iepus}", "V", "iepus"))
        valueList?.addView(getValueView("T相逆变电压", "${battery.ieput}", "V", "ieput"))
        valueList?.addView(getValueView("逆变频率", "${battery.iepf}", "Hz", "iepf"))
        valueList?.addView(getValueView("R相输出电压", "${battery.oepur}", "V", "oepur"))
        valueList?.addView(getValueView("S相输出电压", "${battery.oepus}", "V", "oepus"))
        valueList?.addView(getValueView("T相输出电压", "${battery.oeput}", "V", "oeput"))
        valueList?.addView(getValueView("输出频率", "${battery.oepf}", "Hz", "oepf"))
        valueList?.addView(getValueView("R相输出电流", "${battery.ocr}", "A", "ocr"))
        valueList?.addView(getValueView("S相输出电流", "${battery.ocs}", "A", "ocs"))
        valueList?.addView(getValueView("T相输出电流", "${battery.oct}", "A", "oct"))
        valueList?.addView(getValueView("R相输出功率", "${battery.opr}", "KVA", "opr"))
        valueList?.addView(getValueView("S相输出功率", "${battery.ops}", "KVA", "ops"))
        valueList?.addView(getValueView("T相输出功率", "${battery.opt}", "KVA", "opt"))
        valueList?.addView(getValueView("输出总功率", "${battery.otp}", "KVA", "otp"))
        valueList?.addView(getValueView("RS输出功率因素", "${battery.opfrs}", "", "opfrs"))
        valueList?.addView(getValueView("ST输出功率因素", "${battery.opfst}", "", "opfst"))
        valueList?.addView(getValueView("TR输出功率因素", "${battery.opftr}", "", "opftr"))
        valueList?.addView(getValueView("母线电压", "${battery.bbv}", "V", "bbv"))
        valueList?.addView(getValueView("电池充电电流", "${battery.bcc}", "A", "bcc"))
        valueList?.addView(getValueView("电池放电电流", "${battery.bdc}", "A", "bdc"))
    }

    private fun insertUPS2Fields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
        valueList?.addView(getValueView("R相市电电压", "${battery.mepur}", "V", "mepur"))
        valueList?.addView(getValueView("S相市电电压", "${battery.mepus}", "V", "mepus"))
        valueList?.addView(getValueView("T相市电电压", "${battery.meput}", "V", "meput"))
        valueList?.addView(getValueView("市电频率", "${battery.mepf}", "Hz", "mepf"))
        valueList?.addView(getValueView("R相旁路电压", "${battery.bepur}", "V", "bepur"))
        valueList?.addView(getValueView("S相旁路电压", "${battery.bepus}", "V", "bepus"))
        valueList?.addView(getValueView("T相旁路电压", "${battery.beput}", "V", "beput"))
        valueList?.addView(getValueView("旁路频率", "${battery.bepf}", "Hz", "bepf"))
        valueList?.addView(getValueView("R相逆变电压", "${battery.iepur}", "V", "iepur"))
        valueList?.addView(getValueView("S相逆变电压", "${battery.iepus}", "V", "iepus"))
        valueList?.addView(getValueView("T相逆变电压", "${battery.ieput}", "V", "ieput"))
        valueList?.addView(getValueView("逆变频率", "${battery.iepf}", "Hz", "iepf"))
        valueList?.addView(getValueView("输出电压", "${battery.oepur}", "V", "oepur"))
        valueList?.addView(getValueView("输出频率", "${battery.oepf}", "Hz", "oepf"))
        valueList?.addView(getValueView("输出电流", "${battery.ocr}", "A", "ocr"))
        valueList?.addView(getValueView("输出功率", "${battery.opr}", "KVA", "opr"))
        valueList?.addView(getValueView("输出功率因素", "${battery.opfrs}", "", "opfrs"))
        valueList?.addView(getValueView("母线电压", "${battery.bbv}", "V", "bbv"))
        valueList?.addView(getValueView("电池充电电流", "${battery.bcc}", "A", "bcc"))
        valueList?.addView(getValueView("电池放电电流", "${battery.bdc}", "A", "bdc"))
    }

    private fun insertUPS3Fields() {
        valueList?.addView(getValueView("电池电流", "${battery.bi}", "A", "bi"))
        valueList?.addView(getValueView("电池组电压", "${battery.btv}", "V", "btv"))
        valueList?.addView(getValueView("电池压差", "${battery.dp}", "V", "dp"))
        valueList?.addView(getValueView("单体最高电压", "${battery.chv}", "V", "chv"))
        valueList?.addView(getValueView("单体最低电压", "${battery.clv}", "V", "clv"))
        valueList?.addView(getValueView("单体最高温度", "${battery.cht}", "℃", "cht"))
        valueList?.addView(getValueView("单体最高内阻", "${battery.chr}", "mR", "chr"))
        valueList?.addView(getValueView("市电电压", "${battery.mepur}", "V", "mepur"))
        valueList?.addView(getValueView("市电频率", "${battery.mepf}", "Hz", "mepf"))
        valueList?.addView(getValueView("旁路电压", "${battery.bepur}", "V", "bepur"))
        valueList?.addView(getValueView("旁路频率", "${battery.bepf}", "Hz", "bepf"))
        valueList?.addView(getValueView("逆变电压", "${battery.iepur}", "V", "iepur"))
        valueList?.addView(getValueView("逆变频率", "${battery.iepf}", "Hz", "iepf"))
        valueList?.addView(getValueView("输出电压", "${battery.oepur}", "V", "oepur"))
        valueList?.addView(getValueView("输出频率", "${battery.oepf}", "Hz", "oepf"))
        valueList?.addView(getValueView("输出电流", "${battery.ocr}", "A", "ocr"))
        valueList?.addView(getValueView("输出功率", "${battery.opr}", "KVA", "opr"))
        valueList?.addView(getValueView("输出功率因素", "${battery.opfrs}", "", "opfrs"))
        valueList?.addView(getValueView("母线电压", "${battery.bbv}", "V", "bbv"))
        valueList?.addView(getValueView("电池充电电流", "${battery.bcc}", "A", "bcc"))
        valueList?.addView(getValueView("电池放电电流", "${battery.bdc}", "A", "bdc"))
    }

    private fun getValueView(label: String, value: String, unit: String, key: String): View {
        val item = layoutInflater.inflate(R.layout.device_value_content, null)
        val itemLabel = item.findViewById<TextView>(R.id.tv_item_label)
        val itemValue = item.findViewById<TextView>(R.id.tv_item_value)
        val itemUnit = item.findViewById<TextView>(R.id.tv_item_unit)
        itemLabel.text = label
        itemValue.text = value
        itemUnit.text = unit
        item.setOnClickListener {
            SystemValueActivity.start(activity!!, battery, String.format("%s(%s)",
                    key.toUpperCase(), label), key)
        }
        return item
    }

}