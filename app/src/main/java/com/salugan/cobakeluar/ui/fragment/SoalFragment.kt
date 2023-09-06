package com.salugan.cobakeluar.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.AnswerAdapter
import com.salugan.cobakeluar.databinding.FragmentSoalBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.model.SelectionModel
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
            val images = mutableListOf<String>()
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                Log.d("mbmb", source)
                images.add(source.replace("""\"""", ""))
                null
            }, null).toString()


            for(i in 0 until images.size) {
                val imageView = ImageView(requireActivity())

                Glide.with(requireActivity())
                    .load(images[i])
                    .into(imageView)

                val desiredWidthInPixels = 720 // Replace with your desired width in pixels
                val desiredHeightInPixels = 480 // Replace with your desired height in pixels

                val layoutParams = LinearLayout.LayoutParams(
                    desiredWidthInPixels,
                    desiredHeightInPixels
                )

                layoutParams.gravity = Gravity.CENTER_HORIZONTAL

                imageView.layoutParams = layoutParams

                binding.llQuestion.addView(imageView)
            }

            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvAnswer.layoutManager = layoutManager

            setAdapter(question.selections!!)
        }
    }

    private fun setAdapter(selections: List<SelectionModel>) {
        val selectionsArrayList = ArrayList<SelectionModel>()
        selections.forEach {
            selectionsArrayList.add(it)
        }
        val adapter = AnswerAdapter(selectionsArrayList)
        binding.rvAnswer.adapter = adapter
    }

    companion object {
        const val QUESTIONS = "extra_question"
    }
}
