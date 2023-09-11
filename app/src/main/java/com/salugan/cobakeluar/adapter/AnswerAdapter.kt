package com.salugan.cobakeluar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salugan.cobakeluar.R
import com.salugan.cobakeluar.databinding.AnswerItemBinding
import com.salugan.cobakeluar.model.QuestionModel
import com.salugan.cobakeluar.model.SelectionModel


class AnswerAdapter(
    private val selections: ArrayList<SelectionModel>, private val question: QuestionModel,
    private val onClick: (SelectionModel) -> Unit,
) : RecyclerView.Adapter<AnswerAdapter.ViewHolder>(){

    private var isSelected = false
    fun setSelected(value: Boolean) {
        isSelected = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerAdapter.ViewHolder {
        val binding = AnswerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerAdapter.ViewHolder, position: Int) {
        val selection = selections[position]
        holder.bind(selection)
        if (!isSelected) {
            holder.itemView.setOnClickListener {
                if (question.isMultipleChoice) {
                    selections.forEach { it.isSelected = false }
                    selection.isSelected = true
                } else {
                    selection.isSelected = !selection.isSelected
                }
                onClick(selection)

                notifyDataSetChanged()
            }
        } else {
            holder.itemView.isClickable = false
        }
    }

    override fun getItemCount(): Int = selections.size

    inner class ViewHolder(private val binding: AnswerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(selection: SelectionModel) {
            binding.mvSelection.text = selection.text

            if (selection.image != "image") {
                binding.ivSelection.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(selection.image)
                    .into(binding.ivSelection)
            } else {
                binding.ivSelection.visibility = View.GONE
            }
            if (selection.isSelected) {
                itemView.setBackgroundResource(R.drawable.radio_selected)
            } else {
                itemView.setBackgroundResource(R.drawable.radio_not_selected)
            }

        }
    }
}