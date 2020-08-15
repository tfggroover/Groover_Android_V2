package com.amartindalonsoc.groover.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.spotify.protocol.types.Track
import com.squareup.picasso.Picasso
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

class ProfileFragment: Fragment() {
    //lateinit var globalVars :GlobalVars
    //lateinit var storedUser: StoredUser
    lateinit var profileFragmentContext: Context
    var distance = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //globalVars  = activity!!.applicationContext as GlobalVars
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragmentContext = activity!!.applicationContext

        val userName = SharedPreferencesManager.getString(Constants.user_name, profileFragmentContext)!!
        val userEmail = SharedPreferencesManager.getString(Constants.user_email, profileFragmentContext)!!
        val userImage = SharedPreferencesManager.getString(Constants.profile_image, profileFragmentContext)!!

        profileName.text = userName
        profileEmail.text = userEmail
        if(userImage != ""){
            Picasso.get().load(userImage).into(profileImage)
        }else{
            profileImage.setBackgroundResource(R.drawable.ic_profile_dark_foreground)
        }
//        var savedDistance = SharedPreferencesManager.getFloat(Constants.selected_distance_to_place, profileFragmentContext)!!.toInt()
//        if (savedDistance < 0) { savedDistance = 0 }
//        profilePlaceDistanceTextView.text = savedDistance.toString() + "m"
//        profilePlaceDistanceBar.progress = savedDistance
//
//        profilePlaceDistanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                distance = ((p1 + 99) / 100 ) * 100
//                profilePlaceDistanceTextView.text = "$distance m"
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//                SharedPreferencesManager.saveFloat(Constants.selected_distance_to_place, distance.toFloat(), profileFragmentContext)
//            }
//
//        })

        //TODO Modificar logout para que borre todos los datos guardados en SharedPreferences
        logoutButton.setOnClickListener {
            val logoutPopup = AlertDialog.Builder(activity!!)
            logoutPopup.setTitle("Logout")
            logoutPopup.setMessage("To be able to logout click on \"Not you?\" in the Spotify prompt.")
            logoutPopup.setPositiveButton("Continue"){ dialog, which ->
                /*val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.spotify.com/es/account/apps/")
                startActivity(openURL)
                activity!!.finish()*/
            }
            logoutPopup.show()
        }

    }
}

