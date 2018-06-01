package com.ybg.app.hztu.adapter

import android.content.Context
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.LineChartItem
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.hztu.R

/**
 * Created by ybg on 17-11-30.
 */
class DeviceItemAdapter(private var context: Context) : RecyclerBaseAdapter<LineChartItem>(context) {

    private var num: TextView? = null
    private var value: TextView? = null
    private var time: TextView? = null

    override val rootResource: Int
        get() = R.layout.system_list_content

    override fun getView(viewHolder: BaseViewHolder, item: LineChartItem?, position: Int) {
        num = viewHolder.getView(R.id.tv_num)
        value = viewHolder.getView(R.id.tv_value)
        time = viewHolder.getView(R.id.tv_time)
        if (num != null) {
            num!!.text = "${position + 1}"
        }
        if (value != null) {
            value!!.text = "${item?.yValue}"
        }
        if (time != null) {
            time!!.text = DateUtil.getTimeInterval("${item?.xValue}")
        }
        viewHolder.setIsRecyclable(false)
    }

}