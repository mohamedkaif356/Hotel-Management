package com.wadachirebandi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wadachirebandi.data.entry.SingleEntry
import com.wadachirebandi.databinding.EntryItemViewBinding
import com.wadachirebandi.ui.viewmodels.EntryViewModel

class EntryAdapter(private val viewModel: EntryViewModel) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(val binding: EntryItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SingleEntry>() {
        override fun areItemsTheSame(oldItem: SingleEntry, newItem: SingleEntry): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: SingleEntry, newItem: SingleEntry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        return EntryViewHolder(
            EntryItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = differ.currentList[position]
        holder.binding.apply {
            name.text = entry.guestName
            customerNumber.text = entry.contact
            guestNumber.text = entry.guests
            paymentAmount.text = entry.amount.toString()
            paymentMethod.text = entry.paymentMethod
            checkInDate.text = entry.checkIn
            checkOutDate.text = entry.checkOut
            btnDeleteEntry.setOnClickListener {
                viewModel.deleteEntry(entry._id)
            }
        }
    }
}