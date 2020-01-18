package app.model

import app.model.constants.Color


class Cube {

    var sides : HashMap<Color, Side> = HashMap()
    var corners = arrayOf<Corner>()
    var edges = arrayOf<Edge>()

}