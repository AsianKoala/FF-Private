package neil.ftc21.robotroopers

import neil.ftc21.robotroopers.subsystems.Drive

class Yonagi(hardware: Hardware) {
    val drive = Drive(hardware.fl, hardware.bl, hardware.fr, hardware.br)
}