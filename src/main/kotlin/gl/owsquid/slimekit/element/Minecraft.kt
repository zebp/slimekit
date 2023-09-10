package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.context
import gl.owsquid.slimekit.element
import gl.owsquid.slimekit.observables.dismount
import gl.owsquid.slimekit.screen.ScreenConfiguration
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable

fun <T> DrawableSelectableChild(drawableChild: T) where T: Drawable, T: Element, T: Selectable {
    val screenBuilders = context().screenBuilders
    screenBuilders.addDrawableSelectableChild(drawableChild)
    dismount { screenBuilders.remove(drawableChild) }
}

fun <T> DrawableChild(drawable: T) where T: Drawable, T: Element {
    val screenBuilders = context().screenBuilders
    screenBuilders.addDrawable(drawable)
    dismount { screenBuilders.remove(drawable) }
}

inline fun Screen(
    background: Boolean = true,
    pause: Boolean = true,
    crossinline init: Context.() -> Unit
) = element {
    screenBuilders.updateConfiguration(ScreenConfiguration(background, pause))
    dismount { screenBuilders.updateConfiguration(ScreenConfiguration()) }
    this.init()
}