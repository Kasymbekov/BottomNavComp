package com.bottomnavcomp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottomnavcomp.databinding.PagerBoardBinding
import com.bottomnavcomp.models.IntroPage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


class BoardAdapter(private val onClickStart: () -> Unit) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    private val pages = arrayOf(
        IntroPage("Welcome", "to our page.", R.drawable.img1),
        IntroPage("Салам", "биздин баракчага кош келиңиз.", R.drawable.img),
        IntroPage("Добро пожаловать", "на нашу страницу.", R.drawable.img2)
    )

    inner class ViewHolder(private var binding: PagerBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.textTitle.text = pages[position].title
            binding.textDesc.text = pages[position].desc
            binding.imageView.setImageResource(pages[position].img)

            if (position == pages.size - 1)
                binding.btnStart.visibility = View.VISIBLE
            else
                binding.btnStart.visibility = View.INVISIBLE

            binding.btnStart.setOnClickListener {
                onClickStart.invoke()
            }
        }

        //need to fix
//        @BindingAdapter("profileImage")
//        fun loadImage(view: ImageView, imageUrl: String?) {
//            Glide.with(binding.root)
//                .load(imageUrl).apply(RequestOptions().circleCrop())
//                .into(view)
//        }
        //-----------------------------

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PagerBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = pages.size

}