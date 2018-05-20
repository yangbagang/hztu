package com.ybg.app.hztu.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabFragmentPagerAdapter(var fm: FragmentManager, var list: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

}