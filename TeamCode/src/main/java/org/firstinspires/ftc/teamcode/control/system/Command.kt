package org.firstinspires.ftc.teamcode.control.system

abstract class Command(val prio: Int, val command: () -> Unit)
