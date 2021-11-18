package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.RevBulkData

object BulkDataButScuffed {
    lateinit var hwMap: HardwareMap
    lateinit var masterData: RevBulkData
    lateinit var slaveData: RevBulkData

    fun updateData(master: RevBulkData, slave: RevBulkData) {
        masterData = master
        slaveData = slave
    }
}