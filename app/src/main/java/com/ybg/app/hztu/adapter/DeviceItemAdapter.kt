package com.ybg.app.hztu.adapter

import android.content.Context
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.hztu.R

/**
 * Created by ybg on 17-11-30.
 */
class DeviceItemAdapter(private var context: Context) : RecyclerBaseAdapter<Battery>(context) {

    private var num: TextView? = null
    private var bi: TextView? = null
    private var btv: TextView? = null
    private var time: TextView? = null

    override val rootResource: Int
        get() = R.layout.system_list_content

    override fun getView(viewHolder: BaseViewHolder, item: Battery?, position: Int) {
        num = viewHolder.getView(R.id.tv_num)
        bi = viewHolder.getView(R.id.tv_bi)
        btv = viewHolder.getView(R.id.tv_btv)
        time = viewHolder.getView(R.id.tv_time)
        if (num != null) {
            num!!.text = "${position}"
        }
        if (bi != null) {
            bi!!.text = "${item?.bi}"
        }
        if (btv != null) {
            btv!!.text = "${item?.btv}"
        }
        if (time != null) {
            time!!.text = DateUtil.getTimeInterval("${item?.createTime}")
        }
        viewHolder.setIsRecyclable(false)
    }

}