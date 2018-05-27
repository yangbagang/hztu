package com.ybg.app.hztu.activity.battery

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ybg.app.hztu.R

@SuppressLint("ValidFragment")
class BatteryListFragment(var uid: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_battery_list,  container)
        loadBatteryList()
        return view
    }

    private fun loadBatteryList() {

    }
}