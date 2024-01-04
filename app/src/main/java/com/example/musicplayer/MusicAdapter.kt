package com.example.musicplayer

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.MusicItemBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File

class MusicAdapter(private val context: Context, private val musicFiles: ArrayList<MusicFiles>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val album_art = binding.musicImg
        val file_name = binding.musicFileName
        val menuMore = binding.menuMore


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
        val model = musicFiles[position]
        holder.file_name.text = model.title
        val image = getAlbumArt(musicFiles.get(position).path)
        if (image != null) {

            Glide.with(context).asBitmap().load(image).into(holder.album_art)
        } else {
            Glide.with(context).asBitmap().load(R.drawable.img).into(holder.album_art)

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
        holder.menuMore.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.delete -> {
                        Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show()
                        deleteFile(position, it)

                    }

                    else -> Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }
                true


            }
        }

    }

    private fun deleteFile(position: Int, it: View) {
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            musicFiles.get(position).id.toLong()
        )
        val file = File(musicFiles.get(position).path)
        val deleted = file.delete()
        if (deleted) {
            context.contentResolver.delete(contentUri,null,null)
            musicFiles.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, musicFiles.size)
            Snackbar.make(it, "File Deleted", Snackbar.LENGTH_LONG).show()

        }else{
            Snackbar.make(it, "Can't be Deleted", Snackbar.LENGTH_LONG).show()

        }

    }

    override fun getItemCount(): Int {
        return musicFiles.size
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art;

    }

}