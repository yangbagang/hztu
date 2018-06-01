package com.ybg.app.base.http

import android.content.Context
import android.util.Pair
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.listener.UploadListener
import java.io.File

object SendRequest {


    //第一部分，用户操作

    /**
     * 1.1 用户登录接口
     *
     * @param mobile 手机号
     * @param password 密码
     */
    fun userLogin(tag: Context, mobile: String, password: String, callback: OkCallback<*>) {
        val params = mapOf("mobile" to mobile, "password" to password)
        OkHttpProxy.post(HttpUrl.userLoginUrl, tag, params, callback)
    }

    /**
     * 1.2 用户登出接口
     *
     * @param token 用户token
     */
    fun userLogout(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token)
        OkHttpProxy.post(HttpUrl.userLogoutUrl, tag, params, callback)
    }

    /**
     * 1.3 修改密码接口
     *
     * @param token
     * @param oldPwd
     * @param newPwd
     */
    fun updateUserPassword(tag: Context, token: String, oldPwd: String, newPwd: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "oldPwd" to oldPwd, "newPwd" to newPwd)
        OkHttpProxy.post(HttpUrl.updateUserPasswordUrl, tag, params, callback)
    }

    /**
     * 1.4
     */
    fun updateAppToken(tag: Context, userToken: String, appToken: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("userToken" to userToken, "appToken" to appToken)
        OkHttpProxy.post(HttpUrl.updateClientIdUrl, tag, params, callback)
    }

    /**
     * 1.5
     */
    fun getUserInfo(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token)
        OkHttpProxy.post(HttpUrl.getUserInfoUrl, tag, params, callback)
    }

    //第二部分，电池操作
    /**
     * 2.1
     * 获取设备列表
     */
    fun getDeviceInfoList(tag: Context, token: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "name" to name)
        OkHttpProxy.post(HttpUrl.deviceInfoListUrl, tag, params, callback)
    }

    /**
     * 2.2
     * 获取位置描述。type=0移动。1联通。
     */
    fun getLocation(tag: Context, token: String, lac: Int, cid: Int, type: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "cid" to "$cid", "lac" to "$lac", "type" to "$type")
        OkHttpProxy.post(HttpUrl.getLocationUrl, tag, params, callback)
    }

    /**
     * 2.3
     * 自定义设备名称
     */
    fun updateName(tag: Context, token: String, uid: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "name" to name)
        OkHttpProxy.post(HttpUrl.updateNameUrl, tag, params, callback)
    }

    /**
     * 2.4
     * 获取某设备下的所有电池
     */
    fun getBatteryDataByUid(tag: Context, token: String, uid: String, pageSize: Int, pageNum: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.getBatteryListUrl, tag, params, callback)
    }

    /**
     * 2.5
     * 获得历史数据
     */
    fun getBatteryDataList(tag: Context, token: String, batteryId: Long, pageSize: Int, pageNum: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "batteryId" to "$batteryId", "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.batteryDataUrl, tag, params, callback)
    }

    /**
     * 2.6
     */
    fun getBatterySumList(tag: Context, token: String, batteryId: Long, key: String, period: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "batteryId" to "$batteryId", "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.batteryChartUrl, tag, params, callback)
    }

    /**
     * 2.7
     */
    fun getDeviceDataList(tag: Context, token: String, uid: String, pageSize: Int, pageNum: Int,
                      callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.deviceDataUrl, tag, params, callback)
    }

    /**
     * 2.8
     */
    fun getDeviceSumList(tag: Context, token: String, uid: String, key: String, period: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.deviceChartUrl, tag, params, callback)
    }

    /**
     * 2.9
     * 获得某指定列的历史数据
     */
    fun getDeviceKeyDataList(tag: Context, token: String, uid: String, key: String, pageSize: Int,
                             pageNum: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid,
                "key" to key, "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.deviceKeyDataUrl, tag, params, callback)
    }

}
