package com.amartindalonsoc.groover.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.models.Place
import com.amartindalonsoc.groover.ui.main.MapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.place_cell.view.*

class PlacesListAdapter(private val items: List<Place>, private val fragment: MapFragment, private val activity: MainActivity) : RecyclerView.Adapter<PlacesListAdapter.PlaceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesListAdapter.PlaceHolder {
        val inflatedView = parent.inflate(R.layout.place_cell, false)
        return PlaceHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlacesListAdapter.PlaceHolder, position: Int) {
        val place = items[position]
        holder.itemView.setOnClickListener {
            Log.i("place_list", place.displayName)
            fragment.markersMap[place.id]?.showInfoWindow()
            fragment.mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(place.location.latitude, place.location.longitude)))
        }
        holder.itemView.place_cell_playlistPlay.setOnClickListener {
            Log.i("place_list", place.displayName + " - mostrar detalles")
        }
        holder.bindPlace(place, activity)
    }

    class PlaceHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindPlace(place: Place, activity: MainActivity) {
            view.place_cell_bar_name.text = place.displayName
            if (place.mainPlaylist != null) {
                view.place_cell_playlistName.text = place.mainPlaylist.name
                if(place.mainPlaylist.imageUrl != ""){
                    Picasso.get().load(place.mainPlaylist.imageUrl).into(view.place_cell_playlistImage)
                } else {
                    view.place_cell_playlistImage.setImageResource(R.drawable.ic_profile_dark_foreground)
                }
            }
            if (place.similitude != null) {
                Log.i("RECOMMENDED", (place.similitude * 100).toInt().toString() + "%")
                if (place.similitude >= 1.0) {
                    view.place_cell_similitude_percentage.text = "100%"
                } else {
                    view.place_cell_similitude_percentage.text = (place.similitude * 100).toInt().toString() + "%"
                }
                view.place_cell_similitude_playlist.text = " similar to " + activity.lastItemtemForRecommendationUsed!!.playlist!!.name
            }
        }

    }

}