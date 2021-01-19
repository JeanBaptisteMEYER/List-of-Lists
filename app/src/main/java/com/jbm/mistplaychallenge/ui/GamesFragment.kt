package com.jbm.mistplaychallenge.ui

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jbm.mistplaychallenge.R
import com.jbm.mistplaychallenge.tools.Constants
import com.jbm.mistplaychallenge.tools.GameService
import com.squareup.picasso.Picasso

class GamesFragment : Fragment() {

    val TAG = "tag.jbm." + GamesFragment::class.java.simpleName

    private var mIsServiceBound = false
    private var mGameServiceInstance: GameService? = null
    private var mIntentFilter = IntentFilter()

    private lateinit var parentLayout: LinearLayout

    @Override
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_games, container, false)

        //get layout for future updates
        parentLayout = root.findViewById(R.id.game_parent_list)

        //Start Game Service, bind it to our Service Connection class and register our broadcast Receiver
        mIntentFilter.addAction(Constants().mBroadcastGameListUpdate)
        activity?.registerReceiver(this.mBraodcastReceiver, mIntentFilter)

        val gameServiceIntent =  Intent(context, GameService::class.java)
        activity?.startService(gameServiceIntent)
        activity?.bindService(gameServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)

        return root
    }

    @Override
    override fun onDestroy() {
        activity?.unbindService(mServiceConnection)
        activity?.unregisterReceiver(mBraodcastReceiver)

        super.onDestroy()
    }

    /*************************************/

    //This class will handle connection between our service and this fragment
    private val mServiceConnection = object : ServiceConnection {
        @Override
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as GameService.MyBinder
            mGameServiceInstance = binder.service
            mIsServiceBound = true

            // Service started and connected. Lets ask it to parse our data from Json File
            mGameServiceInstance?.parseJsonData()
        }

        @Override
        override fun onServiceDisconnected(name: ComponentName?) {
            mIsServiceBound = false
        }
    }

    // this function will update our UI with the new game list
    fun updateUI() {
        // get list of game from Service
        val gl = mGameServiceInstance?.getGameList()
        var categories: MutableList<String> = mutableListOf()

        //create a list of categories availables
        for (i in 1..gl!!.size.minus(1)) {
            if (!categories.contains(gl[i].category)) {
                categories.add(gl[i].category)
            }
        }

        Log.d(TAG, "Categories are " + categories.toString())

        for (i in 0..categories.size.minus(1)) {
            val listLayout: View =
                layoutInflater.inflate(R.layout.games_horizontal_list, parentLayout, false)

            listLayout.findViewById<TextView>(R.id.game_list_category).text = categories[i]

            val linearLayout2 = listLayout.findViewById<LinearLayout>(R.id.game_card_area)

            for (j in gl) {

                if (j.category.equals(categories[i])) {
                    val gameCardLayout: View =
                        layoutInflater.inflate(R.layout.game_card, linearLayout2, false)

                    //set title
                    gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = j.title

                    //Loading Image from URL
                    Picasso.get()
                        .load(j.img)
                        .resize(200, 250)
                        .centerCrop()
                        .into(gameCardLayout.findViewById<ImageView>(R.id.game_card_img))

                    linearLayout2.addView(gameCardLayout)
                }
            }

            parentLayout.addView(listLayout)
        }

        /*
        for (i in 1..gl!!.size.minus(1)) {

            val gameCardLayout: View = layoutInflater.inflate(R.layout.game_card, listLayout.findViewById(R.id.game_list_category), false)

            //set title
            gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = gl[i].title

            //Loading Image from URL
            Picasso.get()
                .load(gl[i].img)
                .resize(200, 250)
                .centerCrop()
                .into(gameCardLayout.findViewById<ImageView>(R.id.game_card_img))

            listLayout.findViewById<LinearLayout>(R.id.game_list_category).addView(gameCardLayout)
        }
        parentLayout.addView(listLayout)
         */



    }

    //braodcast reveiver for communication between Services and this fragment
    private val mBraodcastReceiver = object : BroadcastReceiver() {
        @Override
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action) {
                Constants().mBroadcastGameListUpdate -> updateUI()
            }
        }
    }
}