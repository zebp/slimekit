package gl.owsquid.slimekit.observables

import gl.owsquid.slimekit.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal var currentSignalObserver: SignalObserver? = null

internal data class SignalObserver(
    val observed: () -> Boolean,
)

class Signal<T>(initialValue: T) : ReadWriteProperty<Any?, T> {
    private var value = initialValue
    private var observers = HashMap<UUID, SignalObserver>()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val self = this

        currentSignalObserver?.apply {
            observers[UUID.randomUUID()] = this
        }
        maybeContext()?.apply { observesSignal(self) }

        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val oldValue = this.value
        this.value = value

        if (value == oldValue) {
            return
        }

        val oldObservers = observers
        observers = HashMap()
        oldObservers.filter { (key, value) -> !value.observed() }
            .forEach { (id, observer) -> observers[id] = observer }
    }

    fun observe(observe: () -> Boolean): UUID {
        val id = UUID.randomUUID()
        this.observers[id] = SignalObserver { observe() }
        return id
    }

    fun removeObservation(id: UUID) {
        observers.remove(id)
    }

    fun currentValue() = this.value
}
