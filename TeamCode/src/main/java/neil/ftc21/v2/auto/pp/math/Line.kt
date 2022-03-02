package neil.ftc21.v2.auto.pp.math

import com.acmerobotics.roadrunner.util.epsilonEquals
import neil.lib.math.MathUtil.epsilonNotEqual
import neil.lib.math.Point
import java.lang.IllegalArgumentException

class Line {
    var slope = 0.0
    var intercept = 0.0

    // If true, intercept is x-intercept. Otherwise (usually) it is y-intercept
    var isVertical = false
        private set

    constructor(p1: Point, p2: Point) : this(
            p1,
            (p2.y - p1.y) / (p2.x - p1.x)
    ) {
    }

    constructor(point: Point, slope: Double) {
        // We use negative infinity for the slope because it makes perpendicular slopes work
        if (java.lang.Double.isInfinite(slope)) {
            this.slope = Double.NEGATIVE_INFINITY
            intercept = point.x
            isVertical = true
        } else {
            this.slope = slope
            intercept = point.y - point.x * slope
            isVertical = false
        }
    }

    constructor(slope: Double, yIntercept: Double) {
        require(!slope.isInfinite()) { "Cannot use slope/y-intercept form for vertical line" }
        this.slope = slope
        intercept = yIntercept
        isVertical = false
    }

    fun perpendicularSlope(): Double {
        return -1.0 / slope
    }

    fun evaluate(x: Double): Double {
        return if (isVertical) {
            throw IllegalArgumentException("Cannot evalute values of vertical line")
        } else {
            x * slope + intercept
        }
    }

    fun intersect(l2: Line): Point {
        // Vertical lines will trigger this too, as they always have slope NEGATIVE_INFINITY
        if (slope epsilonEquals l2.slope) {
            // If parallel lines
            require(intercept epsilonNotEqual l2.intercept) { "Equal lines intersect everywhere" }
            // If parallel lines
            throw IllegalArgumentException("Parallel lines do not intersect")
        }
        return if (isVertical) {
            Point(intercept, l2.evaluate(intercept))
        } else if (l2.isVertical) {
            Point(l2.intercept, evaluate(intercept))
        } else {
            val xIntersect = (l2.intercept - intercept) / (slope - l2.slope)
            Point(xIntersect, evaluate(xIntersect))
        }
    }

    fun nearestLinePoint(p: Point): Point {
        val perpContainer = Line(p, perpendicularSlope())
        return this.intersect(perpContainer)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val line = o as Line
        return line.slope epsilonEquals slope && line.intercept epsilonEquals intercept
    }

    companion object {
        fun midpoint(p1: Point, p2: Point): Point {
            return Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
        }

        fun distance(p1: Point, p2: Point): Double {
            return Math.sqrt(Math.pow(p1.x - p2.x, 2.0) + Math.pow(p1.y - p2.y, 2.0))
        }
    }
}