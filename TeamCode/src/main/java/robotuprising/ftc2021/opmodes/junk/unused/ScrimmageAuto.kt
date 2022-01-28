package robotuprising.ftc2021.opmodes.junk.unused

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import robotuprising.ftc2021.opmodes.junk.nakiri.NakiriOpMode

//@Autonomous(name = "Scrimmage Auto")
//@Disabled
//class ScrimmageAuto : NakiriOpMode() {
//
//    var start: Long = 0
//    override fun m_start() {
//        super.m_start()
//        start = System.currentTimeMillis()
//    }
//
//    var state = states.TURNING
//    enum class states {
//        TURNING,
//        SPINNING
//    }
//    override fun m_loop() {
//        super.m_loop()
//
//        when (state) {
//            states.TURNING -> {
//                superstructure.requestAyamePowers(
//                    Pose(
//                        Point(
//                            0.0,
//                            0.0
//                        ),
//                        Angle(-0.2, AngleUnit.RAW)
//                    )
//                )
//
//                if (System.currentTimeMillis() - start > 250) {
//                    start = System.currentTimeMillis()
//                    state = states.SPINNING
//                    superstructure.requestAyamePowers(Pose(Point.ORIGIN, Angle(0.0, AngleUnit.RAW)))
//                }
//            }
//
//            states.SPINNING -> {
//                superstructure.requestSpinnerReverse()
//
//                if (System.currentTimeMillis() - start > 6000) {
//                    superstructure.requestSpinnerOff()
//                    requestOpModeStop()
//                }
//            }
//        }
//    }
//}
