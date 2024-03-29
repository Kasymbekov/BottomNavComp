package com.bottomnavcomp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bottomnavcomp.App
import com.bottomnavcomp.R
import com.bottomnavcomp.databinding.FragmentHomeBinding
import com.bottomnavcomp.models.News
import com.bottomnavcomp.printErrorLog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = NewsAdapter() {

        }
        adapter.onLongClick = {
            //AlertDialog implementation
            openDialog(it)
        }
        val list = App.database.newsDao().getAll()
        adapter.addItems(list)
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
        ) { _, bundle ->
            val news = bundle.getSerializable("news") as News

            //adapter.addItem(news)
            Log.e("Home", "text: $news")
            Log.e("Home", "text: ${news.title}")
        }

        binding.recyclerview.adapter = adapter

        //EditText listener
        binding.editText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val list = App.database.newsDao().getAll()

                if (binding.editText.text.isEmpty()) {
                    adapter.clearList()
                    adapter.addItems(list)
                } else {
                    adapter.clearList()
                    for (n in list) {
                        if (n.title.contains(binding.editText.text)) {
                            adapter.addItem(n)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun openDialog(pos: Int) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        alert.setTitle("Delete item")
        //method below is designed so that the user can't close dialog anywhere on the screen
        alert.setCancelable(false)
        alert.setMessage("Are you sure you want to delete this item?")
        alert.setPositiveButton("Yes") { _, _ ->
            Toast.makeText(
                requireContext(),
                adapter.getItem(pos).title + " item is deleted",
                Toast.LENGTH_SHORT
            ).show()
            adapter.deleteItem(pos)
        }
        alert.setNegativeButton("No") { _, _ ->
            Toast.makeText(requireContext(), "Operation canceled", Toast.LENGTH_SHORT).show()
        }
        alert.create()
        alert.show()
    }
}