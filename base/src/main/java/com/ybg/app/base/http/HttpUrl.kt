package com.ybg.app.base.http

import com.ybg.app.base.constants.AppConstant

/**
 * 网络请求相关设置,配置请求地址及参数
 */
object HttpUrl {

    //全局定义
    //开发服务器地址
    private const val API_HOST_DEBUG = "http://192.168.1.3:8080/hztua"
    //生产服务器地址
    private const val API_HOST_PRODUCT = "http://47.100.22.227:8080/hztua"

    private val ROOT_URL = if (AppConstant.isDebug) API_HOST_DEBUG else API_HOST_PRODUCT

    //第一部分，用户操作
    val userLoginUrl: String
        get() = "$ROOT_URL/userInfo/login"
    val userLogoutUrl: String
        get() = "$ROOT_URL/userInfo/logout"
    val updateUserPasswordUrl: String
        get() = "$ROOT_URL/userInfo/updatePassword"
    val getUserInfoUrl: String
        get() = "$ROOT_URL/userInfo/getUserInfo"
    val updateClientIdUrl: String
        get() = "$ROOT_URL/userInfo/updateAppToken"

    //第二部分，电池操作
    val deviceInfoListUrl: String
        get() = "$ROOT_URL/deviceInfo/list"
    val getLocationUrl: String
        get() = "$ROOT_URL/batteryLocation/getLocation"
    val updateNameUrl: String
        get() = "$ROOT_URL/deviceInfo/updateName"
    val getBatteryListUrl: String
        get() = "$ROOT_URL/battery/listByUid"
    val batteryDataUrl: String
        get() = "$ROOT_URL/batteryHistory/list"
    val batteryChartUrl: String
        get() = "$ROOT_URL/batteryHistory/calculate"
    val deviceDataUrl: String
        get() = "$ROOT_URL/deviceValueHistory/list"
    val deviceChartUrl: String
        get() = "$ROOT_URL/deviceValueHistory/calculate"

}
