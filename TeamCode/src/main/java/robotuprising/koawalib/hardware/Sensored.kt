package robotuprising.koawalib.hardware

import robotuprising.koawalib.util.KDouble

interface Sensored : KDouble {
    val sensorValue: Double
}