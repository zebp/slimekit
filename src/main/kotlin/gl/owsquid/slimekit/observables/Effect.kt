package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.currentContext

fun effect(func: () -> Unit) {
    val signalObserver = SignalObserver {
        effect(func)
        true
    }

    val ctx = currentContext

    currentContext = null
    currentSignalObserver = signalObserver
    func()
    currentSignalObserver = null
    currentContext = ctx
}