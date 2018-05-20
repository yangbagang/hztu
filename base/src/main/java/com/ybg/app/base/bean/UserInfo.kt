package com.ybg.app.base.bean

import java.io.Serializable

/**
 * Created by yangbagang on 2016/10/27.
 */
class UserInfo : Serializable {

    var id = 0L
    var name = "未填写"
    var mobile = ""
    var company = ""
    var email = ""
    var token = ""//token
    var appToken = ""//appToken
    var code = ""//用户编号
    var sideNum = 0//站点数量
    var installedCapacity = 0//装机容量

    companion object {

        private val serialVersionUID = -5719117491648774918L
    }

    override fun toString(): String =
            "UserInfo(id=$id, name='$name', mobile='$mobile')"
}
