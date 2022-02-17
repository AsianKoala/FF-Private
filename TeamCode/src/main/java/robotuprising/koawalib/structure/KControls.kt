package robotuprising.koawalib.structure

import robotuprising.koawalib.gamepad.CommandGamepad

abstract class KControls {
    protected lateinit var driver: CommandGamepad
    protected lateinit var gunner: CommandGamepad

    open fun bindControls(driver: CommandGamepad, gunner: CommandGamepad) {
        this.driver = driver
        this.gunner = gunner
    }
}