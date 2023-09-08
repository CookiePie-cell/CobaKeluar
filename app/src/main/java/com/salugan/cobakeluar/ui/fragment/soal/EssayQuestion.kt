package com.salugan.cobakeluar.ui.fragment.soal

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
import com.bumptech.glide.Glide
import com.salugan.cobakeluar.databinding.FragmentEssayQuestionBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.utils.QUESTION
import io.github.kexanie.library.MathView

class EssayQuestion : Fragment() {

    private var _binding: FragmentEssayQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEssayQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val question = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(QUESTION, QuestionModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(QUESTION)
        }
        val mathView: MathView = binding.formulaOne

        val flags = Html.FROM_HTML_MODE_COMPACT or Html.FROM_HTML_MODE_LEGACY

        if (question != null) {
            Log.d("terpanggil", question.toString())
            val images = mutableListOf<String>()
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                Log.d("mbmb", source)
                images.add(source.replace("""\"""", ""))
                null
            }, null).toString()


            for (i in 0 until images.size) {
                val imageView = ImageView(requireActivity())

                Glide.with(requireActivity())
                    .load(images[i])
                    .into(imageView)

                val desiredWidthInPixels = 720
                val desiredHeightInPixels = 480

                val layoutParams = LinearLayout.LayoutParams(
                    desiredWidthInPixels,
                    desiredHeightInPixels
                )

                layoutParams.gravity = Gravity.CENTER_HORIZONTAL

                imageView.layoutParams = layoutParams

                binding.llQuestion.addView(imageView)
            }
        }
    }
}