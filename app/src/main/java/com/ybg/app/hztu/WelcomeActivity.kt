package com.ybg.app.hztu

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.ybg.app.hztu.activity.home.MainActivity
import com.ybg.app.hztu.activity.user.LoginActivity
import com.ybg.app.hztu.app.UserApplication

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //全屏声明
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        super.onCreate(savedInstanceState)
        if (UserApplication.instance != null && UserApplication.instance!!.hasLogin()) {
            MainActivity.start(this)
        } else {
            LoginActivity.start(this)
        }
        finish()
    }
}
