package com.amartindalonsoc.groover.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.amartindalonsoc.groover.ui.main.PlaylistFragmentOfPlace
import com.amartindalonsoc.groover.ui.main.RecognizedFragmentOfPlace

class PlaylistTypeInPlacePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PlaylistFragmentOfPlace()
            }
            else -> {
                return RecognizedFragmentOfPlace()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Official"
            else -> {
                return "Recognized"
            }
        }
    }
}