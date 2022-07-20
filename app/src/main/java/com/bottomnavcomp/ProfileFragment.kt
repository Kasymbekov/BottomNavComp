package com.bottomnavcomp

import android.R
import android.R.attr.data
import android.app.Activity.RESULT_OK
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
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Prefs(requireContext()).checkImage("image_data")) {
            var image = Uri.parse(Prefs(requireContext()).getString("image_data"))
            binding.imageView.setImageURI(image)
        }

        Log.e("ERROR", Prefs(requireContext())?.getString("image_data").toString())
//        binding.imageView2.setImageURI(Uri.parse(Prefs(requireContext())?.getString("image_data")))


        binding.imageView.setOnClickListener {
            pickImageGallery()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch(intent)
    }

    var getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

                val data: Intent? = result.data
                binding.imageView.setImageURI(data?.data)
//                val preferences = Prefs(requireContext())
                Prefs(requireContext()).setString("image_data", data?.data.toString())
//
                var image = Uri.parse(Prefs(requireContext()).getString("image_data"))
                Log.e("ERROR", image.toString())
                binding.imageView.setImageURI(image)

            }
        }


}