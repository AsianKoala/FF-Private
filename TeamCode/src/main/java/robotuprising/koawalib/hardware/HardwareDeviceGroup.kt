package robotuprising.koawalib.hardware

interface HardwareDeviceGroup<T : HardwareDevice<*>> {

    fun getFollowers(): Array<T>

    fun getFollowerList(): List<T> {
        return getFollowers().asList()
    }

    fun getAllDevices(): Array<T>

    fun getAllDevicesList(): List<T> {
        return getAllDevices().asList()
    }

    fun propogate(value: Double) {}
}