package app

import app.model.movement.RelativeMovement

/*
    Different sequences useful for solving the cube
    You can find informations on OLLs and PLLs here : http://badmephisto.com/
 */

val RELATIVE_CORNER_INSERTION_1 = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val RELATIVE_CORNER_INSERTION_2 = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val RELATIVE_CORNER_INSERTION_3 = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT
)
val RELATIVE_CORNER_INSERTION_4 = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT
)

val RELATIVE_EDGE_INSERTION_1 = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP,
    RelativeMovement.FRONT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val RELATIVE_EDGE_INSERTION_2 = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.FRONT
)

val TRIGGER_ONE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE
)
val TRIGGER_ONE_SYMETRY = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP
)
val TRIGGER_TWO = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE
)
val TRIGGER_TWO_SYMETRY = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT
)
val TRIGGER_THREE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP
)
val TRIGGER_THREE_SYMETRY = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE
)

val OLL_ONE = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_TWO = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_THREE = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + TRIGGER_ONE + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_FOUR = arrayOf(
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE
)
val OLL_FIVE = arrayOf(
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE
)
val OLL_SIX = arrayOf(
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK
)
val OLL_SEVEN = arrayOf(RelativeMovement.FRONT_REVERSE) + TRIGGER_ONE_SYMETRY + TRIGGER_ONE_SYMETRY + arrayOf(
    RelativeMovement.FRONT
)
val OLL_EIGHT = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_NINE = arrayOf(RelativeMovement.FRONT_REVERSE) + TRIGGER_ONE_SYMETRY + arrayOf(
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_TEN = arrayOf(
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_ELEVEN = arrayOf(
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_TWELVE = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE
)
val OLL_THIRTEEN = arrayOf(
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_FOURTEEN = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT_REVERSE
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE
)
val OLL_FIFTEEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT
)
val OLL_SIXTEEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT
)
val OLL_SEVENTEEN = TRIGGER_ONE + arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE
)
val OLL_EIGHTEEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE
)
val OLL_NINETEEN = arrayOf(RelativeMovement.FRONT) + TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE
)
val OLL_TWENTY = TRIGGER_ONE + TRIGGER_TWO
val OLL_TWENTYONE = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE
)
val OLL_TWENTYTWO = arrayOf(
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT
)
val OLL_TWENTYTHREE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE
) + TRIGGER_TWO + arrayOf(
    RelativeMovement.TOP,
    RelativeMovement.RIGHT
)
val OLL_TWENTYFOUR = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
) + TRIGGER_TWO + arrayOf(
    RelativeMovement.TOP,
    RelativeMovement.TOP
) + TRIGGER_TWO
val OLL_TWENTYFIVE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
) + TRIGGER_TWO + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_TWENTYSIX = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.LEFT
) + TRIGGER_TWO
val OLL_TWENTYSEVEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_TWENTYEIGHT = TRIGGER_THREE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE
) + TRIGGER_TWO
val OLL_TWENTYNINE = TRIGGER_THREE_SYMETRY + arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP
) + TRIGGER_TWO_SYMETRY
val OLL_THIRTY = TRIGGER_THREE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_THIRTYONE = TRIGGER_THREE + TRIGGER_TWO + arrayOf(
    RelativeMovement.TOP,
    RelativeMovement.TOP
) + TRIGGER_TWO
val OLL_THIRTYTWO = arrayOf(RelativeMovement.FRONT) + TRIGGER_THREE + arrayOf(
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT_REVERSE
)
val OLL_THIRTYTHREE = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.LEFT
)
val OLL_THIRTYFOUR = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT_REVERSE
)
val OLL_THIRTYFIVE = TRIGGER_THREE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_THIRTYSIX = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_THIRTYSEVEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP
) + TRIGGER_THREE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_THIRTYEIGHT = TRIGGER_THREE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.FRONT_REVERSE
)
val OLL_THIRTYNINE = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT_REVERSE
)
val OLL_FOURTY = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT
)
val OLL_FOURTYONE = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE
)
val OLL_FOURTYTWO = arrayOf(
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val OLL_FOURTYTHREE = arrayOf(
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.LEFT
)
val OLL_FOURTYFOUR = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE
)
val OLL_FOURTYFIVE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val OLL_FOURTYSIX = arrayOf(
    RelativeMovement.FRONT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FOURTYSEVEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val OLL_FOURTYEIGHT = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FOURTYNINE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK
)
val OLL_FIFTY = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FIFTYONE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT
)
val OLL_FIFTYTWO = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FIFTYTHREE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FIFTYFOUR = TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FIFTYFIVE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE
)
val OLL_FIFTYSIX = TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val OLL_FIFTYSEVEN = TRIGGER_THREE + TRIGGER_TWO + arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)

val PLL_ONE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT
)
val PLL_TWO = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT
)
val PLL_THREE = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT
)
val PLL_FOUR = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE
)
val PLL_FIVE = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT
)
val PLL_SIX = TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val PLL_SEVEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE
)
val PLL_EIGHT = arrayOf(
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE
)
val PLL_NINE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT
) + TRIGGER_ONE + arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE
)
val PLL_TEN = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.FRONT,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.TOP
)
val PLL_ELEVEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT
)
val PLL_TWELVE = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT
)
val PLL_THIRTEEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.BOTTOM_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BOTTOM,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT
)
val PLL_FOURTEEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.BOTTOM,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.BOTTOM_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK
)
val PLL_FIFTEEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.TOP,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.BOTTOM,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK_REVERSE
)
val PLL_SIXTEEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BOTTOM_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.FRONT
)
val PLL_SEVENTEEN = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.BOTTOM,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.BACK,
    RelativeMovement.BACK,
    RelativeMovement.LEFT,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT,
    RelativeMovement.RIGHT,
    RelativeMovement.FRONT,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP
)
val PLL_EIGHTTEEN = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE
)
val PLL_NINETEEN = arrayOf(
    RelativeMovement.RIGHT,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT,
    RelativeMovement.RIGHT,
    RelativeMovement.BACK_REVERSE,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.FRONT_REVERSE
)
val PLL_TWENTY = arrayOf(
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE
)
val PLL_TWENTYONE = arrayOf(
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.LEFT,
    RelativeMovement.TOP_REVERSE,
    RelativeMovement.RIGHT,
    RelativeMovement.TOP,
    RelativeMovement.TOP,
    RelativeMovement.LEFT_REVERSE,
    RelativeMovement.TOP,
    RelativeMovement.RIGHT_REVERSE,
    RelativeMovement.TOP
)