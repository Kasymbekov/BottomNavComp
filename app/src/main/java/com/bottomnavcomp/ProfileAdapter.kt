package com.bottomnavcomp

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottomnavcomp.databinding.ItemProfileBinding
import com.bumptech.glide.Glide

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private val list = arrayListOf(
        "https://www.seekpng.com/png/detail/219-2190977_circle-profile-no-background-png-120dpi-page001-gentleman.png",
        "https://media-s3-us-east-1.ceros.com/orrick/images/2022/03/23/d5f55746bcdf4fe28e5cca49154517cd/sn26811-circular-photo-kelly-erhart-900px.png",
        "https://www.nicepng.com/png/full/182-1829287_cammy-lin-ux-designer-circle-picture-profile-girl.png",
        "https://www.seekpng.com/png/detail/50-503873_facebook-teerasej-profile-ball-circle-facebook-profile-picture.png",
        "https://www.nicepng.com/png/detail/856-8561250_profile-pic-circle-girl.png",
        "https://www.pngitem.com/pimgs/m/348-3481514_circle-profile-girl-hd-png-download.png",
        "https://www.pngkey.com/png/full/934-9343148_2p2a2179-circle-girl.png",
        "https://www.pngitem.com/pimgs/m/128-1284293_marina-circle-girl-picture-in-circle-png-transparent.png",
        "https://lovetobeinthekitchen.com/wp-content/uploads/2015/04/Emily-Circle-Profile-e1428003256512.png",
        "https://media-s3-us-east-1.ceros.com/orrick/images/2022/03/23/d5f55746bcdf4fe28e5cca49154517cd/sn26811-circular-photo-kelly-erhart-900px.png",
        "https://www.coachcarson.com/wp-content/uploads/2018/09/Chad-Profile-pic-circle.png",
        "https://www.pngkey.com/png/full/590-5904853_glen-circle-profile-fundraising.png",
    )

    inner class ViewHolder(private var binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val media = Uri.parse(list[position])
            if (media !== null) {
                Glide.with(binding.root.context)
                    .load(media)
                    .centerCrop()
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.img)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size

}