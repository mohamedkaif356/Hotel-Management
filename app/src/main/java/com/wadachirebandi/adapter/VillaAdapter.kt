package com.wadachirebandi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wadachirebandi.data.villa.VillaData
import com.wadachirebandi.databinding.VillaItemViewBinding
import com.wadachirebandi.ui.fragments.VillaFragmentDirections

class VillaAdapter(private val view: View) : RecyclerView.Adapter<VillaAdapter.VillaViewHolder>() {

    inner class VillaViewHolder(val binding: VillaItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<VillaData>() {
        override fun areItemsTheSame(oldItem: VillaData, newItem: VillaData): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: VillaData, newItem: VillaData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VillaViewHolder {
        return VillaViewHolder(
            VillaItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VillaViewHolder, position: Int) {
        val villa = differ.currentList[position]
        holder.binding.apply {
            Glide.with(view).load(villa.gallery[0]).into(villaImage)
            villaName.text = villa.villaName
            villaCardView.setOnClickListener {
                val action =
                    VillaFragmentDirections.actionVillaFragmentToAddEntryFragment(villa._id)
                view.findNavController().navigate(action)
            }
        }
    }
}