package com.shaya.myprofile.presentation.profile_activity.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaya.myprofile.R
import com.shaya.myprofile.databinding.RowItemSocialBinding

class SocialAdapter(
    private val items: List<SocialItem>,
    private val onItemClick: (SocialItem) -> Unit
) : RecyclerView.Adapter<SocialAdapter.SocialViewHolder>() {

    inner class SocialViewHolder(val binding: RowItemSocialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SocialItem, isLast: Boolean) {
            // Set icon dynamically
            val iconRes = when (item.type) {
                SocialType.WEBSITE -> R.drawable.ic_website
                SocialType.INSTAGRAM -> R.drawable.ic_instagram
                SocialType.FACEBOOK -> R.drawable.ic_facebook
            }
            binding.ivSocial.setImageResource(iconRes)

            // Handle click
            binding.ivSocial.setOnClickListener { onItemClick(item) }

            // Hide dot if last item
            binding.ivDot.visibility = if (isLast) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialViewHolder {
        val binding = RowItemSocialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SocialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialViewHolder, position: Int) {
        holder.bind(items[position], position == items.size - 1)
    }

    override fun getItemCount(): Int = items.size
}
