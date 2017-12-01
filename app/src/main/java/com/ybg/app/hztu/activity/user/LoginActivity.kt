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
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.UserInfo
import com.ybg.app.base.http.SendRequest
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.GsonUtil
import com.ybg.app.base.utils.ToastUtil
import com.ybg.app.hztu.R
import com.ybg.app.hztu.activity.home.MainActivity
import com.ybg.app.hztu.app.UserApplication
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via mobile/password.
 */
class LoginActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        mobile_sign_in_button.setOnClickListener { attemptLogin() }
        tv_action_register.setOnClickListener {
            RegisterActivity.start(this@LoginActivity)
            finish()
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        mobile.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val mobileStr = mobile.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mobileStr)) {
            mobile.error = getString(R.string.error_field_required)
            focusView = mobile
            cancel = true
        } else if (!isMobileValid(mobileStr)) {
            mobile.error = getString(R.string.error_invalid_mobile)
            focusView = mobile
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
            login(mobileStr, passwordStr)
        }
    }

    private fun isMobileValid(mobile: String): Boolean = mobile.length == 11

    private fun isPasswordValid(password: String): Boolean = password.length > 5

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

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun login(mobile: String, password: String) {
        SendRequest.userLogin(this, mobile, password, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val userInfo = GsonUtil.createGson().fromJson<UserInfo>(data, UserInfo::class.java)
                userApplication.token = userInfo.token
                userApplication.userInfo = userInfo
                showProgress(false)
                MainActivity.start(this@LoginActivity)
                finish()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                super.onJsonFail(jsonBean)
                println("errorMsg: ${jsonBean.message}")
                ToastUtil.show(userApplication, "登录失败")
                showProgress(false)
            }
        })
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }
}
