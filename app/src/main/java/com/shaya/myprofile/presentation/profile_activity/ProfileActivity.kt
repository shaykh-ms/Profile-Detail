package com.shaya.myprofile.presentation.profile_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.shaya.myprofile.databinding.ActivityProfileBinding
import com.shaya.myprofile.domain.Activity
import com.shaya.myprofile.domain.Social
import com.shaya.myprofile.domain.Statistics
import com.shaya.myprofile.domain.User
import com.shaya.myprofile.presentation.profile_activity.components.ActivityPagerAdapter
import com.shaya.myprofile.presentation.profile_activity.components.SocialAdapter
import com.shaya.myprofile.presentation.profile_activity.components.SocialItem
import com.shaya.myprofile.presentation.profile_activity.components.SocialType
import com.shaya.myprofile.util.Resource
import com.shaya.myprofile.util.loadImageByUrl
import com.shaya.myprofile.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var socialAdapter: SocialAdapter
    private lateinit var binding: ActivityProfileBinding
    private var socialItems: MutableList<SocialItem> = mutableListOf()
    private lateinit var adapter: ActivityPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeScreenFullView()
        initSocialRecyclerView()
        observeUser()
        viewModel.fetchUser()
    }

    private fun makeScreenFullView(view: View? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            setWindowInsetListener(view)
        } else {
            setWindowInsetListener(view)
        }
    }

    private fun setWindowInsetListener(view: View?) {
        view?.let {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = insets.right
                }
                WindowInsetsCompat.CONSUMED
            }

            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(user: User) {
        setActivityCounts(user.statistics.activity)
        setSocialItems(user.social)
        setStatistics(user.statistics)

        binding.apply {
            tvUsername.text = user.username
            tvName.text = user.name
            tvLocation.text = "${user.location.city}, ${user.location.country}"
            ivProfile.loadImageByUrl(this@ProfileActivity, user.avatar)
            tvFollowingCount.text = user.statistics.following.toString()
            tvFollowerCount.text = user.statistics.followers.toString()
        }
    }

    private fun initSocialRecyclerView() {
        socialAdapter = SocialAdapter(socialItems) { item ->
            // Handle click events
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            startActivity(intent)
        }
        binding.rvSocial.apply {
            adapter = socialAdapter
            layoutManager = LinearLayoutManager(
                this@ProfileActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setSocialItems(social: Social) {
        binding.apply {
            val hasAny = social.website.isNotBlank() || social.profiles.any { it.url.isNotBlank() }
            if (hasAny) clSocial.visible(true) else return

            if (social.website.isNotBlank()) {
                socialItems.add(SocialItem(SocialType.WEBSITE, social.website))
            }

            social.profiles
                .filter { it.url.isNotBlank() }
                .forEach {
                    val type = when (it.platform.lowercase()) {
                        "instagram" -> SocialType.INSTAGRAM
                        "facebook" -> SocialType.FACEBOOK
                        else -> null
                    }
                    type?.let { t ->
                        socialItems.add(SocialItem(t, it.url))
                    }
                }
            Log.d("TAG", "setSocialItems: ${socialItems} ")
            socialAdapter.notifyDataSetChanged()
        }
    }

    private fun setStatistics(statics: Statistics) {
        lifecycleScope.launch {

            binding.apply {
                adapter = ActivityPagerAdapter(
                    supportFragmentManager,
                    lifecycle,
                )

                val shotCount = statics.activity.shots
                val collectionCount = statics.activity.collections


                tlSelector.addTab(tlSelector.newTab().setText("$shotCount shots"))
                tlSelector.addTab(tlSelector.newTab().setText("$collectionCount Collection"))
                val firstTab = tlSelector.getTabAt(0)
                val firstTabTextView = (firstTab?.view?.getChildAt(1) as? TextView)
                firstTabTextView?.setTypeface(null, Typeface.BOLD)
                viewpager.adapter = adapter


                tlSelector.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.let {
                            viewpager.currentItem = it.position
                            val tabTextView = (it.view.getChildAt(1) as? TextView)
                            tabTextView?.setTypeface(null, Typeface.BOLD)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        tab?.let {
                            val tabTextView = (it.view.getChildAt(1) as? TextView)
                            tabTextView?.setTypeface(null, Typeface.NORMAL)
                        }
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })

                viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        tlSelector.selectTab(tlSelector.getTabAt(position))
                    }
                })
            }
        }
    }

    private fun setActivityCounts(activity: Activity) {
        viewModel.setShotsCount(activity.shots)
        viewModel.setCollectionCount(activity.collections)
    }

    private fun observeUser() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { state ->
                    when (state) {
                        is Resource.Loading -> {
                            binding.clProgressbar.visible(state.isLoading)
                        }

                        is Resource.Success -> {
                            val user = state.data
                            user?.let { setUserData(it) }
                            binding.clProgressbar.visible(false)
                        }

                        is Resource.Error -> {
                            Toast.makeText(this@ProfileActivity, state.message, Toast.LENGTH_SHORT)
                                .show()
                            binding.clProgressbar.visible(false)
                        }
                    }
                }
            }
        }
    }
}