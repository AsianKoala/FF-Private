package robotuprising.ftc2021.hardware.wraith.interfaces

interface Zeroable {
    var zeroInitTime: Int
    val postOffsetValue: Double
    var offset: Double
    fun zero()
    fun readyForInit(): Boolean
}