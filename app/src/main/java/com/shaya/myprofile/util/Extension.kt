package com.shaya.myprofile.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.shaya.myprofile.R

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImageByUrl(context: Context, url: String, isCenterCrop: Boolean = true) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 50f
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(
            context,
            R.color.red_test
        )
    )
    circularProgressDrawable.start()
    Glide
        .with(context)
        .load(url)
        .apply {
            if (isCenterCrop) {
                centerCrop()
            }
        }
        .placeholder(circularProgressDrawable)
        .into(this)
}