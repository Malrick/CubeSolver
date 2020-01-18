package app.model.constants

// CORNER INSERTION : Insersion of a corner on the first layer (White side)

val CORNER_INSERSION_1 = arrayOf(Movement.GREEN, Movement.YELLOW, Movement.GREEN_REVERSE)
val CORNER_INSERSION_2 = arrayOf(Movement.GREEN, Movement.YELLOW_REVERSE, Movement.GREEN_REVERSE)
val CORNER_INSERSION_3 = arrayOf(Movement.GREEN_REVERSE, Movement.YELLOW, Movement.GREEN)
val CORNER_INSERSION_4 = arrayOf(Movement.GREEN_REVERSE, Movement.YELLOW_REVERSE, Movement.GREEN)
val CORNER_INSERSION_5 = arrayOf(Movement.RED, Movement.YELLOW, Movement.RED_REVERSE)
val CORNER_INSERSION_6 = arrayOf(Movement.RED, Movement.YELLOW_REVERSE, Movement.RED_REVERSE)
val CORNER_INSERSION_7 = arrayOf(Movement.RED_REVERSE, Movement.YELLOW, Movement.RED)
val CORNER_INSERSION_8 = arrayOf(Movement.RED_REVERSE, Movement.YELLOW_REVERSE, Movement.RED)
val CORNER_INSERSION_9 = arrayOf(Movement.BLUE, Movement.YELLOW, Movement.BLUE_REVERSE)
val CORNER_INSERSION_10 = arrayOf(Movement.BLUE, Movement.YELLOW_REVERSE, Movement.BLUE_REVERSE)
val CORNER_INSERSION_11 = arrayOf(Movement.BLUE_REVERSE, Movement.YELLOW, Movement.BLUE)
val CORNER_INSERSION_12 = arrayOf(Movement.BLUE_REVERSE, Movement.YELLOW_REVERSE, Movement.BLUE)
val CORNER_INSERSION_13 = arrayOf(Movement.ORANGE, Movement.YELLOW, Movement.ORANGE_REVERSE)
val CORNER_INSERSION_14 = arrayOf(Movement.ORANGE, Movement.YELLOW_REVERSE, Movement.ORANGE_REVERSE)
val CORNER_INSERSION_15 = arrayOf(Movement.ORANGE_REVERSE, Movement.YELLOW, Movement.ORANGE)
val CORNER_INSERSION_16 = arrayOf(Movement.ORANGE_REVERSE, Movement.YELLOW_REVERSE, Movement.ORANGE)

// EDGES INSERTION : Insertion of a edge on the second layer

val EDGE_INSERTION_1 = CORNER_INSERSION_7 + arrayOf(Movement.YELLOW) + CORNER_INSERSION_2
val EDGE_INSERTION_2 = CORNER_INSERSION_2 + arrayOf(Movement.YELLOW_REVERSE) + CORNER_INSERSION_7
val EDGE_INSERTION_3 = CORNER_INSERSION_11 + arrayOf(Movement.YELLOW) + CORNER_INSERSION_6
val EDGE_INSERTION_4 = CORNER_INSERSION_6 + arrayOf(Movement.YELLOW_REVERSE) + CORNER_INSERSION_11
val EDGE_INSERTION_5 = CORNER_INSERSION_15 + arrayOf(Movement.YELLOW) + CORNER_INSERSION_10
val EDGE_INSERTION_6 = CORNER_INSERSION_10 + arrayOf(Movement.YELLOW_REVERSE) + CORNER_INSERSION_15
val EDGE_INSERTION_7 = CORNER_INSERSION_3 + arrayOf(Movement.YELLOW) + CORNER_INSERSION_14
val EDGE_INSERTION_8 = CORNER_INSERSION_14 + arrayOf(Movement.YELLOW_REVERSE) + CORNER_INSERSION_3

// OLL ONE

val OLL_ONE = arrayOf(Movement.BLUE, Movement.YELLOW, Movement.RED, Movement.YELLOW_REVERSE, Movement.RED_REVERSE, Movement.BLUE_REVERSE)

// OLL TWO

val OLL_TWO = arrayOf(Movement.RED, Movement.YELLOW, Movement.YELLOW, Movement.RED_REVERSE, Movement.YELLOW_REVERSE, Movement.RED, Movement.YELLOW_REVERSE, Movement.RED_REVERSE, Movement.ORANGE_REVERSE, Movement.YELLOW, Movement.YELLOW, Movement.ORANGE, Movement.YELLOW, Movement.ORANGE_REVERSE, Movement.YELLOW, Movement.ORANGE)

// PLL ONE

val PLL_ONE = arrayOf(Movement.GREEN, Movement.GREEN, Movement.YELLOW, Movement.GREEN, Movement.YELLOW, Movement.GREEN_REVERSE, Movement.YELLOW_REVERSE, Movement.GREEN_REVERSE, Movement.YELLOW_REVERSE, Movement.GREEN_REVERSE, Movement.YELLOW, Movement.GREEN_REVERSE)
val PLL_ONE_SECOND = arrayOf(Movement.GREEN, Movement.YELLOW_REVERSE, Movement.GREEN, Movement.YELLOW, Movement.GREEN, Movement.YELLOW, Movement.GREEN, Movement.YELLOW_REVERSE, Movement.GREEN_REVERSE, Movement.YELLOW_REVERSE, Movement.GREEN_REVERSE, Movement.GREEN_REVERSE)

// PLL TWO

val PLL_TWO = arrayOf(Movement.GREEN_REVERSE, Movement.RED, Movement.GREEN_REVERSE, Movement.ORANGE, Movement.ORANGE, Movement.GREEN, Movement.RED_REVERSE, Movement.GREEN_REVERSE, Movement.ORANGE, Movement.ORANGE, Movement.GREEN, Movement.GREEN)
val PLL_TWO_SECOND = arrayOf(Movement.BLUE, Movement.RED_REVERSE, Movement.BLUE, Movement.ORANGE, Movement.ORANGE, Movement.BLUE_REVERSE, Movement.RED, Movement.BLUE, Movement.ORANGE, Movement.ORANGE, Movement.BLUE, Movement.BLUE)
