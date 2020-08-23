package com.amartindalonsoc.groover.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.ui.main.PlaylistDetailsFragment
import com.spotify.protocol.types.Track
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_cell.view.*
import kotlinx.android.synthetic.main.song_cell.view.*

class PlaylistDetailsAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<PlaylistDetailsAdapter.TrackHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDetailsAdapter.TrackHolder {
        val inflatedView = parent.inflate(R.layout.song_cell, false)
        return TrackHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: PlaylistDetailsAdapter.TrackHolder, position: Int) {
        val track = tracks[position]
        holder.bindPlaylist(track)
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

        fun bindPlaylist(track: Track) { // Pasar esto a playlist
            this.track = track
            view.song_cell_title.text = track.name

            val artists = track.artists
            var recognizedSongArtist = artists.first().name
            if (artists.size > 1) {
                for (i in 1 until artists.size) {
                    recognizedSongArtist = recognizedSongArtist.plus(", " + artists[i].name)
                }
            }
            view.song_cell_artist.text = recognizedSongArtist

            // TODO Meter el boton de play, y su funcionalidad dependiendo de si es premium o no
        }

    }
}