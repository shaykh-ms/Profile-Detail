package com.shaya.myprofile.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shaya.myprofile.databinding.FragmentEmojiBinding
import com.shaya.myprofile.presentation.profile_activity.ProfileViewModel
import hilt_aggregated_deps._com_shaya_myprofile_presentation_profile_activity_ProfileViewModel_HiltModules_BindsModule


class EmojiFragment : Fragment() {

    private lateinit var binding: FragmentEmojiBinding
    //private val viewModel: ProfileViewModel by
    private var mFragmentType: Int = 0

    companion object {

         private const val FRAGMENT_TYPE = "fragment_type"
         const val PARAM_SHOTS_FRAGMENT = 0
         const val PARAM_COLLECTION_FRAGMENT = 1

        @JvmStatic
        fun newInstance(
            fragmentType: Int,
        ): EmojiFragment {
            val fragment = EmojiFragment()
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
        binding = FragmentEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentBasedOnType()
    }


    private fun setFragmentBasedOnType(){
        mFragmentType = arguments?.getInt(FRAGMENT_TYPE)?:0
        when(mFragmentType){
            PARAM_SHOTS_FRAGMENT -> {


            }
            PARAM_COLLECTION_FRAGMENT -> {


            }
        }
    }






}