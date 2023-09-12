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
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.FragmentEssayQuestionBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.ui.activity.hasil.ActivityHasil
import com.salugan.cobakeluar.ui.activity.soal.SoalActivity
import com.salugan.cobakeluar.utils.QUESTION
import io.github.kexanie.library.MathView

class EssayQuestion : Fragment() {

    private var _binding: FragmentEssayQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private lateinit var tab: TabLayout

    private lateinit var bottomSheetDialog: BottomSheetDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEssayQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = requireActivity().findViewById(R.id.viewPager)

        tab = requireActivity().findViewById(R.id.tabs)

        val question = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(QUESTION, QuestionModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(QUESTION)
        }
        val mathView: MathView = binding.formulaOne

        val flags = Html.FROM_HTML_MODE_COMPACT or Html.FROM_HTML_MODE_LEGACY

        setButtonNext()

        setButtonPrevious()

        if (question != null) {
            Log.d("terpanggil", question.toString())
            val images = mutableListOf<String>()
            mathView.text = Html.fromHtml(question.questionText, flags, { source ->
                Log.d("mbmb", source)
                images.add(source.replace("""\"""", ""))
                null
            }, null).toString()


            setBtnJawab(question)

            binding.btnCekPembahasanEssay.setOnClickListener {
                showBottomSheet(question.discussion?.get(0)?.discussionText)
            }
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


    private fun setBtnJawab(question: QuestionModel) {
        binding.btnJawab.setOnClickListener {
            val userAnswer: Double = binding.edtJawaban.text.toString().toDouble()
            val tabView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView

            val systemAnswer: Double? = question.shortAnswer?.get(0)?.shortAnswerText?.toDouble()
            val systemAnswer1: Double? = question.shortAnswer?.get(0)?.firstRange?.toDouble()
            val systemAnswer2: Double? = question.shortAnswer?.get(0)?.secondRange?.toDouble()

            if (systemAnswer == userAnswer) {
                jawabanBenar(tabView)
            } else {
                if (systemAnswer1!! > systemAnswer2!!) {
                    if (userAnswer in systemAnswer2..systemAnswer1) {
                        jawabanBenar(tabView)
                    } else {
                        jawabanSalah(tabView)
                    }
                } else {
                    if (userAnswer in systemAnswer1..systemAnswer2) {
                        jawabanBenar(tabView)
                    } else {
                        jawabanSalah(tabView)
                    }
                }
            }

            question.hasSelected = true
            binding.btnJawab.visibility = View.GONE
            binding.btnCekPembahasanEssay.visibility = View.VISIBLE
            showBottomSheet(question.discussion?.get(0)?.discussionText)

            val selectedTab = tab.getTabAt(viewPager.currentItem)

            selectedTab?.customView = tabView
        }
    }

    private fun jawabanBenar(tab: TextView) {
        Toast.makeText(requireActivity(), "Jawaban benar", Toast.LENGTH_SHORT).show()
        (requireActivity() as SoalActivity).score += 1
        (requireActivity() as SoalActivity).answers[viewPager.currentItem] = 1
        tab.setBackgroundResource(R.drawable.tab_correct_background)
    }

    private fun jawabanSalah(tab: TextView) {
        Toast.makeText(requireActivity(), "Jawaban salah", Toast.LENGTH_SHORT).show()
        (requireActivity() as SoalActivity).answers[viewPager.currentItem] = 2
        tab.setBackgroundResource(R.drawable.tab_uncorrect_background)
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

    private fun showBottomSheet(discussion: String?) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val mathView = bottomSheetView.findViewById<MathView>(R.id.mvDiscussion)
        mathView.text = discussion
    }
}