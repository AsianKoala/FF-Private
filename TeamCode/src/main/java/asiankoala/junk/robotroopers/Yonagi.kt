package asiankoala.junk.robotroopers

import asiankoala.junk.robotroopers.subsystems.Drive

class Yonagi(hardware: Hardware) {
    val drive = Drive(hardware.fl, hardware.bl, hardware.fr, hardware.br)
}
