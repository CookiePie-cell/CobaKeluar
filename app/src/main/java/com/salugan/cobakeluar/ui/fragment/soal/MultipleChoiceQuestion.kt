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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.adapter.AnswerAdapter
import com.salugan.cobakeluar.databinding.FragmentMultipleChoiceQuestionBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.model.SelectionModel
import com.salugan.cobakeluar.ui.activity.soal.SoalActivity
import com.salugan.cobakeluar.utils.QUESTION
import io.github.kexanie.library.MathView

class MultipleChoiceQuestion : Fragment() {

    private var _binding: FragmentMultipleChoiceQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private var answer: SelectionModel? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultipleChoiceQuestionBinding.inflate(inflater)
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

        viewPager = requireActivity().findViewById(R.id.viewPager)

        binding.btnNext.setOnClickListener {
            // Navigate to the next tab (fragment)
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem + 1, true)
        }


        binding.btnPrev.setOnClickListener {
            // Navigate to the previous tab (fragment)
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem - 1, true)
        }

        if (question != null) {
            val images = mutableListOf<String>()
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                images.add(source.replace("""\"""", ""))
                null
            }, null).toString()


            for(i in 0 until images.size) {
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

            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvAnswer.layoutManager = layoutManager

            setAdapter(question.selections!!, question)

            binding.btnJawab.setOnClickListener {
                if (answer != null) {
                    if (answer?.id == question.selectionAnswer?.get(0)?.selectionId) {
                        Toast.makeText(requireActivity(), "Jawaban benar", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity(), "Jawaban salah", Toast.LENGTH_SHORT).show()
                    }
                    question.hasSelected = true
                    binding.btnJawab.visibility = View.GONE
                    binding.btnCekPembahasan.visibility = View.VISIBLE
                    showBottomSheet(question.discussion?.get(0)?.discussionText)
                }
            }

            binding.btnCekPembahasan.setOnClickListener {
                showBottomSheet(question.discussion?.get(0)?.discussionText)
            }

            if (question.hasSelected) {
                binding.btnJawab.visibility = View.GONE
                binding.btnCekPembahasan.visibility = View.VISIBLE
            } else {
                binding.btnJawab.visibility = View.VISIBLE
                binding.btnCekPembahasan.visibility = View.GONE
            }
        }
    }

    private fun showBottomSheet(discussion: String?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val mathView = bottomSheetView.findViewById<MathView>(R.id.mvDiscussion)
        mathView.text = discussion
    }

    private fun setAdapter(selections: List<SelectionModel>, questionType: QuestionModel) {
        val selectionsArrayList = ArrayList<SelectionModel>()
        selections.forEach {
            selectionsArrayList.add(it)
        }
        val adapter = AnswerAdapter(selectionsArrayList, questionType) { selection ->
            answer = selection
        }
        binding.rvAnswer.adapter = adapter
    }
}
