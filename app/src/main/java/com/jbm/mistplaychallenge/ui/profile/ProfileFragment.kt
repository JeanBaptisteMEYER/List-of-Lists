package com.jbm.mistplaychallenge.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jbm.mistplaychallenge.R

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        //building the horizontal scrolling list of game
        val ll : LinearLayout = root.findViewById(R.id.game_card_area)

        for (i in 1..10) {
            val gameCardLayout: View = layoutInflater.inflate(R.layout.game_card, ll, false)
            gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = "coucou"
            ll.addView(gameCardLayout)
        }

        return root
    }
}