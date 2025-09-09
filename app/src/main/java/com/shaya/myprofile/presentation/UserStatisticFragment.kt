package com.shaya.myprofile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.shaya.myprofile.databinding.FragmentUserStatisticBinding
import com.shaya.myprofile.presentation.profile_activity.ProfileViewModel
import com.shaya.myprofile.util.visible


class UserStatisticFragment : Fragment() {

    private lateinit var binding: FragmentUserStatisticBinding
    private var mFragmentType: Int = 0
    private val viewmodel: ProfileViewModel by activityViewModels()

    companion object {

        private const val FRAGMENT_TYPE = "fragment_type"
        const val PARAM_SHOTS_FRAGMENT = 0
        const val PARAM_COLLECTION_FRAGMENT = 1

        @JvmStatic
        fun newInstance(
            fragmentType: Int
        ): UserStatisticFragment {
            val fragment = UserStatisticFragment()
            val args = Bundle()
            args.putInt(FRAGMENT_TYPE, fragmentType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentBasedOnType()
    }

    private fun setFragmentBasedOnType() {
        mFragmentType = arguments?.getInt(FRAGMENT_TYPE) ?: 0
        when (mFragmentType) {
            PARAM_SHOTS_FRAGMENT -> {
                binding.ivEmpty.visible(viewmodel.shotsCount == 0)
            }

            PARAM_COLLECTION_FRAGMENT -> {
                binding.ivEmpty.visible(viewmodel.collectionCount == 0)
            }
        }
    }
}