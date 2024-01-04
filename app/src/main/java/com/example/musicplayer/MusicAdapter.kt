package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.MusicItemBinding

class MusicAdapter(private val context: Context,private val musicFiles: ArrayList<MusicFiles>) :RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val album_art = binding.musicImg
        val file_name = binding.musicFileName


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.ViewHolder {
        return ViewHolder(
            MusicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicAdapter.ViewHolder, position: Int) {
        val model=musicFiles[position]
        holder.file_name.text=model.title
        val image=getAlbumArt(musicFiles.get(position).path)
        if (image!=null){

            Glide.with(context).asBitmap().load(image).into(holder.album_art)
        }else{
            Glide.with(context).asBitmap().load(R.drawable.img).into(holder.album_art)

        }
        holder.itemView.setOnClickListener {
            val  intent=Intent(context,PlayerActivity::class.java)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return musicFiles.size
    }
    private fun getAlbumArt(uri:String): ByteArray? {
        val retriever=MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art=retriever.embeddedPicture
        retriever.release()
        return  art;

    }

}