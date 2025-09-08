package com.shaya.myprofile.presentation.profile_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.shaya.myprofile.R
import com.shaya.myprofile.databinding.ActivityProfileBinding
import com.shaya.myprofile.domain.Social
import com.shaya.myprofile.domain.Statistics
import com.shaya.myprofile.domain.User
import com.shaya.myprofile.presentation.profile_activity.components.MediaPagerAdapter
import com.shaya.myprofile.presentation.profile_activity.components.SocialAdapter
import com.shaya.myprofile.presentation.profile_activity.components.SocialItem
import com.shaya.myprofile.presentation.profile_activity.components.SocialType
import com.shaya.myprofile.util.Resource
import com.shaya.myprofile.util.loadImageByUrl
import com.shaya.myprofile.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var socialAdapter: SocialAdapter
    lateinit var binding: ActivityProfileBinding
    private var socialItems: MutableList<SocialItem> = mutableListOf()
    private lateinit var adapter: MediaPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeScreenFullView()
        /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
             val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
             insets
         }*/

        // trigger fetch
        viewModel.fetchUser()

        // collect StateFlow safely with lifecycle
        lifecycleScope.launchWhenStarted {
            viewModel.user.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        // show progress bar
                    }

                    is Resource.Success -> {
                        val user = state.data
                        Log.d("TAG", user.toString())
                        user?.let {
                            setUserData(it)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@ProfileActivity, state.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        initSocialRecyclerView()



    }

    fun makeScreenFullView(view: View? = null) {
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
                // Return CONSUMED if you don't want want the window insets to keep passing
                // down to descendant views.
                WindowInsetsCompat.CONSUMED
            }

            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(user: User) {

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
        // Setup adapter
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

    private fun setStatistics(statics: Statistics){
        lifecycleScope.launch {

            binding.apply {
                adapter = MediaPagerAdapter(
                    supportFragmentManager,
                    lifecycle
                )

                val shotCount = statics.activity.shots
                val collectionCount = statics.activity.collections
                tlSelector.addTab(tlSelector.newTab().setText("$shotCount shots"))
                tlSelector.addTab(tlSelector.newTab().setText("$collectionCount Collection"))
                viewpager.adapter = adapter


                tlSelector.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.let {
                            viewpager.currentItem = tab.position
                        }
                    }
                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        tab?.let {
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
}