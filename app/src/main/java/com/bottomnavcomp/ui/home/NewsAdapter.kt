package com.bottomnavcomp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottomnavcomp.databinding.ItemNewsBinding
import com.bottomnavcomp.models.News
import java.text.SimpleDateFormat
import java.util.*

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

        //gives some inserting animation
        notifyItemInserted(0)
    }

    fun getItem(pos: Int): News {
        return list[pos]
    }

    fun deleteItem(pos: Int) {
        list.removeAt(pos)

        notifyItemRemoved(pos)
    }

}