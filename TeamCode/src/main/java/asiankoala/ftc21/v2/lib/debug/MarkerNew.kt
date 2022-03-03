package asiankoala.ftc21.v2.lib.debug

class MarkerNew(var name: String) {
    var start: Long = System.currentTimeMillis()
    var time = System.currentTimeMillis() - start

    fun start() {
        start = System.currentTimeMillis()
    }
}
