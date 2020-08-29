package com.amartindalonsoc.groover.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.ui.main.PlaylistDetailsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_cell.view.*
import java.util.Collections.copy
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

class UserPlaylistsAdapter(private val items: List<ItemForRecommendation>, private val activity: MainActivity) : RecyclerView.Adapter<UserPlaylistsAdapter.PlaylistHolder>() {


    var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPlaylistsAdapter.PlaylistHolder {
        val inflatedView = parent.inflate(R.layout.playlist_cell, false)
        return PlaylistHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserPlaylistsAdapter.PlaylistHolder, position: Int) {
        val playlist = items[position]
        if (activity.selectedItem == position) {
            holder.view.setBackgroundColor(activity.getColor(R.color.selected_playlist))
        } else {
            holder.view.setBackgroundColor(0x00000000)
        }
        holder.view.setOnClickListener {
            lastPosition = activity.selectedItem
            activity.selectedItem = position
            if (items[position].isPlaylist) {
                activity.itemForRecommendation = items[position]
            } else {
                activity.itemForRecommendation = ItemForRecommendation(false, null)
            }
            notifyItemChanged(lastPosition)
            notifyItemChanged(activity.selectedItem)
        }
        holder.bindPlaylist(playlist)
    }


    class PlaylistHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var view: View = v
        var item: ItemForRecommendation? = null

        init {
            v.setOnClickListener(this)
        }

        @SuppressLint("ResourceAsColor")
        override fun onClick(v: View) {
//            v.setBackgroundColor(R.color.selected_playlist)
            Log.d("RecyclerView", "User playlist adapter")
        }

        fun bindPlaylist(item: ItemForRecommendation) { // Pasar esto a playlist
            this.item = item
            if (item.isPlaylist && item.playlist != null) {
                view.playlist_cell_title.text = item.playlist.name
                if(item.playlist.images.isNotEmpty() && item.playlist.images.first().url != ""){
                    Picasso.get().load(item.playlist.images.first().url).into(view.playlist_cell_image)
                } else {
                    view.playlist_cell_image.setImageResource(R.drawable.ic_profile_dark_foreground)
                }
//                view.playlist_cell_title.setOnClickListener {
//                    activity.itemForRecommendation = item
//                }

                view.playlist_cell_details_button.setOnClickListener {
                    val fragment = PlaylistDetailsFragment()
                    fragment.item = item
                    (view.context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                }
            } else if (!item.isPlaylist) {
                view.playlist_cell_title.text = "Top 50 tracks" //TODO Pasarlo a strings
                view.playlist_cell_image.setImageResource(R.drawable.ic_profile_dark_foreground) //TODO Cambiar por una imagen representativa de un Top50

                view.playlist_cell_details_button.setOnClickListener {
                    val fragment = PlaylistDetailsFragment()
                    fragment.item = item
                    (view.context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                }
            }
            // TODO Meter el boton de play, y su funcionalidad dependiendo de si es premium o no
        }

    }
}