package com.ybg.app.hztu.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.DateUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication

/**
 * Created by ybg on 18-3-6.
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
        //TODO 根据UID不同显示不同图标
        if (uid.startsWith("WLCB")) {

        } else if (uid.startsWith("WLCD")) {

        } else if (uid.startsWith("WLCU")) {

        }
        viewHolder?.systemValueView?.text = "BI: ${battery.bi}, BTV: ${battery.btv}"
        val name = battery.name
        if (name == "") {
            viewHolder?.systemNameView?.text = uid
            viewHolder?.nameEditText?.setText(uid)
        } else {
            viewHolder?.systemNameView?.text = name
            viewHolder?.nameEditText?.setText(name)
        }
        viewHolder?.systemNameView?.setOnClickListener {
            viewHolder.systemNameView?.visibility = View.GONE
            viewHolder.nameEditText?.visibility = View.VISIBLE
            viewHolder.nameUpdateBtn?.visibility = View.VISIBLE
            viewHolder.nameCancelBtn?.visibility = View.VISIBLE
        }
        viewHolder?.nameUpdateBtn?.setOnClickListener {
            val nameValue = viewHolder.nameEditText?.text?.toString()
            if (nameValue == null || nameValue == "") {
                ToastUtil.show(userApplication, "自定义名称不能为空。")
            } else {
                updateSystemName(viewHolder, uid, nameValue, battery)
            }
        }
        viewHolder?.nameCancelBtn?.setOnClickListener {
            viewHolder.systemNameView?.visibility = View.VISIBLE
            viewHolder.nameEditText?.visibility = View.GONE
            viewHolder.nameUpdateBtn?.visibility = View.GONE
            viewHolder.nameCancelBtn?.visibility = View.GONE
        }
        viewHolder?.systemUpdateTimeView?.text = DateUtil.getTimeInterval(battery.createTime)

        getSystemAddress(viewHolder, battery.lac, battery.cid, 0)

        return convertView!!
    }

    override fun getItem(position: Int): Battery = mList!![position]

    override fun getItemId(position: Int): Long = mList!![position].id

    override fun getCount(): Int = mList!!.size

    private fun initViewHolder(viewHolder: ViewHolder, convertView: View?) {
        viewHolder.systemImageView = convertView?.findViewById<ImageView>(R.id.iv_system)
        viewHolder.systemNameView = convertView?.findViewById(R.id.tv_system_name)
        viewHolder.systemUpdateTimeView = convertView?.findViewById(R.id.tv_update_time)
        viewHolder.systemValueView = convertView?.findViewById(R.id.tv_system_value)
        viewHolder.nameEditText = convertView?.findViewById(R.id.et_system_name)
        viewHolder.nameUpdateBtn = convertView?.findViewById(R.id.btn_update)
        viewHolder.nameCancelBtn = convertView?.findViewById(R.id.btn_cancel)
        viewHolder.systemAddressView = convertView?.findViewById(R.id.tv_system_address)

        convertView?.tag = viewHolder
    }

    inner class ViewHolder {
        internal var systemImageView: ImageView? = null
        internal var systemNameView: TextView? = null
        internal var systemUpdateTimeView: TextView? = null
        internal var systemValueView: TextView? = null
        internal var nameEditText: EditText? = null
        internal var nameUpdateBtn: Button? = null
        internal var nameCancelBtn: Button? = null
        internal var systemAddressView: TextView? = null
    }

    private fun updateSystemName(viewHolder: ViewHolder, uid: String, name: String, battery: Battery) {
        SendRequest.updateName(mContext, userApplication.token, uid, name, object : JsonCallback(){
            override fun onSuccess(code: Int, response: String) {
                ToastUtil.show(userApplication, "名称己经更新。")
                battery.name = name
                viewHolder.systemNameView?.text = name
                viewHolder.systemNameView?.visibility = View.VISIBLE
                viewHolder.nameEditText?.visibility = View.GONE
                viewHolder.nameUpdateBtn?.visibility = View.GONE
                viewHolder.nameCancelBtn?.visibility = View.GONE
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                ToastUtil.show(userApplication, jsonBean.message)
            }
        })
    }

    private fun getSystemAddress(viewHolder: ViewHolder?, lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(mContext, userApplication.token, lac, cid, type, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                val address = data.split(";")[0]
                viewHolder?.systemAddressView?.text = address
            }
        })
    }

}