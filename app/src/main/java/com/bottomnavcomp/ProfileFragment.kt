package com.bottomnavcomp

import android.R
import android.R.attr.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bottomnavcomp.databinding.FragmentProfileBinding
import com.bottomnavcomp.ui.home.NewsAdapter
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var adapter: ProfileAdapter = ProfileAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //check if it exists or not, and set
        if (Prefs(requireContext()).checkImage("image_data")) {
            var image = Uri.parse(Prefs(requireContext()).getString("image_data"))
            binding.imageView.setImageURI(image)
            binding.imgbg.setImageURI(image)
        }

        //profile image listener
        binding.imageView.setOnClickListener {
            pickImageGallery()
        }

        binding.profileRecyclerview.adapter = adapter
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch(intent)
    }

    private var getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                Prefs(requireContext()).setString("image_data", data?.data.toString())

                var image = Uri.parse(Prefs(requireContext()).getString("image_data"))
                binding.imageView.setImageURI(image)
                binding.imgbg.setImageURI(image)
            }
        }


}