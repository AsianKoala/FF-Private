package robotuprising.lib.movement.pathfunc

import robotuprising.lib.movement.path.Path
import robotuprising.ftc2021.hardware.Superstructure

fun interface InterruptFunction : Func {
    fun run(akemi: Superstructure, path: Path): Boolean
}
