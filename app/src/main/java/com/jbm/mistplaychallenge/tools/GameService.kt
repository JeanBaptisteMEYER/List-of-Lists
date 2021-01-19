package com.jbm.mistplaychallenge.tools

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.jbm.mistplaychallenge.R
import org.json.JSONArray

class GameService : Service() {

    val TAG = "tag.jbm." + GameService::class.java.simpleName

    val mBinder: IBinder = MyBinder()

    inner class MyBinder : Binder() {
        internal val service: GameService
            get() = this@GameService
    }

    //List of Game that'll be populated from a JSON file
    var mGameList: MutableList<Game> = mutableListOf()


    @Override
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    // this fun get the data from the JSON file in res/raw, parse it and build the list of game
    fun parseJsonData() {

        val rawData = resources.openRawResource(R.raw.jsondata).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(rawData)

        synchronized(mGameList) {
            mGameList.clear()
        }

        //Parse JSON object into a list of Game object
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
    }
}