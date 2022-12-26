package com.wadachirebandi.admin.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wadachirebandi.admin.data.entry.EntriesData
import com.wadachirebandi.admin.databinding.EntryItemViewBinding
import com.wadachirebandi.admin.ui.fragments.EntryFragmentDirections
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EntryAdapter(private val view: View) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(val binding: EntryItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<EntriesData>() {
        override fun areItemsTheSame(oldItem: EntriesData, newItem: EntriesData): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: EntriesData, newItem: EntriesData): Boolean {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = differ.currentList[position]
        holder.binding.apply {
            name.text = entry.guestName
            customerNumber.text = entry.contact
            guestNumber.text = entry.guests
            paymentAmount.text = entry.amount.toString()
            paymentMethod.text = entry.paymentMethod
            val inputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
            val checkIn: LocalDate = LocalDate.parse(entry.checkIn, inputFormatter)
            val checkOut: LocalDate = LocalDate.parse(entry.checkOut, inputFormatter)
            val checkInFinal: String = outputFormatter.format(checkIn)
            val checkOutFinal: String = outputFormatter.format(checkOut)

            checkInDate.text = checkInFinal
            checkOutDate.text = checkOutFinal
            entryCV.setOnClickListener {
                val action = EntryFragmentDirections.actionEntryFragmentToEntryDetailsFragment(entry._id)
                view.findNavController().navigate(action)
            }
        }
    }
}