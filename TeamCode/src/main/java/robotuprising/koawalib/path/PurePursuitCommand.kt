package robotuprising.koawalib.path

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.path.waypoints.Waypoint
import robotuprising.koawalib.subsystem.drive.DriveStates
import robotuprising.koawalib.subsystem.drive.KMecanumOdoDrive

class PurePursuitCommand(private val drive: KMecanumOdoDrive, vararg waypoints: Waypoint) : Command {
    private val path: PurePursuitPath

    override fun initialize() {
        drive.driveState = DriveStates.PATH
        drive.currentPath = path
    }

    override fun execute() {

    }

    override fun isFinished(): Boolean {
        return drive.currentPath == null
    }

    init {
        addRequirements(drive)
        path = PurePursuitPath(waypoints.toList())
    }
}