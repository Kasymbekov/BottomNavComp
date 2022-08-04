package com.bottomnavcomp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bottomnavcomp.databinding.FragmentNewsBinding
import com.bottomnavcomp.models.News
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        val text = binding.editText.text.toString()

        val news = News(0, text, System.currentTimeMillis())

        //Insert model into Room
        App.database.newsDao().insert(news)

        //Save model in Firestore
        saveToFirestore(news)

        val bundle = Bundle()
        bundle.putSerializable("news", news)
        parentFragmentManager.setFragmentResult("k_news", bundle)
        findNavController().navigateUp()
    }

    private fun saveToFirestore(news: News) {
        Firebase.firestore
            .collection("news")
            .add(news)
            .addOnSuccessListener { documentReference ->
                Log.d("RESULT", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("RESULT", "Error adding document", e)
            }
    }
}