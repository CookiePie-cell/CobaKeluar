//package com.salugan.cobakeluar.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.salugan.cobakeluar.R
//import com.salugan.cobakeluar.databinding.ItemCardBinding
//import com.salugan.cobakeluar.model.UserModel
//
//class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
//    private var data: List<UserModel> = emptyList()
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_card, parent, false)
//        return ViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentItem = data[position]
//        holder.bind(currentItem)
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val binding: ItemCardBinding = ItemCardBinding.bind(itemView)
//
//        fun bind(item: UserModel) {
//            binding.testOrder.text = item.testOrder
//            binding.waktu.text = item.waktu
//            binding.tanggal.text = item.tanggal
//            binding.nilaiData.text = item.nilai
//            binding.benar.text = item.benar
//            binding.salah.text = item.salah
//        }
//    }
//}