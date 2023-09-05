package com.salugan.cobakeluar.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salugan.cobakeluar.databinding.FragmentSoalBinding
import com.salugan.cobakeluar.model.QuestionModel
import io.github.kexanie.library.MathView
import org.jsoup.Jsoup

class SoalFragment : Fragment() {

    private var _binding: FragmentSoalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSoalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(QUESTIONS, QuestionModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(QUESTIONS)
        }
//        val mathView: MathView = binding.formulaOne
//        if (question != null) {
//            mathView.text =  Html.fromHtml(question.discussion?.get(0)?.discussionText, Html.FROM_HTML_MODE_LEGACY).toString()
//            Log.d("tyutyu", Html.fromHtml(question.discussion?.get(0)?.discussionText, Html.FROM_HTML_MODE_LEGACY).toString())
//        }

    }
    companion object {
        const val QUESTIONS = "extra_question"
    }
}