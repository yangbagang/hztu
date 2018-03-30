package com.ybg.app.hztu.pop

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import com.ybg.app.base.bean.Battery
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication

/**
 * Created by yangbagang on 2018/3/30.
 */
class SystemEditPopupWindow() : PopupWindow(), View.OnClickListener {

    private val userApplication = UserApplication.instance!!

    private var mContext: Context? = null
    private var mSystemEditListener: SystemEditListener? = null
    private var uid: String = ""
    private var name: String = ""
    private var btnCancel: Button? = null
    private var btnUpdate: Button? = null
    private var editSystemName: EditText? = null

    constructor(context: Context, uid: String, name: String, systemEditListener: SystemEditListener) : this() {
        this.mContext = context
        this.uid = uid
        this.name = name
        this.mSystemEditListener = systemEditListener

        contentView = View.inflate(mContext, R.layout.system_pop_win, null)
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        btnCancel = contentView.findViewById(R.id.btn_cancel)
        btnUpdate = contentView.findViewById(R.id.btn_update)
        editSystemName = contentView.findViewById(R.id.et_system_name)

        editSystemName?.setText(name)
    }

    private fun updateSystemName(uid: String, name: String) {
        if (mContext == null) return
        SendRequest.updateName(mContext!!, userApplication.token, uid, name, object : JsonCallback(){
            override fun onSuccess(code: Int, response: String) {
                ToastUtil.show(userApplication, "名称己经更新。")
                mSystemEditListener?.updateName(name)
                dismiss()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                ToastUtil.show(userApplication, jsonBean.message)
            }
        })
    }

    fun showWindow(parent: View) {
        try {
            if (!this.isShowing) {
                this.showAtLocation(parent, android.view.Gravity.CENTER, 0, 0)
            } else {
                this.dismiss()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.btn_cancel -> {
                dismiss()
            }
            R.id.btn_update -> {
                val systemName = editSystemName?.text?.toString()
                if (systemName != null && systemName != "") {
                    if (systemName == name) {
                        dismiss()
                    } else {
                        name = systemName
                        updateSystemName(uid, name)
                    }
                } else {
                    ToastUtil.show(userApplication, "名称不能为空。")
                }
            }
        }
    }

    interface SystemEditListener {
        fun updateName(systemName: String)
    }
}