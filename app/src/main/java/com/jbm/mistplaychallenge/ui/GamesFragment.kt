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
        //This will allowed us to have the service and the fragment communicate in an asynchronous way
        mIntentFilter.addAction(Constants().mBroadcastGameListUpdate)
        activity?.registerReceiver(this.mBraodcastReceiver, mIntentFilter)

        val gameServiceIntent =  Intent(context, GameService::class.java)
        activity?.startService(gameServiceIntent)
        activity?.bindService(gameServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)

        return root
    }

    @Override
    override fun onDestroy() {
        //onDestroy, so must unbind and unregister our Service
        activity?.unbindService(mServiceConnection)
        activity?.unregisterReceiver(mBraodcastReceiver)

        super.onDestroy()
    }

    /*************************************/

    // this function will update our UI with the new game list
    //it will get called when the service notifyed this fragment that the game list have been updated
    fun updateUI() {
        // get list of game from Service
        val gl = mGameServiceInstance?.getGameList()
        var categories: MutableList<String> = mutableListOf()

        //create a list of categories
        for (i in 1..gl!!.size.minus(1)) {
            if (!categories.contains(gl[i].category)) {
                categories.add(gl[i].category)
            }
        }

        //then for each category...
        for (i in 0..categories.size.minus(1)) {

            //...inflate a category view (layout/game_category_list)...
            val listLayout: View =
                layoutInflater.inflate(R.layout.game_category_list, parentLayout, false)

            //name the category with its given title
            listLayout.findViewById<TextView>(R.id.game_list_category_title).text = categories[i]

            //get the view into which we will inflate our game card
            val cardAreaLayout = listLayout.findViewById<LinearLayout>(R.id.game_card_area)

            //for each game in the gamelist
            for (g in gl) {

                //if it is part of the category add its card to the view=
                if (g.category.equals(categories[i])) {
                    val gameCardLayout: View =
                        layoutInflater.inflate(R.layout.game_card, cardAreaLayout, false)

                    //set game title
                    gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = g.title

                    //Loading Image from URL (this is done with the Picasso lib)
                    Picasso.get()
                        .load(g.img)
                        .resize(200, 250)
                        .centerCrop()
                        .into(gameCardLayout.findViewById<ImageView>(R.id.game_card_img))

                    //add the game card to the Game Category scrolling List view
                    cardAreaLayout.addView(gameCardLayout)
                }
            }

            parentLayout.addView(listLayout)
        }
    }

    //This class will handle the connection callback between our service and this fragment
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

    //broadcast receiver for communication between our Services and this fragment
    private val mBraodcastReceiver = object : BroadcastReceiver() {
        @Override
        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action) {
                Constants().mBroadcastGameListUpdate -> updateUI()
            }
        }
    }
}