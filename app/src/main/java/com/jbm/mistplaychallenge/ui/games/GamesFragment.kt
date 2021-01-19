package com.jbm.mistplaychallenge.ui.games

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

    private lateinit var ll: LinearLayout

    @Override
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_games, container, false)

        //get layout for future updates
        ll = root.findViewById(R.id.game_card_area)

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

        Log.d(TAG, "Updating UI with new game list")

        val gl = mGameServiceInstance?.getGameList()

        for (i in 1..gl!!.size.minus(1)) {
            val gameCardLayout: View = layoutInflater.inflate(R.layout.game_card, ll, false)
            gameCardLayout.findViewById<TextView>(R.id.game_card_title).text = gl[i].title


            //Loading Image from URL
            Picasso.get()
                .load(gl[i].img)
                .resize(200, 250)
                .centerCrop()
                .into(gameCardLayout.findViewById<ImageView>(R.id.game_card_img))
            ll.addView(gameCardLayout)
        }
    }

    //braodcast reveiver for communication between Services and this fragment
    private val mBraodcastReceiver = object : BroadcastReceiver() {
        @Override
        override fun onReceive(p0: Context?, p1: Intent?) {
            //Log.d(TAG, "onReceive " + p1?.action)

            when(p1?.action) {
                Constants().mBroadcastGameListUpdate -> updateUI()
            }
        }
    }
}