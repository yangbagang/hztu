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
import kotlinx.android.synthetic.main.activity_register.*

/**
 * A register screen that offers register via mobile/password/name/company/email.
 */
class RegisterActivity : AppCompatActivity() {

    private val userApplication = UserApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        mobile_register_in_button.setOnClickListener { attemptLogin() }
        tv_action_login.setOnClickListener {
            LoginActivity.start(this@RegisterActivity)
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
        user_name.error = null
        company.error = null
        email.error = null

        // Store values at the time of the login attempt.
        val mobileStr = mobile.text.toString()
        val passwordStr = password.text.toString()
        val userNameStr = user_name.text.toString()
        val companyStr = company.text.toString()
        val emailStr = email.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid mobile, if the user entered one.
        if (!TextUtils.isEmpty(mobileStr) && !isMobileValid(mobileStr)) {
            mobile.error = getString(R.string.error_invalid_mobile)
            focusView = mobile
            cancel = true
        }

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

        // Check for a valid name.
        if (TextUtils.isEmpty(userNameStr)) {
            user_name.error = getString(R.string.error_field_required)
            focusView = user_name
            cancel = true
        }

        // Check for a valid company.
        if (TextUtils.isEmpty(companyStr)) {
            company.error = getString(R.string.error_field_required)
            focusView = company
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
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
            register(mobileStr, passwordStr, userNameStr, companyStr, emailStr)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isMobileValid(mobile: String): Boolean = mobile.length == 11

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

            register_form.visibility = if (show) View.GONE else View.VISIBLE
            register_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            register_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            register_progress.visibility = if (show) View.VISIBLE else View.GONE
            register_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    fun register(mobile: String, password: String, name: String, company: String, email: String) {
        SendRequest.userRegister(this, mobile, password, name, company, email, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                super.onJsonSuccess(data)
                val userInfo = GsonUtil.createGson().fromJson<UserInfo>(data, UserInfo::class.java)
                userApplication.token = userInfo.token
                userApplication.userInfo = userInfo
                showProgress(false)
                MainActivity.start(this@RegisterActivity)
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
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }
}
