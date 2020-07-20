package app.model.cube.lookups

import app.model.Color
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition

/*
    All the references needed to have a quick access to data
    (These references are used for performance matters)
 */
data class CubeLookups(var rotationLookups : HashMap<Color, RotationLookup>,
                       var edgeLookup : HashMap<EdgePositionsIdentities, EdgePosition>,
                       var cornerLookup : HashMap<CornerPositionsIdentities, CornerPosition>,
                       var sortedCornersPositionLookup : List<CornerPosition>,
                       var sortedEdgesPositionLookup : List<EdgePosition>)
