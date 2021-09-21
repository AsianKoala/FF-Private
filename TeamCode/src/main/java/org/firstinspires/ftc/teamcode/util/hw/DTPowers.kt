package org.firstinspires.ftc.teamcode.util.hw

import org.firstinspires.ftc.teamcode.util.math.Point

data class DTPowers(private var powers: Point = Point.ORIGIN) {
    constructor(fwd: Double, turn: Double) : this(Point(fwd, turn))
    var fwd
        get() = powers.x
        set(x) {
            powers.x = x
        }
    var turn
        get() = powers.y
        set(x) {
            powers.y = x
        }
}
