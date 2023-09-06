package com.salugan.cobakeluar.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.salugan.cobakeluar.ui.fragment.SoalFragment

class TabPagerSoalAdapter(activity: AppCompatActivity, private val size: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {
        return SoalFragment()
    }
}