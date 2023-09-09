package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.observables.dismount
import gl.owsquid.slimekit.screen.ScreenConfiguration
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable

fun <T> DrawableSelectableChild(ctx: Context, drawableChild: T) where T: Drawable, T: Element, T: Selectable {
    val screenBuilders = ctx.screenBuilders
    screenBuilders.addDrawableSelectableChild(drawableChild)
    ctx.dismount { screenBuilders.remove(drawableChild) }
}

fun <T> DrawableChild(ctx: Context, drawable: T) where T: Drawable, T: Element {
    val screenBuilders = ctx.screenBuilders
    screenBuilders.addDrawable(drawable)
    ctx.dismount {
        screenBuilders.remove(drawable)
    }
}

inline fun Screen(
    ctx: Context,
    background: Boolean = true,
    pause: Boolean = true,
    crossinline init: Context.() -> Unit
) = ctx.element {
    screenBuilders.updateConfiguration(ScreenConfiguration(background, pause))
    dismount { screenBuilders.updateConfiguration(ScreenConfiguration()) }

    this.init()
}