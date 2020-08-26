package com.amartindalonsoc.groover.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.models.Song
import kotlinx.android.synthetic.main.song_cell.view.*

class MainPlaylistAdapter(private val songs: List<Song>, private val activity: MainActivity) : RecyclerView.Adapter<MainPlaylistAdapter.SongHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPlaylistAdapter.SongHolder {
        val inflatedView = parent.inflate(R.layout.song_cell, false)
        return SongHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: MainPlaylistAdapter.SongHolder, position: Int) {
        val song = songs[position]
        holder.bindSong(song, activity)
    }


    //1
    class SongHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var song: Song? = null

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            //5
            private val PHOTO_KEY = "PHOTO"
        }

        fun bindSong(song: Song, activity: MainActivity) {
            this.song = song
            view.song_cell_title.text = song.name // TODO Meter el boton de play, y su funcionalidad dependiendo de si es premium o no
            if (!activity.spotifyAccountType.contentEquals("premium")) {
                view.song_cell_play_button.setImageDrawable(null)
            } else {
                view.song_cell_play_button.setOnClickListener {
                    activity.spotifyAppRemote.connectApi.connectSwitchToLocalDevice()
                    activity.spotifyAppRemote.playerApi.play("spotify:track:" + song.id)
                }
            }
            val artists = song.artists
            var recognizedSongArtist = artists.first().name
            if (artists.size > 1) {
                for (i in 1 until artists.size) {
                    recognizedSongArtist = recognizedSongArtist.plus(", " + artists[i].name)
                }
            }
            view.song_cell_artist.text = recognizedSongArtist
        }

    }

}
