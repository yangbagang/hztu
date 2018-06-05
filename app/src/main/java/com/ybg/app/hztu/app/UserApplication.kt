package com.ybg.app.hztu.app

import android.content.Context
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.ybg.app.base.app.YbgAPP

/**
 * Created by ybg on 17-11-16.
 */
class UserApplication : YbgAPP() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        initImageLoader(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        container.clear()
    }

    private fun initImageLoader(context: Context) {
        val config = ImageLoaderConfiguration.Builder(context)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024) // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        config.writeDebugLogs() // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build())
    }

    companion object {

        var instance: UserApplication? = null
            private set
    }

    fun setAutoPlay(boolean: Boolean) {
        preference.setBoolean("autoPlay", boolean)
    }

    fun isAutoPlay(): Boolean = preference.getBoolean("autoPlay", false)

    fun setReceiverMsg(boolean: Boolean) {
        preference.setBoolean("receiverMsg", boolean)
    }

    fun isReceiverMsg(): Boolean = preference.getBoolean("receiverMsg", false)

    private val container = mutableMapOf<String, Any>()

    var password: String
        get() = preference.getString("password", "")
        set(pwd) = preference.setString("password", pwd)
    var mobile: String
        get() = preference.getString("mobile", "")
        set(pwd) = preference.setString("mobile", pwd)

}