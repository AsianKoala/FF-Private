package org.firstinspires.ftc.teamcode.control.path.funcs

import org.firstinspires.ftc.teamcode.control.path.Path
import org.firstinspires.ftc.teamcode.hardware.Superstructure

fun interface RepeatFunction : Func {
    fun run(akemi: Superstructure, path: Path)
}
