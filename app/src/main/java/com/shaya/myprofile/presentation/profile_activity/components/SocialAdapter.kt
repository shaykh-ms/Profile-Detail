package com.shaya.myprofile.presentation.profile_activity.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaya.myprofile.R
import com.shaya.myprofile.databinding.RowItemSocialBinding
import com.shaya.myprofile.util.visible

class SocialAdapter(
    private val items: List<SocialItem>,
    private val onItemClick: (SocialItem) -> Unit
) : RecyclerView.Adapter<SocialAdapter.SocialViewHolder>() {

    inner class SocialViewHolder(private val binding: RowItemSocialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SocialItem, isLast: Boolean) {
            val iconRes = when (item.type) {
                SocialType.WEBSITE -> R.drawable.ic_website
                SocialType.INSTAGRAM -> R.drawable.ic_instagram
                SocialType.FACEBOOK -> R.drawable.ic_facebook
            }
            binding.apply {
                ivSocial.setImageResource(iconRes)
                ivSocial.setOnClickListener { onItemClick(item) }
                ivDot.visible(!isLast)
            }
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
