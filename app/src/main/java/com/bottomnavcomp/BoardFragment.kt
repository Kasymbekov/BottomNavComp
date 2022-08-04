package com.bottomnavcomp

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bottomnavcomp.databinding.FragmentBoardBinding
import com.google.android.material.tabs.TabLayoutMediator


class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = BoardAdapter {
            close()
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

        }.attach()

        //Back button implementation
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        //Skip button listener
        binding.btnSkip.setOnClickListener {
            Prefs(requireContext()).saveBoardState()
            findNavController().navigateUp()
        }
    }

    private fun close() {
        Prefs(requireContext()).saveBoardState()
        findNavController().navigateUp()
    }


}