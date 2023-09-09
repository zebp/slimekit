package gl.owsquid.slimekit.hooks

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.observables.dismount

fun Context.tick(
    tick: (Int) -> Unit
) {
    val tickers = screenBuilders.tickers
    tickers.add(tick)
    dismount { tickers.remove(tick) }
}

inline fun Context.every(ticks: Int, crossinline func: () -> Unit) {
    tick {
        if (it % ticks == 0) {
            func()
        }
    }
}