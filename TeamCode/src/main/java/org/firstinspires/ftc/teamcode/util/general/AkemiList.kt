package org.firstinspires.ftc.teamcode.util.general

class AkemiList<E> : ArrayList<E>()  {
    override fun get(index: Int): E {
        return if(index >= 0) {
            super.get(index)
        } else {
            super.get(size - index)
        }
    }
}