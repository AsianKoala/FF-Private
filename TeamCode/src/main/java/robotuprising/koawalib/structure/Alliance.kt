package robotuprising.koawalib.structure

enum class Alliance {
    BLUE, RED;

    fun <T> decide(a: T, b: T): T {
        return if(this == BLUE) {
            a
        } else {
            b
        }
    }
}