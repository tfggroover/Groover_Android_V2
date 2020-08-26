package com.amartindalonsoc.groover.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.song_cell.view.*

class PlaylistDetailsAdapter(private val tracks: List<Track>, private val activity: MainActivity) : RecyclerView.Adapter<PlaylistDetailsAdapter.TrackHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDetailsAdapter.TrackHolder {
        val inflatedView = parent.inflate(R.layout.song_cell, false)
        return TrackHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: PlaylistDetailsAdapter.TrackHolder, position: Int) {
        val track = tracks[position]
        holder.bindPlaylist(track, activity)
    }


    class TrackHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var track: Track? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun bindPlaylist(track: Track, activity: MainActivity) { // Pasar esto a playlist
            this.track = track
            view.song_cell_title.text = track.name

            if (!activity.spotifyAccountType.contentEquals("premium")) {
                view.song_cell_play_button.setImageDrawable(null)
            } else {
                view.song_cell_play_button.setOnClickListener {
                    activity.spotifyAppRemote.connectApi.connectSwitchToLocalDevice()
                    activity.spotifyAppRemote.playerApi.play(track.uri)
                }
            }
            val artists = track.artists
            if (artists != null && artists.isNotEmpty()) {
                var recognizedSongArtist = artists.first().name
                if (artists.size > 1) {
                    for (i in 1 until artists.size) {
                        recognizedSongArtist = recognizedSongArtist.plus(", " + artists[i].name)
                    }
                }
                view.song_cell_artist.text = recognizedSongArtist
            } else {
                view.song_cell_artist.text = "Unknown"
            }

            // TODO Meter el boton de play, y su funcionalidad dependiendo de si es premium o no
        }

    }
}