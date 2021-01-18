package com.jbm.mistplaychallenge.tools

class Game {
    var title: String = ""
    var img: String = ""

    @Override
    override fun toString(): String {
        val str: String = "Game title : " + title + " and img URL : " + img
        return str
    }
}