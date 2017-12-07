package com.ybg.app.base.http

import android.content.Context
import android.util.Pair
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.listener.UploadListener
import java.io.File

object SendRequest {


    //第一部分，用户操作
    /**
     * 1.1获取验证码
     *
     * @param mobile 手机号
     */
    fun getCaptcha(tag: Context, mobile: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile)
        OkHttpProxy.post(HttpUrl.getCaptchaUrl, tag, params, callback)
    }

    /**
     * 1.2验证码校验
     *
     * @param mobile 手机号
     * @param captcha  验证码的值
     */
    fun checkCaptcha(tag: Context, mobile: String, captcha: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "captcha" to captcha)
        OkHttpProxy.post(HttpUrl.checkCaptchaUrl, tag, params, callback)
    }

    /**
     * 1.3用户注册接口
     *
     * @param mobile 手机号
     * @param password  密码
     * @param name
     * @param company
     * @param email
     */
    fun userRegister(tag: Context, mobile: String, password: String, name: String, company: String,
                     email: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "password" to password, "name" to name,
                "company" to company, "email" to email)
        OkHttpProxy.post(HttpUrl.userRegisterUrl, tag, params, callback)
    }

    /**
     * 1.4 用户登录接口
     *
     * @param mobile 手机号
     * @param password 密码
     */
    fun userLogin(tag: Context, mobile: String, password: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("mobile" to mobile, "password" to password)
        OkHttpProxy.post(HttpUrl.userLoginUrl, tag, params, callback)
    }

    /**
     * 1.5 用户登出接口
     *
     * @param mobile 手机号
     * @param password 密码
     */
    fun userLogout(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.userLogoutUrl, tag, params, callback)
    }

    /**
     * 1.6 更新用户信息接口
     *
     * @param mobile 手机号
     * @param name
     * @param company
     * @param email
     */
    fun updateUserInfo(tag: Context, token: String, name: String, company: String, email: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "name" to name, "company" to company, "email" to email)
        OkHttpProxy.post(HttpUrl.updateUserInfoUrl, tag, params, callback)
    }

    fun updateAppToken(tag: Context, userToken: String, appToken: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("userToken" to userToken, "appToken" to appToken)
        OkHttpProxy.post(HttpUrl.updateClientIdUrl, tag, params, callback)
    }

    fun getUserInfo(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.getUserInfoUrl, tag, params, callback)
    }

    //第二部分，电池操作
    /**
     * 获取电池列表
     */
    fun getBatteryList(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.batteryListUrl, tag, params, callback)
    }

    /**
     * 获得历史数据
     */
    fun getBatteryDataList(tag: Context, token: String, batteryId: Long, pageSize: Int, pageNum: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "batteryId" to "$batteryId", "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.batteryDataUrl, tag, params, callback)
    }

    fun getBatterySumList(tag: Context, token: String, batteryId: Long, key: String, period: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "batteryId" to "$batteryId", "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.batteryChartUrl, tag, params, callback)
    }


    /**
     * 上传图片文件
     */
    fun uploadPicFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_PIC_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 上传视频文件
     */
    fun uploadVideoFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_VIDEO_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
