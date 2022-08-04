package com.bottomnavcomp.ui.dashboard

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bottomnavcomp.R
import com.bottomnavcomp.databinding.FragmentDashboardBinding
import com.bottomnavcomp.models.News
import com.bottomnavcomp.printErrorLog
import com.bottomnavcomp.ui.home.NewsAdapter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var adapter: NewsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Init adapter
        adapter = NewsAdapter {

        }

        //Adapter item listener
        adapter.onLongClick = {
            showDeleteAlertDialog(requireContext(), adapter, it)
        }
    }

    //AlertDialog implementation, request for deletion
    private fun showDeleteAlertDialog(context: Context, adapter: NewsAdapter, pos: Int) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(context)

        alert.setTitle("Delete item")
        //method below is designed so that the user can't close dialog anywhere on the screen
        alert.setCancelable(false)
        alert.setMessage("Are you sure you want to delete this item?")
        alert.setPositiveButton("Yes") { _, _ ->
            Toast.makeText(
                context,
                adapter.getItem(pos).title + " is deleted ",
                Toast.LENGTH_SHORT
            ).show()
            findTokenDocumentByValue("createdAt", adapter.getItem(pos).createdAt, pos)
        }
        alert.setNegativeButton("No") { _, _ ->
            Toast.makeText(context, "Deletion canceled", Toast.LENGTH_SHORT).show()
        }
        alert.create()
        alert.show()
    }

    //get token of the document by field and value
    private fun findTokenDocumentByValue(field: String, value: Any, pos: Int): String {
        var token = ""

        Firebase.firestore
            .collection("news")
            .whereEqualTo(field, value)
            .get()
            .addOnSuccessListener {
                token = it.documents[0].id
                printErrorLog("Selected item's token is $token")
                deleteDocument(token, pos)
            }
            .addOnFailureListener {
                printErrorLog("Error getting documents.")
            }

        printErrorLog("Value is $token")
        return token

        //вопрос по поводу возвращения токена, почему return выполняется раньше чем метод addOnSuccessListener. Может надо приостановить поток.
        //хотел добавить возвращаемый метод findTokenDocumentByValue в качестве аргумента в метод deleteDocument
    }

    //delete document from the Firestore
    private fun deleteDocument(id: String, pos: Int) {
        Firebase.firestore
            .collection("news")
            .document(id)
            .delete()
            .addOnSuccessListener {
                printErrorLog("Successfully deleted!")

                //remove News from adapter list
                adapter.getList().removeAt(pos)
                adapter.notifyItemRemoved(pos)
            }
            .addOnFailureListener {
                printErrorLog("Deletion error.")
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.adapter = adapter
        getData()
        toAnimate()
    }

    private fun toAnimate() {
        binding.animationView.setAnimation(R.raw.progress)

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator
            .addUpdateListener { animation: ValueAnimator ->
                binding.animationView.progress = animation.animatedValue as Float
            }
        animator.start()
    }

    private fun getData() {
        Firebase.firestore
            .collection("news")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val list = it.toObjects(News::class.java)
                adapter.addItems(list)
                binding.animationView.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.w("UNIQUE", "Error getting documents.", exception)
            }
    }
}