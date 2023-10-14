package gl.owsquid.slimekit

import gl.owsquid.slimekit.ExampleMod.logger
import gl.owsquid.slimekit.observables.Signal
import gl.owsquid.slimekit.screen.ScreenBuilders
import net.minecraft.text.Text
import java.util.*

var contexts = Stack<Context>()

fun context(): Context {
    val currentContext = maybeContext()
    if (currentContext != null) {
        return currentContext
    } else {
        throw IllegalStateException("how")
    }
}

fun maybeContext(): Context? = try {
    contexts.peek()
} catch (_: EmptyStackException) {
    null
}

sealed class Context(open val screenBuilders: ScreenBuilders) {
    internal val dismounts: MutableMap<UUID, (causedBy: Signal<*>?) -> Unit> = mutableMapOf()
    internal val children: MutableList<Context> = mutableListOf()
    private val observations = mutableMapOf<Signal<*>, UUID>()
    var dead = false
    var name = "unnamed"

    data class Root(override val screenBuilders: ScreenBuilders) : Context(screenBuilders)

    data class Populated(
        val parent: Context,
        val rebuild: Context.() -> Unit,
        override val screenBuilders: ScreenBuilders = parent.screenBuilders,
    ) : Context(screenBuilders) {
        init {
            parent.children.add(this)
        }
    }

    fun dismount(causedBy: Signal<*>? = null) {
        dismounts.values.forEach { it(causedBy) }
        dismounts.clear()

        children.forEach {
            it.dismount()
        }
    }

    val Int.seconds
        get() = this * 20

    val String.literal: Text
        get() = Text.literal(this)

    inline fun Fragment(crossinline init: Context.() -> Unit) = element {
        this.init()
    }

    fun observesSignal(signal: Signal<*>) {
        observations[signal] = signal.observe {
            recompute(signal)
            true
        }
    }

    private fun recompute(causedBy: Signal<*>? = null) {
        if (dead) {
            logger.error("TRIED TO RECOMPUTE DEAD CONTEXT! (Context: $name)")
            return
        }

        dismount(causedBy)

        if (this is Populated) {
            val freshContext = Populated(
                parent = parent,
                rebuild = rebuild,
            )

            contexts.push(freshContext)
            rebuild(freshContext)
            contexts.pop()

            val childIndex = parent.children.indexOf(this)
            parent.children[childIndex] = freshContext
            dead = true
        }

        observations.forEach { (signal, id) ->
            signal.removeObservation(id)
        }
    }
}

fun element(build: Context.() -> Unit) {
    val current = context()

    if (current.name == "unnamed") {
        current.name = StackWalker.getInstance()
            .walk {
                it.dropWhile { method ->
                    method.methodName.first().isLowerCase()
                }.toList()
            }.first().methodName
    }

    val ctx = Context.Populated(
        parent = current,
        rebuild = build
    )

    contexts.push(ctx)
    ctx.build()
    contexts.pop()
}
