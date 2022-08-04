package com.bottomnavcomp.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottomnavcomp.App
import com.bottomnavcomp.BoardAdapter
import com.bottomnavcomp.databinding.ItemNewsBinding
import com.bottomnavcomp.models.News
import com.bottomnavcomp.printErrorLog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val onClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val list = arrayListOf<News>()

    var onLongClick: ((position: Int) -> Unit)? = null

    inner class ViewHolder(private var binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.textTitle.text = news.title

            //Format created news time into a necessary view
            var formatter = SimpleDateFormat("hh:mm, dd MMM yyyy");
            var dateString = formatter.format(Date(news.createdAt))
            binding.createdTime.text = dateString

            binding.root.setOnClickListener {
                onClick.invoke(adapterPosition)
            }
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(adapterPosition)
                return@setOnLongClickListener true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addItem(news: News) {
        list.add(0, news)

        //gives some insert animation
        notifyItemInserted(0)
    }

    fun addItems(list: List<News>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): News {
        return list[pos]
    }

    fun deleteItem(pos: Int) {
        //delete News from Room
        App.database.newsDao().delete(list[pos])

        //remove News from adapter list
        list.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun clearList() {
        this.list.clear()
    }

    fun checkList(): Boolean {
        return list.isEmpty()
    }

    fun getList(): ArrayList<News> {
        return this.list
    }
}