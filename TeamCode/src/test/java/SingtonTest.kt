import robotuprising.ftc2021.hardware.subsystems.Ayame
import robotuprising.lib.hardware.MecanumPowers

object SingtonTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val m_homura = Ayame
        m_homura.setFromMecanumPowers(MecanumPowers())
    }
}
