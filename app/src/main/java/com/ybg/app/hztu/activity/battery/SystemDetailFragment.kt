package com.ybg.app.hztu.activity.battery

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.decoration.SpaceItemDecoration
import com.ybg.app.base.scroll.ScrollableView
import com.ybg.app.hztu.R
import com.ybg.app.hztu.adapter.DeviceValueAdapter
import com.ybg.app.hztu.adapter.RecyclerBaseAdapter
import com.ybg.app.hztu.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.hztu.view.bgarefresh.BGARefreshLayout

@SuppressLint("ValidFragment")
class SystemDetailFragment(var battery: Battery) : Fragment(), ScrollableView {

    override fun setRefreshEnable(enable: Boolean) {
        freshLayout.setRefreshEnable(enable)
    }

    override fun setLoadingEnable(enable: Boolean) {
        freshLayout.setLoadingEnable(enable)
    }

    private lateinit var deviceValueAdapter: DeviceValueAdapter
    private var valueList = ArrayList<String>()

    private lateinit var freshLayout: BGARefreshLayout
    private lateinit var valueRecyclerView: RecyclerView

    private var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_system_detail, container, false)

        freshLayout = view.findViewById(R.id.rl_fresh_layout)
        valueRecyclerView = view.findViewById(R.id.rv_battery_list)

        initView()

        return view
    }

    private fun initView() {
        if (activity == null) return
        deviceValueAdapter = DeviceValueAdapter(activity!!)
        deviceValueAdapter.setDataList(valueList)
        deviceValueAdapter.setOnItemClickListener(systemItemClickListener)
        valueRecyclerView.adapter = deviceValueAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        valueRecyclerView.layoutManager = LinearLayoutManager(activity, layoutManager, false)
        valueRecyclerView.itemAnimator = DefaultItemAnimator()
        valueRecyclerView.addItemDecoration(SpaceItemDecoration(2))

        freshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(activity!!, true))
        freshLayout.setDelegate(mDelegate)

        freshLayout.beginRefreshing()
    }

    private fun getValueList() {
        valueList.clear()
        when (battery.catalogId) {
            1 -> insertBSFields()
            2 -> insertUPS1Fields()
            3 -> insertUPS2Fields()
            4 -> insertUPS3Fields()
            5 -> insertDC1Fields()
            6 -> insertDC2Fields()
        }
        deviceValueAdapter.setDataList(valueList)
        deviceValueAdapter.notifyDataSetChanged()
        if (type == 0) {
            freshLayout.endRefreshing()
        } else {
            freshLayout.endLoadingMore()
        }
    }

    /**
     * 监听 刷新或者上拉
     */
    private val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            type = 0
            getValueList()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            type = 1
            getValueList()
            return true
        }
    }

    private val systemItemClickListener = object : RecyclerBaseAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            val item = valueList[position]
            val values = item.split(",")
            val label = values[0]
            val key = values[3]
            SystemValueActivity.start(activity!!, battery, String.format("%s(%s)",
                    key.toUpperCase(), label), key)
        }
    }

    private fun insertBSFields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
    }

    private fun insertDC1Fields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
        valueList.add("R相市电电压,${battery.mepur},V,mepur")
        valueList.add("S相市电电压,${battery.mepus},V,mepus")
        valueList.add("T相市电电压,${battery.meput},V,meput")
        valueList.add("市电频率,${battery.mepf},Hz,mepf")
        valueList.add("电池充电电流,${battery.bcc},A,bcc")
        valueList.add("电池放电电流,${battery.bdc},A,bdc")
        valueList.add("正母线电压,${battery.mp},V,mp")
        valueList.add("负母线电压,${battery.mn},V,mn")
        valueList.add("合母电压,${battery.hmv},V,hmv")
        valueList.add("控母电压,${battery.kmv},V,kmv")
        valueList.add("输出电流,${battery.oi},A,oi")
    }

    private fun insertDC2Fields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
        valueList.add("R相市电电压,${battery.mepur},V,mepur")
        valueList.add("市电频率,${battery.mepf},Hz,mepf")
        valueList.add("电池充电电流,${battery.bcc},A,bcc")
        valueList.add("电池放电电流,${battery.bdc},A,bdc")
        valueList.add("正母线电压,${battery.mp},V,mp")
        valueList.add("负母线电压,${battery.mn},V,mn")
        valueList.add("合母电压,${battery.hmv},V,hmv")
        valueList.add("控母电压,${battery.kmv},V,kmv")
        valueList.add("输出电流,${battery.oi},A,oi")
    }

    private fun insertUPS1Fields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
        valueList.add("R相市电电压,${battery.mepur},V,mepur")
        valueList.add("S相市电电压,${battery.mepus},V,mepus")
        valueList.add("T相市电电压,${battery.meput},V,meput")
        valueList.add("市电频率,${battery.mepf},Hz,mepf")
        valueList.add("R相旁路电压,${battery.bepur},V,bepur")
        valueList.add("S相旁路电压,${battery.bepus},V,pepus")
        valueList.add("T相旁路电压,${battery.beput},V,beput")
        valueList.add("旁路频率,${battery.bepf},Hz,bepf")
        valueList.add("R相逆变电压,${battery.iepur},V,iepur")
        valueList.add("S相逆变电压,${battery.iepus},V,iepus")
        valueList.add("T相逆变电压,${battery.ieput},V,ieput")
        valueList.add("逆变频率,${battery.iepf},Hz,iepf")
        valueList.add("R相输出电压,${battery.oepur},V,oepur")
        valueList.add("S相输出电压,${battery.oepus},V,oepus")
        valueList.add("T相输出电压,${battery.oeput},V,oeput")
        valueList.add("输出频率,${battery.oepf},Hz,oepf")
        valueList.add("R相输出电流,${battery.ocr},A,ocr")
        valueList.add("S相输出电流,${battery.ocs},A,ocs")
        valueList.add("T相输出电流,${battery.oct},A,oct")
        valueList.add("R相输出功率,${battery.opr},KVA,opr")
        valueList.add("S相输出功率,${battery.ops},KVA,ops")
        valueList.add("T相输出功率,${battery.opt},KVA,opt")
        valueList.add("输出总功率,${battery.otp},KVA,otp")
        valueList.add("RS输出功率因素,${battery.opfrs},,opfrs")
        valueList.add("ST输出功率因素,${battery.opfst},,opfst")
        valueList.add("TR输出功率因素,${battery.opftr},,opftr")
        valueList.add("母线电压,${battery.bbv},V,bbv")
        valueList.add("电池充电电流,${battery.bcc},A,bcc")
        valueList.add("电池放电电流,${battery.bdc},A,bdc")
    }

    private fun insertUPS2Fields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
        valueList.add("R相市电电压,${battery.mepur},V,mepur")
        valueList.add("S相市电电压,${battery.mepus},V,mepus")
        valueList.add("T相市电电压,${battery.meput},V,meput")
        valueList.add("市电频率,${battery.mepf},Hz,mepf")
        valueList.add("R相旁路电压,${battery.bepur},V,bepur")
        valueList.add("S相旁路电压,${battery.bepus},V,bepus")
        valueList.add("T相旁路电压,${battery.beput},V,beput")
        valueList.add("旁路频率,${battery.bepf},Hz,bepf")
        valueList.add("R相逆变电压,${battery.iepur},V,iepur")
        valueList.add("S相逆变电压,${battery.iepus},V,iepus")
        valueList.add("T相逆变电压,${battery.ieput},V,ieput")
        valueList.add("逆变频率,${battery.iepf},Hz,iepf")
        valueList.add("输出电压,${battery.oepur},V,oepur")
        valueList.add("输出频率,${battery.oepf},Hz,oepf")
        valueList.add("输出电流,${battery.ocr},A,ocr")
        valueList.add("输出功率,${battery.opr},KVA,opr")
        valueList.add("输出功率因素,${battery.opfrs},,opfrs")
        valueList.add("母线电压,${battery.bbv},V,bbv")
        valueList.add("电池充电电流,${battery.bcc},A,bcc")
        valueList.add("电池放电电流,${battery.bdc},A,bdc")
    }

    private fun insertUPS3Fields() {
        valueList.add("电池电流,${battery.bi},A,bi")
        valueList.add("电池组电压,${battery.btv},V,btv")
        valueList.add("电池压差,${battery.dp},V,dp")
        valueList.add("单体最高电压,${battery.chv},V,chv")
        valueList.add("单体最低电压,${battery.clv},V,clv")
        valueList.add("单体最高温度,${battery.cht},℃,cht")
        valueList.add("单体最高内阻,${battery.chr},mR,chr")
        valueList.add("市电电压,${battery.mepur},V,mepur")
        valueList.add("市电频率,${battery.mepf},Hz,mepf")
        valueList.add("旁路电压,${battery.bepur},V,bepur")
        valueList.add("旁路频率,${battery.bepf},Hz,bepf")
        valueList.add("逆变电压,${battery.iepur},V,iepur")
        valueList.add("逆变频率,${battery.iepf},Hz,iepf")
        valueList.add("输出电压,${battery.oepur},V,oepur")
        valueList.add("输出频率,${battery.oepf},Hz,oepf")
        valueList.add("输出电流,${battery.ocr},A,ocr")
        valueList.add("输出功率,${battery.opr},KVA,opr")
        valueList.add("输出功率因素,${battery.opfrs},,opfrs")
        valueList.add("母线电压,${battery.bbv},V,bbv")
        valueList.add("电池充电电流,${battery.bcc},A,bcc")
        valueList.add("电池放电电流,${battery.bdc},A,bdc")
    }

}