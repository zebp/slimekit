package gl.owsquid.slimekit.observables

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Computed<T>(private inline val compute: () -> T) : ReadOnlyProperty<Any?, T> {
    private var dirty = true
    private var dirtyValue: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = if (dirty) {
        if (currentSignalObserver != null) {
            throw IllegalStateException("Cannot compute when already within a Compute")
        }

        currentSignalObserver = SignalObserver {
            this.markDirty()
            false
        }

        val value = compute()
        this.dirtyValue = value
        this.dirty = false

        currentSignalObserver = null

        value
    } else {
        dirtyValue!!
    }

    private fun markDirty() {
        this.dirty = true
    }
}
