package com.wadachirebandi.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wadachirebandi.admin.databinding.CustomerIdItemViewBinding

class CustomerIdsAdapter(private val view: View) : RecyclerView.Adapter<CustomerIdsAdapter.CustomerIdsViewHolder>() {

    inner class CustomerIdsViewHolder(val binding: CustomerIdItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerIdsViewHolder {
        return CustomerIdsViewHolder(
            CustomerIdItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CustomerIdsViewHolder, position: Int) {
        val image = differ.currentList[position]
        Glide.with(view).load(image).into(holder.binding.imageView)
    }

}