package com.ybg.app.base.bean

import java.io.Serializable

/**
 * Created by ybg on 17-11-30.
 */
class Battery : Serializable {
    var id = 0L
    var createTime = ""//接收到的时间
    var uid = ""//为序列号即为“站号”
    var name = ""//用户自定义名称
    var catalogId = 0//分类ID
    var catalogName = ""//分类名称
    var lac = 0//基站定位，app定位地图
    var cid = 0//基站定位，app定位地图
    var num = 0//一个站内，多个被采集对象的序号
    var bv = 0f//电池电压
    var bt = 0f//电池温度
    var br = 0f//电池内阻
    var bi = 0f//电池电流
    var mepur = 0f//R相市电电压
    var mepus = 0f//S相市电电压
    var meput = 0f//T相市电电压
    var mepf = 0f//市电频率
    var bepur = 0f//R相旁路电压
    var bepus = 0f//S相旁路电压
    var beput = 0f//T相旁路电压
    var bepf = 0f//旁路频率
    var iepur = 0f//R相逆变电压
    var iepus = 0f//S相逆变电压
    var ieput = 0f//T相逆变电压
    var iepf = 0f//逆变频率
    var oepur = 0f//R相输出电压
    var oepus = 0f//S相输出电压
    var oeput = 0f//T相输出电压
    var oepf = 0f//输出频率
    var ocr = 0f//R相输出电流
    var ocs = 0f//S相输出电流
    var oct = 0f//T相输出电流
    var opr = 0f//R相输出功率
    var ops = 0f//S相输出功率
    var opt = 0f//T相输出功率
    var otp = 0f//输出总功率
    var opfrs = 0f//RS输出功率因素
    var opfst = 0f//ST输出功率因素
    var opftr = 0f//TR输出功率因素
    var btv = 0f//电池组电压
    var bbv = 0f//母线电压
    var bcc = 0f//电池充电电流
    var bdc = 0f//电池放电电流
    var mp = 0f//正母线电压
    var mn = 0f//负母线电压
    var hmv = 0f//合母电压
    var kmv = 0f//控母电压
    var oi = 0f//输出电流
    var dp = 0f//电池压差
    var chv = 0f//单体最高电压
    var clv = 0f//单体最低电压
    var cht = 0f//单体最高温度
    var chr = 0f//单体最高内阻

    companion object {
        private val serialVersionUID = -5719117491658774918L
    }
}