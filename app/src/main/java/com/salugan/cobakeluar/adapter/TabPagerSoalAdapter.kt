package com.salugan.cobakeluar.adapter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.fragment.soal.EssayQuestion
import com.salugan.cobakeluar.ui.fragment.soal.MultipleAnswerQuestion
import com.salugan.cobakeluar.ui.fragment.soal.MultipleChoiceQuestion
import com.salugan.cobakeluar.utils.QUESTION

class TabPagerSoalAdapter(activity: AppCompatActivity, private val questions: ArrayList<QuestionModel>, private val size: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = if (questions[position].isMultipleChoice) {
            MultipleChoiceQuestion()
        } else if (questions[position].isEssay) {
            EssayQuestion()
        } else {
            MultipleAnswerQuestion()
        }

        fragment.arguments = Bundle().apply {
            putParcelable(QUESTION, questions[position])
        }
        return fragment
    }
}