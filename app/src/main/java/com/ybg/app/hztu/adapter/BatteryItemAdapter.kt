package com.ybg.app.hztu.adapter

import android.content.Context
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.activity.battery.BatteryDetailActivity
import com.ybg.app.hztu.activity.battery.BatteryLineChartActivity

/**
 * Created by ybg on 17-11-30.
 */
class BatteryItemAdapter(private var context: Context) : RecyclerBaseAdapter<Battery>(context) {

    private var num: TextView? = null
    private var bv: TextView? = null
    private var bt: TextView? = null
    private var br: TextView? = null
    private var bi: TextView? = null
    private var time: TextView? = null

    override val rootResource: Int
        get() = R.layout.battery_list_content

    override fun getView(viewHolder: BaseViewHolder, item: Battery?, position: Int) {
        num = viewHolder.getView(R.id.tv_num)
        bv = viewHolder.getView(R.id.tv_bv)
        bt = viewHolder.getView(R.id.tv_bt)
        br = viewHolder.getView(R.id.tv_br)
        bi = viewHolder.getView(R.id.tv_bi)
        time = viewHolder.getView(R.id.tv_time)
        if (num != null) {
            num!!.text = "${item?.num}"
            num!!.setOnClickListener {
                showDetail(item)
            }
        }
        if (bv != null) {
            bv!!.text = "${item?.bv}"
            bv!!.setOnClickListener {
                showLineChart(item)
            }
        }
        if (bt != null) {
            bt!!.text = "${item?.bt}"
            bt!!.setOnClickListener {
                showLineChart(item)
            }
        }
        if (br != null) {
            br!!.text = "${item?.br}"
            br!!.setOnClickListener {
                showLineChart(item)
            }
        }
        if (bi != null) {
            bi!!.text = "${item?.bi}"
            bi!!.setOnClickListener {
                showLineChart(item)
            }
        }
        if (time != null) {
            time!!.text = DateUtil.getTimeInterval("${item?.createTime}")
        }
        viewHolder.setIsRecyclable(false)
    }

    private fun showDetail(item: Battery?) {
        if (item != null) {
            BatteryDetailActivity.start(mContext, item!!)
        }
    }

    private fun showLineChart(item: Battery?) {
        if (item != null) {
            BatteryLineChartActivity.start(mContext, item!!)
        }
    }
}