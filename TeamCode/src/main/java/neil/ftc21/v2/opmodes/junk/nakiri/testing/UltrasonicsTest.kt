package neil.ftc21.v2.opmodes.junk.nakiri.testing

//@TeleOp
//@Disabled
//class UltrasonicsTest : BaseOpMode() {
//    private lateinit var ultrasonics: Ultrasonics
//
//    override fun mInit() {
//        BulkDataManager.init(hardwareMap)
//        NakiriDashboard.init(telemetry, true)
//        ultrasonics = Ultrasonics()
//    }
//
//    override fun mStart() {
//        ultrasonics.startReading()
//    }
//
//    override fun mLoop() {
//        BulkDataManager.read()
//        ultrasonics.update()
//        ultrasonics.sendDashboardPacket(false)
//        NakiriDashboard.update()
//    }
//
//}