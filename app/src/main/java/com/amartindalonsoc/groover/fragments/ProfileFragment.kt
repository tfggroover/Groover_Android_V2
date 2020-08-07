package com.amartindalonsoc.groover.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

class ProfileFragment: Fragment() {
    //lateinit var globalVars :GlobalVars
    //lateinit var storedUser: StoredUser
    private val clientId = "8a16994e018b4e2495bdc78b1ba81ad1"
    private val redirectUri = "https://tfggroover.azurewebsites.net/home/callback"
    private val request_code_original = 1234

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
//        GlobalVars.currentFragment = "profile"


//        profileName.text = StoredUser.spotifyUser.display_name
//        profileEmail.text = StoredUser.spotifyUser.email
//        if(StoredUser.spotifyUser.profileImage != ""){
//            Picasso.get().load(StoredUser.spotifyUser.profileImage).into(profileImage)
//        }else{
//            profileImage.setBackgroundResource(R.drawable.ic_profile_dark_foreground)
//        }


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