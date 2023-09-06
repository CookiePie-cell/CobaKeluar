package com.salugan.cobakeluar.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.FragmentSoalBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.customview.RadioLinearLayout
import io.github.kexanie.library.MathView
import org.jsoup.Jsoup
import java.net.URL

class SoalFragment : Fragment() {

    private var _binding: FragmentSoalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val mathView: MathView = binding.formulaOne

        val flags = Html.FROM_HTML_MODE_COMPACT or Html.FROM_HTML_MODE_LEGACY

        if (question != null) {
            Log.d("poipoi", question.selections?.size.toString())
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                Glide.with(requireActivity())
                    .load(source.replace("""\"""", ""))
                    .into(binding.imgView)

                null
            }, null).toString()

            val llOption1 = binding.llOption1
            val llOption2 = binding.llOption2
            val llOption3 = binding.llOption3
            val llOption4 = binding.llOption4
            val llOption5 = binding.llOption5

            val mvOption1 = llOption1.findViewById<MathView>(R.id.mvOption1)
            val mvOption2 = llOption2.findViewById<MathView>(R.id.mvOption2)
            val mvOption3 = llOption3.findViewById<MathView>(R.id.mvOption3)
            val mvOption4 = llOption4.findViewById<MathView>(R.id.mvOption4)
            val mvOption5 = llOption5.findViewById<MathView>(R.id.mvOption5)

//            mvOption1.text = question.selections?.get(0)?.text
//            mvOption2.text = question.selections?.get(1)?.text
//            mvOption3.text = question.selections?.get(2)?.text
//            mvOption4.text = question.selections?.get(3)?.text
//            mvOption5.text = question.selections?.get(4)?.text

            llOption1.setOnClickListener {
                llOption1.toggle()
                setSelectionBackground(llOption1)
            }

            llOption2.setOnClickListener {
                llOption2.toggle()
                setSelectionBackground(llOption2)
            }

            llOption3.setOnClickListener {
                llOption3.toggle()
                setSelectionBackground(llOption3)
            }

            llOption4.setOnClickListener {
                llOption4.toggle()
                setSelectionBackground(llOption4)
            }

            llOption5.setOnClickListener {
                llOption5.toggle()
                setSelectionBackground(llOption5)
            }
        }


    }

    private fun setSelectionBackground(selectedView: RadioLinearLayout) {
        val radioLinearLayouts = listOf<RadioLinearLayout>(
            binding.llOption1,
            binding.llOption2,
            binding.llOption3,
            binding.llOption4,
            binding.llOption5
        )

        for (view in radioLinearLayouts) {
            if (view == selectedView) {
                view.setBackgroundResource(R.drawable.radio_selected)
            } else {
                view.setBackgroundResource(R.drawable.radio_not_selected)
            }
        }
    }

    companion object {
        const val QUESTIONS = "extra_question"
    }
}