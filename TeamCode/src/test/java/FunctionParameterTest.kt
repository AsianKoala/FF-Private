object FunctionParameterTest {
    @JvmStatic
    fun main(args: Array<String>) {

    }

    fun test(f: () -> Unit) {
        f()
    }
}


class gamepad {
    var x_pressed = false
    var last_x_pressed = false

    fun assign(f: () -> Unit) {
        if(x_pressed != last_x_pressed) {
            l
        }
    }
}