# Mistplay Android Challenge 2021
Solution by Jean-Baptiste Meyer

# The Challenge
Create a vertical scrolling list of horizontally scrolling lists.
Mistplay supports this UI for displaying the games in separate categories.

The requirements for this mini project are to:
1) Build the functionality for the list of lists. Make sure to do this using Object-Oriented programming principles as well as design patterns.
2) Use the Kotlin programming language.
3) Incorporate the classes you build into a sample
application. This sample application can use any design you choose.
4) Well commented and formatted code.


# The Solution
https://youtu.be/paBY7wweu2U

Simple app that:
1) Uses Fragments and Bottom navigation bar for the UI
2) Loads a list of game from a JSON file stored in res/raw
3) Dynamicly builds the UI base on the JSON file
4) Uses ScrollViews for the scrolling features
5) Uses the Picasso lib to handle the game's images download from the cloud
6) Runs on API >23 (Android 6.0 and newer)
