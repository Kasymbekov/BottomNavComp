package com.bottomnavcomp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bottomnavcomp.R
import com.bottomnavcomp.databinding.FragmentHomeBinding
import com.bottomnavcomp.models.News

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Listener for item in recyclerview
        adapter = NewsAdapter() {

        }
        adapter.onLongClick = {
            //AlertDialog implementation
            openDialog(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.newsFragment)
        }
        parentFragmentManager.setFragmentResultListener(
            "k_news",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val news = bundle.getSerializable("news") as News

            adapter.addItem(news)
            Log.e("Home", "text: $news")
            Log.e("Home", "text: ${news.title}")
        }

        binding.recyclerview.adapter = adapter
    }

    private fun openDialog(pos: Int) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alert.setTitle("Delete item")
        //method below is designed so that the user can't close dialog anywhere on the screen
        alert.setCancelable(false)
        alert.setMessage("Are you sure you want to delete this item?")
        alert.setPositiveButton("Yes") { dialog, which ->
            Toast.makeText(
                requireContext(),
                adapter.getItem(pos).title + " item is deleted",
                Toast.LENGTH_SHORT
            ).show()
            adapter.deleteItem(pos)
        }
        alert.setNegativeButton("No") { dialog, which ->
            Toast.makeText(requireContext(), "Operation canceled", Toast.LENGTH_SHORT).show()
        }
        alert.create()
        alert.show()
    }
}