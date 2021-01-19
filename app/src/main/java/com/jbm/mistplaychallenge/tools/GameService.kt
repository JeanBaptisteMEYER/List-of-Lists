package com.jbm.mistplaychallenge.tools

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.jbm.mistplaychallenge.R
import com.jbm.mistplaychallenge.ui.games.GamesFragment
import org.json.JSONArray
import org.json.JSONObject

class GameService : Service() {

    var mGameList: MutableList<Game> = mutableListOf()
    val TAG = "tag.jbm." + GameService::class.java.simpleName

    val mBinder: IBinder = MyBinder()

    inner class MyBinder : Binder() {
        internal val service: GameService
            get() = this@GameService
    }

    @Override
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun getGameList(): MutableList<Game> {
        return mGameList
    }

    // this fun get the data from the JSON file in res/raw, parse it and build the list of game
    fun parseJsonData() {

        val rawData = resources.openRawResource(R.raw.jsondata).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(rawData)

        synchronized(mGameList) {
            mGameList.clear()
        }

        for (i in 0..jsonArray.length()-1) {
            val listObject = jsonArray.getJSONObject(i)

            val listeTitle = listObject.getString("list_title")
            val gameListArray = listObject.getJSONArray("games")

            for (j in 0..gameListArray.length()-1) {
                val gameObject = gameListArray.getJSONObject(j)

                var mGame: Game = Game()
                mGame.category = listeTitle
                mGame.title = gameObject.getString("title")
                mGame.img = gameObject.getString("img")

                mGameList.add(mGame)
            }
        }

        // List of game as been parse. Lets broadcast to the fragment for it to update its UI with new game list
        this.sendBroadcast(Intent().setAction(Constants().mBroadcastGameListUpdate))

        //Log.d(TAG, mGameList.toString())
    }

}