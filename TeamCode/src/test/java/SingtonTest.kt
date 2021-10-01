import robotuprising.ftc2021.hardware.subsystems.Homura
import robotuprising.lib.hardware.MecanumPowers

object SingtonTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val m_homura = Homura
        m_homura.setPowers(MecanumPowers())
    }
}
