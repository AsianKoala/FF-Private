package asiankoala.ftc21.robotroopers

import asiankoala.ftc21.robotroopers.subsystems.Drive

class Yonagi(hardware: Hardware) {
    val drive = Drive(hardware.fl, hardware.bl, hardware.fr, hardware.br)
}