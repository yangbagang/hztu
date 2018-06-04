package com.ybg.app.hztu.adapter

import android.app.Activity
import android.app.Service
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import com.ybg.app.hztu.pop.SystemEditPopupWindow

/**
 * Created by ybg on 18-3-6.
 * 应用于MainActivity
 */
class SystemItemAdapter(private var mContext: Activity) : BaseAdapter() {

    private val userApplication = UserApplication.instance!!

    private var inflater: LayoutInflater? = null

    var mList: List<Battery>? = null

    fun setDataList(list: List<Battery>) {
        mList = list
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext)
            convertView = inflater!!.inflate(R.layout.item_system, parent, false)
            viewHolder = ViewHolder()
            initViewHolder(viewHolder, convertView)
        } else {
            viewHolder = convertView.tag as ViewHolder?
        }

        val battery = getItem(position)
        val uid = battery.uid
        val name = battery.name

        viewHolder?.systemValueView?.text = getDeviceValueText(battery)

        viewHolder?.systemNameView?.text = "$uid($name)"
        viewHolder?.systemNameView?.setOnLongClickListener {
            val popupWindow = SystemEditPopupWindow(mContext, battery.uid, name, object : SystemEditPopupWindow
            .SystemEditListener {
                override fun updateName(systemName: String) {
                    viewHolder.systemNameView?.text = systemName
                    battery.name = systemName
                    val lp = mContext.window.attributes
                    lp.alpha = 1f
                    mContext.window.attributes = lp
                }

                override fun cancelEdit() {
                    val lp = mContext.window.attributes
                    lp.alpha = 1f
                    mContext.window.attributes = lp
                }
            })
            val lp = mContext.window.attributes
            lp.alpha = 0.4f
            mContext.window.attributes = lp
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            popupWindow.showWindow(parent)
            (mContext.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            true
        }
        viewHolder?.systemUpdateTimeView?.text = DateUtil.getTimeInterval(battery.createTime)

        getSystemAddress(viewHolder, battery.lac, battery.cid, 0)

        return convertView!!
    }

    private fun getDeviceValueText(battery: Battery): String {
        var text = ""
        when (battery.catalogId) {
            0 -> text = "电池组电压：${battery.btv} 电流：${battery.bi} 新告警：0"
            1, 2, 3 -> text = "电池组电压：${battery.btv} 电流：${battery.bi} 新告警：0"
            4, 5 -> text = "控母电压：${battery.kmv} 合母电压：${battery.hmv} 电池组电压：${battery.btv} 电流：${battery.bi} " +
                    "新告警：0"
        }
        return text
    }

    override fun getItem(position: Int): Battery = mList!![position]

    override fun getItemId(position: Int): Long = mList!![position].id

    override fun getCount(): Int = mList!!.size

    private fun initViewHolder(viewHolder: ViewHolder, convertView: View?) {
        viewHolder.systemImageView = convertView?.findViewById(R.id.iv_system)
        viewHolder.systemNameView = convertView?.findViewById(R.id.tv_system_name)
        viewHolder.systemUpdateTimeView = convertView?.findViewById(R.id.tv_update_time)
        viewHolder.systemValueView = convertView?.findViewById(R.id.tv_system_value)
        viewHolder.systemAddressView = convertView?.findViewById(R.id.tv_system_address)

        convertView?.tag = viewHolder
    }

    inner class ViewHolder {
        internal var systemImageView: ImageView? = null
        internal var systemNameView: TextView? = null
        internal var systemUpdateTimeView: TextView? = null
        internal var systemValueView: TextView? = null
        internal var systemAddressView: TextView? = null
    }

    private fun getSystemAddress(viewHolder: ViewHolder?, lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(mContext, userApplication.token, lac, cid, type, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                val address = data.split(";")[0]
                viewHolder?.systemAddressView?.text = address
            }
        })
    }

}