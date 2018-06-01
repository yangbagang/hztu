package com.ybg.app.hztu.activity.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        token_update_button.setOnClickListener { attemptLogin() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        if (userApplication.hasLogin()) {
            tv_user_code.text = "客户编号：${userApplication.userInfo?.code}"
            tv_user_company.text = "公司名称：${userApplication.userInfo?.company}"
            tv_user_email.text = "公司邮箱：${userApplication.userInfo?.email}"
            tv_user_mobile.text = "手机号码：${userApplication.userInfo?.mobile}"
            tv_user_num.text = "站点数量：${userApplication.userInfo?.sideNum}"
            tv_user_capacity.text = "装机容量：${userApplication.userInfo?.installedCapacity}kWh"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        et_old_pwd.error = null
        et_new_pwd.error = null

        // Store values at the time of the login attempt.
        val oldPwdStr = et_old_pwd.text.toString()
        val newPwdStr = et_new_pwd.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid name.
        if (TextUtils.isEmpty(oldPwdStr)) {
            et_old_pwd.error = getString(R.string.error_field_required)
            focusView = et_old_pwd
            cancel = true
        }

        if (TextUtils.isEmpty(newPwdStr)) {
            et_new_pwd.error = getString(R.string.error_field_required)
            focusView = et_new_pwd
            cancel = true
        }

        if (newPwdStr.length < 6) {
            et_new_pwd.error = getString(R.string.error_invalid_password)
            focusView = et_new_pwd
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            update(oldPwdStr, newPwdStr)
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            token_update_form.visibility = if (show) View.GONE else View.VISIBLE
            token_update_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            token_update_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            update_progress.visibility = if (show) View.VISIBLE else View.GONE
            update_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            update_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            update_progress.visibility = if (show) View.VISIBLE else View.GONE
            token_update_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun update(oldPwd: String, newPwd: String) {
        SendRequest.updateUserPassword(this, userApplication.token, oldPwd, newPwd, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                showProgress(false)
                ToastUtil.show(userApplication, "操作完成")
                finish()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                super.onJsonFail(jsonBean)
                println("errorMsg: ${jsonBean.message}")
                ToastUtil.show(userApplication, "操作失败")
                showProgress(false)
            }
        })
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, UpdateActivity::class.java)
            context.startActivity(starter)
        }
    }
}
