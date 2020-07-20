package app.model.cube.lookups

import app.model.cube.position.Position

/*
    Used for side rotations. Saving the value of the pieces to switch saves a lot of time.
 */
data class RotationLookup(var cornerA : Position,
                          var cornerB : Position,
                          var cornerC : Position,
                          var cornerD : Position,
                          var edgeA : Position,
                          var edgeB : Position,
                          var edgeC : Position,
                          var edgeD : Position)