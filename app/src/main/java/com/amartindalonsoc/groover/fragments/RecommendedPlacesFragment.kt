package com.amartindalonsoc.groover.ui.main

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.playlist_fragment.*
import kotlinx.android.synthetic.main.recommended_places.*
import org.json.JSONObject
import kotlin.math.round

class RecommendedPlacesFragment: Fragment() {
//
//    //lateinit var globalVars :GlobalVars
//    //lateinit var storedUser: StoredUser
//
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        //globalVars  = activity!!.applicationContext as GlobalVars
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.recommended_places, null)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        GlobalVars.currentFragment = "recommendedPlaces"
//
//        val recommendedPlacesMap = GlobalVars.recommendedPlaces.toList().sortedBy { (_, value) -> value }.reversed().toMap()
//
//        var placesShown = 0
//
//
//        for (place in recommendedPlacesMap){
//
//
//            val placeLayout = LinearLayout(context)
//            placeLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            placeLayout.orientation = LinearLayout.VERTICAL
//
//
//            val barNameLayout = LinearLayout(context)
//            val barNameLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(30.0))
//            barNameLayout.orientation = LinearLayout.HORIZONTAL
//            barNameLayout.layoutParams = barNameLayoutParams
//
//            val barNameTextView = TextView(context)
//            val barNameTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dpToPx(30.0))
//            barNameTextView.layoutParams = barNameTextViewParams
//            barNameTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//            barNameTextView.marqueeRepeatLimit = -1
//            barNameTextView.setSingleLine(true)
//            barNameTextView.isSelected = true
//            barNameTextView.typeface = Typeface.DEFAULT_BOLD
//            barNameTextView.gravity = Gravity.CENTER_VERTICAL
//            barNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
//            barNameTextView.text = place.key.displayname
//
//            val blankView1 = View(context)
//            val blankViewParams = LinearLayout.LayoutParams(0,0,1f)
//            blankView1.layoutParams = blankViewParams
//
//            val expandInfoImage = ImageView(context)
//            expandInfoImage.layoutParams = LinearLayout.LayoutParams(dpToPx(30.0), dpToPx(30.0))
//            expandInfoImage.setImageResource(R.drawable.ic_expand_more)
//            expandInfoImage.background = ContextCompat.getDrawable(activity!!.applicationContext,R.drawable.border)
//
//            barNameLayout.addView(barNameTextView)
//            barNameLayout.addView(blankView1)
//            barNameLayout.addView(expandInfoImage)
//
//
//            val ratingLayout = LinearLayout(context)
//            val ratingLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            ratingLayout.orientation = LinearLayout.HORIZONTAL
//            ratingLayoutParams.setMargins(0,dpToPx(5.0),0,0)
//            ratingLayout.layoutParams = ratingLayoutParams
//
//            val ratingBar = RatingBar(context, null, android.R.attr.ratingBarStyleSmall)
//            ratingBar.numStars = 5
//            ratingBar.rating = place.key.rating.toFloat()
//            ratingBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//            val ratingText = TextView(context)
//            ratingText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            ratingText.text = place.key.rating.toString()
//
//            ratingLayout.addView(ratingBar)
//            ratingLayout.addView(ratingText)
//
//
//            val separatorView1 = View(context)
//            separatorView1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(0.5))
//            separatorView1.setBackgroundColor(ContextCompat.getColor(activity!!.applicationContext,R.color.separatorView))
//
//
//            val playlistLayout = LinearLayout(context)
//            val playlistLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            playlistLayout.orientation = LinearLayout.HORIZONTAL
//            playlistLayoutParams.setMargins(0,dpToPx(5.0),0,dpToPx(5.0))
//            playlistLayout.layoutParams = ratingLayoutParams
//
//            val playlistImage = ImageView(context)
//            playlistImage.layoutParams = LinearLayout.LayoutParams(dpToPx(60.0), dpToPx(60.0))
//
//            val playlistText = TextView(context)
//            val playlistTextParams = LinearLayout.LayoutParams(dpToPx(230.0),LinearLayout.LayoutParams.MATCH_PARENT)
//            playlistTextParams.setMargins(dpToPx(10.0), 0,0,0)
//            playlistText.layoutParams = playlistTextParams
//            playlistText.ellipsize = TextUtils.TruncateAt.MARQUEE
//            playlistText.marqueeRepeatLimit = -1
//            playlistText.setSingleLine(true)
//            playlistText.isSelected = true
//            playlistText.typeface = Typeface.DEFAULT_BOLD
//            playlistText.gravity = Gravity.CENTER_VERTICAL
//            playlistText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//
//            val blankView2 = View(context)
//            blankView2.layoutParams = blankViewParams
//
//            val playlistPlayImage = ImageView(context)
//            playlistPlayImage.layoutParams = LinearLayout.LayoutParams(dpToPx(60.0), dpToPx(60.0))
//
//            playlistLayout.addView(playlistImage)
//            playlistLayout.addView(playlistText)
//            playlistLayout.addView(blankView2)
//            playlistLayout.addView(playlistPlayImage)
//
//            val playlistRequest = object : JsonObjectRequest(
//                Request.Method.GET,
//                "https://api.spotify.com/v1/playlists/" + place.key.playlist + "?fields=name%2Cimages%2Ctracks.items(track(name%2Chref%2Curi%2Cartists(name)%2Calbum(name%2Cimages)))",
//                null,
//                Response.Listener<JSONObject> { jsonResponse ->
//
//                    playlistText.text = jsonResponse.get("name").toString()
//                    Picasso.get().load(
//                        JSONObject(jsonResponse.getJSONArray("images")[0].toString()).get(
//                            "url"
//                        ).toString()
//                    ).into(playlistImage)
//
//                },
//                Response.ErrorListener { error -> }) {
//
//                override fun getHeaders(): MutableMap<String, String> {
//                    val requestHeader: MutableMap<String, String> = hashMapOf()
//                    requestHeader.put("Accept", "application/json")
//                    requestHeader.put("Content-Type", "application/json")
//                    requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                    return requestHeader
//                }
//            }
//
//            if(place.key.playlist["id"] != "") {
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//                if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                    playlistPlayImage.setImageResource(R.drawable.ic_play_foreground)
//                    playlistPlayImage.setOnClickListener {
//                        GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                        GlobalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + place.key.playlist)
//                    }
//                }
//            }
//
//
//            val expandedInfoLayout = LinearLayout(context)
//            val expandedInfoLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//            expandedInfoLayoutParams.setMargins(0,dpToPx(5.0),0,0)
//            expandedInfoLayout.layoutParams = expandedInfoLayoutParams
//            expandedInfoLayout.orientation = LinearLayout.VERTICAL
//
//            val separatorView2 = View(context)
//            val separatorView2Params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(0.5))
//            separatorView2Params.setMargins(0,0,0,dpToPx(5.0))
//            separatorView2.layoutParams = separatorView2Params
//            separatorView2.setBackgroundColor(ContextCompat.getColor(activity!!.applicationContext,R.color.separatorView))
//
//            val addressText = TextView(context)
//            val addressTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//            addressTextParams.setMargins(dpToPx(8.0), 0,0,0)
//            addressText.layoutParams = addressTextParams
//            addressText.ellipsize = TextUtils.TruncateAt.MARQUEE
//            addressText.marqueeRepeatLimit = -1
//            addressText.setSingleLine(true)
//            addressText.isSelected = true
//            addressText.gravity = Gravity.CENTER_VERTICAL
//            addressText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//            addressText.text = place.key.address
//            addressText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_location_on, 0,0,0)
//            addressText.compoundDrawablePadding = dpToPx(5.0)
//
//            val phoneText = TextView(context)
//            val phoneTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//            phoneTextParams.setMargins(dpToPx(8.0), dpToPx(8.0),0,0)
//            phoneText.layoutParams = phoneTextParams
//            phoneText.ellipsize = TextUtils.TruncateAt.MARQUEE
//            phoneText.marqueeRepeatLimit = -1
//            phoneText.setSingleLine(true)
//            phoneText.isSelected = true
//            phoneText.gravity = Gravity.CENTER_VERTICAL
//            phoneText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//            phoneText.text = place.key.phone
//            phoneText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_local_phone, 0,0,0)
//            phoneText.compoundDrawablePadding = dpToPx(5.0)
//
//            val openText = TextView(context)
//            val aopenTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//            aopenTextParams.setMargins(dpToPx(8.0), 0,0,dpToPx(16.0))
//            openText.layoutParams = aopenTextParams
//            openText.gravity = Gravity.CENTER_VERTICAL
//            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//            openText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_access_time, 0,0,0)
//            openText.compoundDrawablePadding = dpToPx(5.0)
//            openText.text = getString(R.string.open_times,
//                timetableDayText(place.key,"monday"),
//                timetableDayText(place.key,"tuesday"),
//                timetableDayText(place.key,"wednesday"),
//                timetableDayText(place.key,"thursday"),
//                timetableDayText(place.key,"friday"),
//                timetableDayText(place.key,"saturday"),
//                timetableDayText(place.key,"sunday"))
//
//            expandedInfoLayout.addView(separatorView2)
//            expandedInfoLayout.addView(addressText)
//            expandedInfoLayout.addView(phoneText)
//            expandedInfoLayout.addView(openText)
//
//            var expandedInfo = false
//
//            expandInfoImage.setOnClickListener {
//                if (expandedInfo){
//                    placeLayout.removeView(expandedInfoLayout)
//                    expandInfoImage.setImageResource(R.drawable.ic_expand_more)
//                    expandedInfo = false
//                }else{
//                    placeLayout.addView(expandedInfoLayout)
//                    expandInfoImage.setImageResource(R.drawable.ic_expand_less)
//                    expandedInfo = true
//                }
//            }
//
//
//            placeLayout.addView(barNameLayout)
//            placeLayout.addView(ratingLayout)
//            placeLayout.addView(separatorView1)
//            placeLayout.addView(playlistLayout)
//
//            val separatorView3 = View(context)
//            val separatorView3Params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(1.5))
//            separatorView3Params.setMargins(0, dpToPx(8.0),0, dpToPx(8.0))
//            separatorView3.layoutParams = separatorView3Params
//            separatorView3.setBackgroundColor(ContextCompat.getColor(activity!!.applicationContext,R.color.black))
//
//            if (placesShown < 3){
//                recommendedPlaces.addView(placeLayout)
//                recommendedPlaces.addView(separatorView3)
//                placesShown += 1
//            }
//
//        }
//
//    }
//
//
//    fun timetableDayText(place: Place, day: String): String {
//        var dayText = ""
//
//        if (place.timetables.containsKey(day)){
//            val dayOpeningMap = place.timetables[day]
//            val dayOpeningKeys = dayOpeningMap?.keys?.sorted()
//
//            if (dayOpeningKeys != null){
//                for (startTime in dayOpeningKeys){
//                    if (startTime == "Closed"){
//                        dayText = "Closed"
//                    }else{
//                        dayText = dayText.plus(startTime + " - " + dayOpeningMap?.get(startTime) + " and ")
//                    }
//                }
//                if (dayText.takeLast(5) == " and "){
//                    dayText = dayText.dropLast(5)
//                }
//            }else{
//                dayText = "Closed"
//            }
//        }else{
//            dayText = "Closed"
//        }
//        return dayText
//    }
//
//
//
//
//    ///////////////////////////////////////////////
//    fun dpToPx(dp: Double): Int {
//        val density = this.resources.displayMetrics.density
//        return round(dp * density).toInt()
//    }

}