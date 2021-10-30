package lib

import robotuprising.lib.control.motion.PIDCoeffs
import robotuprising.lib.control.motion.PIDFController
import robotuprising.lib.util.Extensions.d

object PIDFControllerLambdaTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = PIDFController(PIDCoeffs(0.d, 0.d, 0.d))
        controller.apply {
            targetPosition = 100.d
            targetVelocity = 1.d
            targetAcceleration = 1.d + 9.8
        }
        
    }
}
