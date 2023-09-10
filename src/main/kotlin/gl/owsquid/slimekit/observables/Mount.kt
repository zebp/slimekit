package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.ExampleMod.logger
import gl.owsquid.slimekit.context
import java.util.*

fun dismount(onDismount: () -> Unit) {
    val ctx = context()
    val uuid = UUID.randomUUID()
    var called = false
    val callback = {
        if (!called) {
            onDismount()
            called = true
        }
    }

    ctx.dismounts[uuid] = callback
}