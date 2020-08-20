package com.amartindalonsoc.groover.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_cell.view.*

class UserPlaylistsAdapter(private val items: List<ItemForRecommendation>) : RecyclerView.Adapter<UserPlaylistsAdapter.PlaylistHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPlaylistsAdapter.PlaylistHolder {
        val inflatedView = parent.inflate(R.layout.playlist_cell, false)
        return PlaylistHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserPlaylistsAdapter.PlaylistHolder, position: Int) {
        val playlist = items[position]
        holder.bindPlaylist(playlist)
    }


    class PlaylistHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var item: ItemForRecommendation? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun bindPlaylist(item: ItemForRecommendation) { // Pasar esto a playlist
            this.item = item
            if (item.isPlaylist && item.playlist != null) {
                view.playlist_cell_title.text = item.playlist.name
                if(item.playlist.images.first().url != ""){
                    Picasso.get().load(item.playlist.images.first().url).into(view.playlist_cell_image)
                } else {
                    view.playlist_cell_image.setImageResource(R.drawable.ic_profile_dark_foreground)
                }
            } else if (!item.isPlaylist) {
                view.playlist_cell_title.text = "Top 50 tracks" //TODO Pasarlo a strings
                view.playlist_cell_image.setImageResource(R.drawable.ic_profile_dark_foreground) //TODO Cambiar por una imagen representativa de un Top50
            }
            // TODO Meter el boton de play, y su funcionalidad dependiendo de si es premium o no
        }

    }
}