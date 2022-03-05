package asiankoala.junk.v1.opmodes

// @TeleOp
// class NakiriTeleOp : NakiriOpMode() {
//    private fun driveControl() {
//        val scale = if(gamepad1.right_bumper) {
//            0.7
//        } else {
//            1.0
//        }
//
//        nakiri.requestAyamePowers(
//                MathUtil.cubicScaling(0.85, gamepad1.left_stick_x.d * scale),
//                MathUtil.cubicScaling(0.85, -gamepad1.left_stick_y.d * scale),
//                MathUtil.cubicScaling(0.85, gamepad1.right_stick_x.d * scale)
//        )
//    }
//
//    private enum class IntakeSequenceStates {
//        INTAKE_OUTTAKE_RESET,
//        INTAKE,
//        ROTATING,
//        TRANSFERRING,
//        OUTTAKE_TO_MEDIUM
//    }
//
//    private val intakeSequence = StateMachineBuilder<IntakeSequenceStates>()
//            .state(IntakeSequenceStates.INTAKE_OUTTAKE_RESET)
//            .onEnter {
//                nakiri.requestIntakeRotateOut()
//                nakiri.requestOuttakeIn()
//            }
//            .transitionTimed(0.5)
//            .state(IntakeSequenceStates.INTAKE)
//            .onEnter { nakiri.requestIntakeOn() }
//            .transition { nakiri.isMineralIn() || gamepad1.b}
//            .state(IntakeSequenceStates.ROTATING)
//            .onEnter {
//                nakiri.requestIntakeRotateIn()
//                nakiri.requestIntakeOff()
//                nakiri.requestLiftTransfer()
//                nakiri.requestLinkageTransfer()
//            }
//            .transitionTimed(1.5)
//            .state(IntakeSequenceStates.TRANSFERRING)
//            .onEnter {
//                nakiri.requestIntakeReverse()
//            }
//            .transitionTimed(1.5)
//            .state(IntakeSequenceStates.OUTTAKE_TO_MEDIUM)
//            .onEnter {
//                nakiri.requestOuttakeMedium()
//                nakiri.requestIntakeOff()
//            }
//            .transition { true }
//            .build()
//
//
//    private enum class OuttakeLongStates {
//        LIFTING,
//        EXTENDING_AND_WAIT,
//        DEPOSIT,
//        RETRACT_LINKAGE_AND_OUTTAKE,
//        LIFT_DOWN
//    }
//    private val outtakeLongSequence = StateMachineBuilder<OuttakeLongStates>()
//            .state(OuttakeLongStates.LIFTING)
//            .onEnter {
//                nakiri.requestLiftHigh()
//            }
//            .transitionTimed(0.4)
//            .state(OuttakeLongStates.EXTENDING_AND_WAIT)
//            .onEnter { nakiri.requestLinkageOut() }
//            .transition { gamepad2.right_bumper }
//            .state(OuttakeLongStates.DEPOSIT)
//            .onEnter { nakiri.requestOuttakeOut() }
//            .transitionTimed(0.5)
//            .state(OuttakeLongStates.RETRACT_LINKAGE_AND_OUTTAKE)
//            .onEnter {
//                nakiri.requestOuttakeIn();
//                nakiri.requestLinkageRetract()
//            }
//            .transitionTimed(0.5)
//            .state(OuttakeLongStates.LIFT_DOWN)
//            .onEnter { nakiri.requestLiftBottom() }
//            .transition { true }
//            .build()
//
//    private fun outtakeControl() {
//        nakiri.runCloseOuttakeSequence(gamepad2.right_trigger_pressed)
//        nakiri.runSharedOuttakeSequence(gamepad2.left_bumper)
//        nakiri.runnMiddleTeleOp(gamepad2.a)
//
//        if (!outtakeLongSequence.running && gamepad2.right_bumper) {
//            outtakeLongSequence.reset()
//            outtakeLongSequence.start()
//        }
//
//        if (outtakeLongSequence.running) {
//            outtakeLongSequence.update()
//        }
//    }
//
//    private fun intakeControl() {
// //        nakiri.runIntakeSequence(gamepad1.right_trigger_pressed)
//        intakeSequence.smartRun(gamepad1.right_trigger_pressed)
//    }
//
//    private fun duckControl() {
//        when {
//            gamepad1.dpad_left -> nakiri.requestSpinnerOn()
//            gamepad1.dpad_right -> nakiri.requestSpinnerReverse()
//            else -> nakiri.requestSpinnerOff()
//        }
//    }
//
//    private fun debugControl() {
//        when {
//            gamepad2.right_trigger_pressed -> nakiri.requestLiftHigh()
//            gamepad2.left_trigger_pressed -> nakiri.requestLiftTransfer()
//            gamepad2.right_bumper -> nakiri.requestLiftBottom()
//            gamepad2.a -> nakiri.requestOuttakeIn()
//            gamepad2.b -> nakiri.requestOuttakeOut()
//        }
//    }
//
//    override fun mInit() {
//        super.mInit()
//        nakiri.ayame.poseEstimate = Pose2d(8.0, 63.0, 90.0.radians)
//    }
//
//    override fun mStart() {
//        super.mStart()
// //        nakiri.startGoingOverPipes() // testing
//    }
//
//    override fun mLoop() {
//        super.mLoop()
//        driveControl()
//        intakeControl()
//        outtakeControl()
//        duckControl()
// //        debugControl()
//    }
// }
