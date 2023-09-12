package com.salugan.cobakeluar.ui.fragment.soal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.view.Gravity

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.FragmentMultipleAnswerQuestionBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.model.SelectionModel
import com.salugan.cobakeluar.ui.activity.hasil.ActivityHasil
import com.salugan.cobakeluar.ui.activity.soal.SoalActivity
import com.salugan.cobakeluar.utils.QUESTION
import io.github.kexanie.library.MathView

class MultipleAnswerQuestion : Fragment() {

    private var _binding: FragmentMultipleAnswerQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private lateinit var tab: TabLayout

    private var answers: MutableList<SelectionModel?> = mutableListOf()

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultipleAnswerQuestionBinding.inflate(inflater)
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

        tab = requireActivity().findViewById(R.id.tabs)

        setButtonNext()

        setButtonPrevious()

        if (question != null) {
            val images = mutableListOf<String>()
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                images.add(source.replace("""\"""", ""))
                null
            }, null).toString()


            setImageInQuestion(images)

            setAnswerList(question)

            setBtnJawab(question)
            binding.btnCekPembahasan.setOnClickListener {
                showBottomSheet(question.discussion?.get(0)?.discussionText)
            }

            checkQuestionState(question)
        }
    }

    private fun setBtnJawab(question: QuestionModel) {

        binding.btnJawab.setOnClickListener {
            Log.d("jawabbund", question.selections.toString())
            question.selections?.forEach {
                if (it.isSelected) answers.add(it)
            }

            val tabView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            var currentIndex = 0
            for (userAnswer in answers) {
                var isAnswerCorrect = false
                for (i in 0 until question.selectionAnswer!!.size) {
                    currentIndex = i
                    if (userAnswer?.id == question.selectionAnswer[i]?.selectionId) {
                        isAnswerCorrect = true
                        val child = binding.answerOptionsContainer.getChildAt(i)
                        child.setBackgroundResource(R.drawable.radio_correct)
                        break
                    }
                }
                if (!isAnswerCorrect) {
                    val child = binding.answerOptionsContainer.getChildAt(currentIndex)
                    child.setBackgroundResource(R.drawable.radio_incorrect)
                }
                currentIndex = 0
            }


            question.hasSelected = true
            binding.btnJawab.visibility = View.GONE
            binding.btnCekPembahasan.visibility = View.VISIBLE
            showBottomSheet(question.discussion?.get(0)?.discussionText)

            val selectedTab = tab.getTabAt(viewPager.currentItem)

            selectedTab?.customView = tabView
            disableScrolling()
        }
    }
    private fun setAnswerList(question: QuestionModel) {

        val images = mutableListOf<String>()
        for (option in question.selections!!) {
            val linearLayout = LinearLayout(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT  // Height
            )

            linearLayout.setPadding(16, 24, 16, 24)

            linearLayout.setBackgroundResource(R.drawable.radio_not_selected)

            layoutParams.setMargins(0, 0, 0, 16)

            linearLayout.orientation = LinearLayout.VERTICAL

            val mathViewOption = MathView(requireContext(), null)
            val mathViewLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mathViewLayoutParams.setMargins(8,8,8,8)
            mathViewOption.layoutParams = mathViewLayoutParams

            mathViewOption.text = option.text

            linearLayout.addView(mathViewOption)

            setImageInQuestion(images)
            linearLayout.setOnClickListener {
                option.isSelected = !option.isSelected
                for (i in 0 until binding.answerOptionsContainer.childCount) {
                    val child = binding.answerOptionsContainer.getChildAt(i)
                    if (child is LinearLayout) {
                        val optionAtIndex = question.selections[i]
                        if (optionAtIndex.isSelected) {
                            child.setBackgroundResource(R.drawable.radio_selected)
                        } else {
                            child.setBackgroundResource(R.drawable.radio_not_selected)
                        }
                    }
                }
            }

            linearLayout.layoutParams = layoutParams
            binding.answerOptionsContainer.addView(linearLayout)
        }
    }


    private fun disableScrolling() {
        for (i in 0 until binding.answerOptionsContainer.childCount) {
            val child = binding.answerOptionsContainer.getChildAt(i)
            if (child is LinearLayout) {
                child.setOnClickListener(null)
            }
        }
    }
//    private fun checkAnswerCorrectness(isCorrect: Boolean) {
//        for (i in 0 until binding.answerOptionsContainer.childCount) {
//            val child = binding.answerOptionsContainer.getChildAt(i)
//            if (child is LinearLayout) {
//                val mathview = child.getChildAt(0) as MathView
//                an
//            }
//        }
//    }

    private fun checkQuestionState(question: QuestionModel) {
        if (question.hasSelected) {
            binding.btnJawab.visibility = View.GONE
            binding.btnCekPembahasan.visibility = View.VISIBLE
        } else {
            binding.btnJawab.visibility = View.VISIBLE
            binding.btnCekPembahasan.visibility = View.GONE
        }
    }

    private fun setButtonNext() {
        binding.btnNext.setOnClickListener {
            val totalItems = viewPager.adapter?.itemCount ?: 0

            val currentItem = viewPager.currentItem

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                (requireActivity() as SoalActivity).soalViewModel.resetTimer()
                val score = (requireActivity() as SoalActivity).score

                val completionTimeMillis = (requireActivity() as SoalActivity).soalViewModel.calculateCompletionTime()
                val completionTimeSeconds = completionTimeMillis / 1000
                val completionTimeString = DateUtils.formatElapsedTime(completionTimeSeconds)

                val intent = Intent(requireActivity(), ActivityHasil::class.java)
                intent.putExtra(ActivityHasil.SCORE, score)
                intent.putExtra(ActivityHasil.ANSWERS, ArrayList((requireActivity() as SoalActivity).answers))
                intent.putExtra(ActivityHasil.COMPLETION_TIME, completionTimeString)
                startActivity(intent)
            }
        }
    }

    private fun setButtonPrevious() {
        binding.btnPrev.setOnClickListener {
            // Navigate to the previous tab (fragment)
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem - 1, true)
        }
    }

    private fun setImageInQuestion(images: List<String>) {
        for(element in images) {
            val imageView = ImageView(requireActivity())

            Glide.with(requireActivity())
                .load(element)
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

    private fun showBottomSheet(discussion: String?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val mathView = bottomSheetView.findViewById<MathView>(R.id.mvDiscussion)
        mathView.text = discussion
    }
}