package com.salugan.cobakeluar.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.fragment.SoalFragment

class TabPagerSoalAdapter(activity: AppCompatActivity, private val questions: ArrayList<QuestionModel>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        val fragment = SoalFragment()

        fragment.arguments = Bundle().apply {
            putParcelable(SoalFragment.QUESTIONS, questions[position])
        }
        return fragment
    }
}