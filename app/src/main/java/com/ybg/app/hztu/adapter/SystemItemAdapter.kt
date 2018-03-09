package com.ybg.app.hztu.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
class SystemItemAdapter(private var context: Context) : RecyclerBaseAdapter<Battery>(context) {

    private val userApplication = UserApplication.instance!!

    private var systemImageView: ImageView? = null
    private var systemNameView: TextView? = null
    private var systemUpdateTimeView: TextView? = null
    private var systemValueView: TextView? = null
    private var nameEditText: EditText? = null
    private var nameUpdateBtn: Button? = null
    private var nameCancelBtn: Button? = null
    private var systemAddressView: TextView? = null

    override val rootResource: Int
        get() = R.layout.item_system

    override fun getView(viewHolder: BaseViewHolder, item: Battery?, position: Int) {
        systemImageView = viewHolder.getView(R.id.iv_system)
        systemNameView = viewHolder.getView(R.id.tv_system_name)
        systemUpdateTimeView = viewHolder.getView(R.id.tv_update_time)
        systemValueView = viewHolder.getView(R.id.tv_system_value)
        nameEditText = viewHolder.getView(R.id.et_system_name)
        nameUpdateBtn = viewHolder.getView(R.id.btn_update)
        nameCancelBtn = viewHolder.getView(R.id.btn_cancel)
        systemAddressView = viewHolder.getView(R.id.tv_system_address)

        if (item != null) {
            val uid = item.uid
            //TODO 根据UID不同显示不同图标
            if (uid.startsWith("WLCB")) {

            } else if (uid.startsWith("WLCD")) {

            } else if (uid.startsWith("WLCU")) {

            }
            systemValueView?.text = "BI: ${item.bi}, BTV: ${item.btv}"
            val name = item.name
            if (name == "") {
                systemNameView?.text = uid
                nameEditText?.setText(uid)
            } else {
                systemNameView?.text = name
                nameEditText?.setText(name)
            }
            systemNameView?.setOnClickListener {
                systemNameView?.visibility = View.GONE
                nameEditText?.visibility = View.VISIBLE
                nameUpdateBtn?.visibility = View.VISIBLE
                nameCancelBtn?.visibility = View.VISIBLE
            }
            nameUpdateBtn?.setOnClickListener {
                val nameValue = nameEditText?.text?.toString()
                if (nameValue == null || nameValue == "") {
                    ToastUtil.show(userApplication, "自定义名称不能为空。")
                } else {
                    updateSystemName(uid, nameValue)
                }
            }
            nameCancelBtn?.setOnClickListener {
                systemNameView?.visibility = View.VISIBLE
                nameEditText?.visibility = View.GONE
                nameUpdateBtn?.visibility = View.GONE
                nameCancelBtn?.visibility = View.GONE
            }
            systemUpdateTimeView?.text = DateUtil.getTimeInterval(item.createTime)
            //TODO 取地址有时不显示。
            getSystemAddress(item.lac, item.cid, 0)
            viewHolder.setIsRecyclable(false)
        }
    }

    private fun updateSystemName(uid: String, name: String) {
        SendRequest.updateName(context, userApplication.token, uid, name, object : JsonCallback(){
            override fun onSuccess(code: Int, response: String) {
                ToastUtil.show(userApplication, "名称己经更新。")
                systemNameView?.text = name
                systemNameView?.visibility = View.VISIBLE
                nameEditText?.visibility = View.GONE
                nameUpdateBtn?.visibility = View.GONE
                nameCancelBtn?.visibility = View.GONE
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                ToastUtil.show(userApplication, jsonBean.message)
            }
        })
    }

    private fun getSystemAddress(lac: Int, cid: Int, type: Int) {
        SendRequest.getLocation(context, userApplication.token, lac, cid, type, object : JsonCallback(){
            override fun onJsonSuccess(data: String) {
                val address = data.split(";")[0]
                println(address)
                systemAddressView?.text = address
            }
        })
    }

}