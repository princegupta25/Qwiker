package com.hk.socialmediaapp.post.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.compose.runtime.snapshots.SnapshotApplyResult.Success.check
import com.bumptech.glide.Glide
import com.hk.socialmediaapp.R



class Adapter(
    private val context: Context,
    private val images: List<String>,
//    private var photoListener: PhotoListener
    val listener: (String) -> Unit

) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_grid_imageview, parent, false)
        )
    }

    var prevholder: ViewHolder? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        if(prevholder==null && position==0){
            prevholder = holder
        }
        Glide.with(context)
            .load(image)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            if(holder.check.visibility == View.VISIBLE){
            }else{
                holder.check.visibility = View.VISIBLE
                prevholder!!.check.visibility = View.INVISIBLE
            }
            prevholder = holder
            listener(image)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.gridImageView)
        var check: ImageView = itemView.findViewById(R.id.check)
    }
}