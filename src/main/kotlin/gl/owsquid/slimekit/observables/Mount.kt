package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.context
import java.util.*

fun dismount(onDismount: (causedBy: Signal<*>?) -> Unit) {
    val ctx = context()
    val uuid = UUID.randomUUID()
    var called = false
    val callback: (Signal<*>?) -> Unit = {
        if (!called) {
            onDismount(it)
            called = true
        }
    }

    ctx.dismounts[uuid] = callback
}