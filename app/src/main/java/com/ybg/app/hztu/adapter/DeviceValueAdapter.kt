package com.ybg.app.hztu.adapter

import android.content.Context
import android.widget.TextView
import com.ybg.app.hztu.R

class DeviceValueAdapter(private var context: Context) : RecyclerBaseAdapter<String>(context) {

    private var itemLabel: TextView? = null
    private var itemValue: TextView? = null
    private var itemUnit: TextView? = null

    override val rootResource: Int
        get() = R.layout.device_value_content

    override fun getView(viewHolder: BaseViewHolder, item: String?, position: Int) {
        if (item != null) {
            itemLabel = viewHolder.getView(R.id.tv_item_label)
            itemValue = viewHolder.getView(R.id.tv_item_value)
            itemUnit = viewHolder.getView(R.id.tv_item_unit)
            val values = item.split(",")
            itemLabel?.text = values[0]
            itemValue?.text = values[1]
            itemUnit?.text = values[2]
        }
        viewHolder.setIsRecyclable(false)
    }

}