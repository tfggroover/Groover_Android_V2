package com.amartindalonsoc.groover.ui.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {
    lateinit var profileFragmentContext: Context

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

        logoutButton.setOnClickListener {
            val logoutPopup = AlertDialog.Builder(activity!!)
            logoutPopup.setTitle("Logout")
            logoutPopup.setMessage("Are you sure you want to log out?")
            logoutPopup.setPositiveButton("Yes"){ dialog, which ->
                SharedPreferencesManager.logout(profileFragmentContext)
                Utils.startLoginActivity(activity!!)
            }
            logoutPopup.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            logoutPopup.show()
        }

    }
}

