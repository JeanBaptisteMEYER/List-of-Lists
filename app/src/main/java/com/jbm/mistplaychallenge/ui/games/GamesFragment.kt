package com.jbm.mistplaychallenge.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jbm.mistplaychallenge.R
import com.squareup.picasso.Picasso

class GamesFragment : Fragment() {

    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gamesViewModel =
                ViewModelProvider(this).get(GamesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_games, container, false)

        //building the horizontal scrolling list of game
        val ll : LinearLayout = root.findViewById(R.id.game_card_area)

        for (i in 1..10) {
            val gameCardLayout: View = layoutInflater.inflate(R.layout.game_card, ll, false)
            gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = "coucou" + i

            //Loading Image from URL
            Picasso.get()
                .load("https://mistplay-static.s3.amazonaws.com/images/timeplay/games/best-fiends/unnamed.jpg")
                .into(gameCardLayout.findViewById<ImageView>(R.id.game_card_img))
            ll.addView(gameCardLayout)
        }

        return root
    }
}