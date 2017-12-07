package com.ybg.app.hztu

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.ybg.app.base.bean.UserInfo
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.hztu.activity.home.MainActivity
import com.ybg.app.hztu.activity.user.LoginActivity
import com.ybg.app.hztu.app.UserApplication

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //全屏声明
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        super.onCreate(savedInstanceState)
        val userApplication = UserApplication.instance
        if (userApplication != null && userApplication.hasLogin()) {
            MainActivity.start(this)
            SendRequest.getUserInfo(this@WelcomeActivity, userApplication.token, object : JsonCallback(){
                override fun onJsonSuccess(data: String) {
                    super.onJsonSuccess(data)
                    val userInfo = GsonUtil.createGson().fromJson<UserInfo>(data, UserInfo::class.java)
                    userApplication.userInfo = userInfo
                }
            })
        } else {
            LoginActivity.start(this)
        }
        finish()
    }
}
