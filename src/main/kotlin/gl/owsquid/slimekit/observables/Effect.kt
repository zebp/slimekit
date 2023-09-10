package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.contexts
import java.util.*

fun effect(func: () -> Unit) {
    val signalObserver = SignalObserver {
        effect(func)
        true
    }

    val tmpContexts = contexts

    contexts = Stack()
    currentSignalObserver = signalObserver
    func()
    currentSignalObserver = null
    contexts = tmpContexts
}