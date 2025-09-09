package com.shaya.myprofile.presentation.profile_activity.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shaya.myprofile.presentation.UserStatisticFragment

class ActivityPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserStatisticFragment.newInstance(UserStatisticFragment.PARAM_SHOTS_FRAGMENT)
            }

            1 -> {
                UserStatisticFragment.newInstance(UserStatisticFragment.PARAM_COLLECTION_FRAGMENT)
            }

            else -> {
                UserStatisticFragment.newInstance(UserStatisticFragment.PARAM_SHOTS_FRAGMENT)
            }
        }
    }
}