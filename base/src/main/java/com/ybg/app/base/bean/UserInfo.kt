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

    companion object {

        private val serialVersionUID = -5719117491648774918L
    }

    override fun toString(): String =
            "UserInfo(id=$id, name='$name', mobile='$mobile')"
}
