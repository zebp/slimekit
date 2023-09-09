package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.ExampleMod.logger
import java.util.*

fun Context.dismount(onDismount: () -> Unit) {
    val uuid = UUID.randomUUID()
    var called = false
    val callback = {
        if (!called) {
            onDismount()
            called = true
        }
    }

    dismounts[uuid] = callback
}