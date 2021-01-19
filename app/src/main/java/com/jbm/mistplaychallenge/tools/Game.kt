package com.jbm.mistplaychallenge.tools

class Game {
    var title: String = ""
    var img: String = ""
    var category: String = ""

    @Override
    override fun toString(): String {
        val str: String = "Game title : " + title + ", Game Category : " + category +  " and img URL : " + img
        return str
    }
}