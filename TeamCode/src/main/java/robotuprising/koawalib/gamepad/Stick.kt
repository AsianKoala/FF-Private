package robotuprising.koawalib.gamepad

import robotuprising.koawalib.math.Point
import robotuprising.koawalib.util.interfaces.Enableable

import robotuprising.koawalib.util.interfaces.Periodic


interface Stick : Periodic, Enableable<Stick> {
    /** Return x axis double
     *
     * @return The double
     */
    fun getXAxis(): Double

    /** Return y axis double
     *
     * @return The double
     */
    fun getYAxis(): Double

    /** Return x axis supplier
     *
     * @return The double supplier
     */
    val xSupplier: () -> Double
        get() = this::getXAxis

    /** Return y axis supplier
     *
     * @return The double supplier
     */
    val ySupplier: () -> Double
        get() = this::getYAxis


    val point: Point
        get() = Point(getXAxis(), getYAxis())

    /** Returns the angle of the stick
     *
     * @return The angle
     */
    val angle: Double
        get() = point.atan2

    /** Returns the stick's distance from the center
     *
     * @return The distance
     */
    val distanceFromCenter: Double
        get() = point.hypot
}
