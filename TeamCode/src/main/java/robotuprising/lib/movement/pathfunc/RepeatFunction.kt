package robotuprising.lib.movement.pathfunc

import robotuprising.lib.movement.path.Path
import robotuprising.ftc2021.hardware.Superstructure

fun interface RepeatFunction : Func {
    fun run(akemi: Superstructure, path: Path)
}
