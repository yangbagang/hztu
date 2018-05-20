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
     * 获取电池系统列表
     */
    fun getBatteryBSList(tag: Context, token: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "name" to name)
        OkHttpProxy.post(HttpUrl.batteryBSListUrl, tag, params, callback)
    }

    /**
     * 获取UPS系统列表
     */
    fun getBatteryUPSList(tag: Context, token: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "name" to name)
        OkHttpProxy.post(HttpUrl.batteryUPSListUrl, tag, params, callback)
    }

    /**
     * 获取直流系统列表
     */
    fun getBatteryDCList(tag: Context, token: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "name" to name)
        OkHttpProxy.post(HttpUrl.batteryDCListUrl, tag, params, callback)
    }

    /**
     * 获取位置描述。type=0移动。1联通。
     */
    fun getLocation(tag: Context, token: String, lac: Int, cid: Int, type: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "cid" to "$cid", "lac" to "$lac", "type" to "$type")
        OkHttpProxy.post(HttpUrl.getLocationUrl, tag, params, callback)
    }

    /**
     * 自定义设备名称
     */
    fun updateName(tag: Context, token: String, uid: String, name: String, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "name" to name)
        OkHttpProxy.post(HttpUrl.updateNameUrl, tag, params, callback)
    }

    /**
     * 获取某设备下的所有电池
     */
    fun getBatteryDataByUid(tag: Context, token: String, uid: String, pageSize: Int, pageNum: Int, callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "uid" to uid, "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.getBatteryListUrl, tag, params, callback)
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

    fun getBSDataList(tag: Context, token: String, deviceId: Long, pageSize: Int, pageNum: Int,
                      callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.bsDataUrl, tag, params, callback)
    }

    fun getBSSumList(tag: Context, token: String, deviceId: Long, key: String, period: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.bsChartUrl, tag, params, callback)
    }

    fun getDCDataList(tag: Context, token: String, deviceId: Long, pageSize: Int, pageNum: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.dcDataUrl, tag, params, callback)
    }

    fun getDCSumList(tag: Context, token: String, deviceId: Long, key: String, period: Int,
                    callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.dcChartUrl, tag, params, callback)
    }

    fun getUPSDataList(tag: Context, token: String, deviceId: Long, pageSize: Int, pageNum: Int,
                     callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "pageSize" to "$pageSize", "pageNum" to "$pageNum")
        OkHttpProxy.post(HttpUrl.upsDataUrl, tag, params, callback)
    }

    fun getUPSSumList(tag: Context, token: String, deviceId: Long, key: String, period: Int,
                    callback: OkCallback<*>) {
        val params = mapOf<String, String>("token" to token, "deviceId" to "$deviceId", "key" to key, "period" to "$period")
        OkHttpProxy.post(HttpUrl.upsChartUrl, tag, params, callback)
    }

}
