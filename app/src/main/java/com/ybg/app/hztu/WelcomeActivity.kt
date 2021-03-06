package com.ybg.app.hztu

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.ybg.app.base.bean.UserInfo
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.hztu.activity.home.MainActivity
import com.ybg.app.hztu.activity.user.LoginActivity
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : Activity() {

    private var time = 3//倒计时

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏声明
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option

        setContentView(R.layout.activity_welcome)

        tv_second_num?.setOnClickListener {
            enterMainActivity()
        }

        mHandler?.postDelayed(runnable, 1000)
    }

    private fun enterMainActivity() {
        val userApplication = UserApplication.instance
        if (userApplication != null && userApplication.hasLogin()) {
            MainActivity.start(this)
            SendRequest.getUserInfo(this@WelcomeActivity, userApplication.token, object : JsonCallback() {
                override fun onJsonSuccess(data: String) {
                    val userInfo = GsonUtil.createGson().fromJson<UserInfo>(data, UserInfo::class.java)
                    userApplication.userInfo = userInfo
                }
            })
        } else {
            LoginActivity.start(this)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mHandler != null) {
            mHandler!!.removeCallbacks(null)
            mHandler = null
        }
        time = 0
    }

    private val runnable = object : Runnable {
        override fun run() {
            time--
            if (time == 0) {
                enterMainActivity()
            } else {
                if (mHandler != null) {
                    mHandler!!.sendEmptyMessage(158)
                }
            }
            if (mHandler != null) {
                mHandler!!.postDelayed(this, 1000)
            }
        }
    }

    private var mHandler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                158 -> tv_second_num?.text = String.format("跳过%dS", time)
            }
            super.handleMessage(msg)
        }
    }

}
