package com.jbm.mistplaychallenge.tools

class GameList {
    //{"list_title":"Fantasy", "games":[

    var listTitle: String = ""
    var gameList: MutableList<Game> = mutableListOf()

    @Override
    override fun toString(): String {
        val str: String = "List title is " + listTitle + " with games " + gameList.toString()
        return str
    }
}