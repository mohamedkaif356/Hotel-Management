package com.wadachirebandi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wadachirebandi.databinding.ViewPagerItemViewBinding

class ViewPagerAdapter(
    private val image: MutableList<String>,
    private val view: View
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {


    class ViewPagerViewHolder(val binding: ViewPagerItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            ViewPagerItemViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentImage = image[position]
        Glide.with(view).load(currentImage).into(holder.binding.ivImage)
    }

    override fun getItemCount(): Int {
        return image.size

    }
}