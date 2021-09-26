package robotuprising.lib.movement.pathfunc

class SysFunc(var time: Double, val func: Func) {

    init {
        time += System.currentTimeMillis()
    }
}
