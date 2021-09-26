package robotuprising.lib.movement.pathfunc

import robotuprising.lib.movement.path.Path
import robotuprising.ftc2021.hardware.Superstructure

fun interface LoopUntilFunction : Func {
    fun run(akemi: Superstructure, path: Path): Boolean
}
