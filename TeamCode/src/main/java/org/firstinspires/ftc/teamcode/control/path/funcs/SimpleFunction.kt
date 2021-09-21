package org.firstinspires.ftc.teamcode.control.path.funcs

import org.firstinspires.ftc.teamcode.control.path.Path
import org.firstinspires.ftc.teamcode.deprecated.OldAzusa

fun interface SimpleFunction : Func {
    fun run(azusa: OldAzusa, path: Path)
}
