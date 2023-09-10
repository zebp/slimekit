package gl.owsquid.slimekit.screen

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.contexts
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

data class ScreenConfiguration(
    val background: Boolean = true,
    val pause: Boolean = true,
)

data class ScreenBuilders(
    private val screen: SlimekitScreen
) {
    fun <T> addDrawable(drawable: T) where T: Drawable {
        screen.addDrawableSlimekit(drawable)
    }

    fun <T> addDrawableSelectableChild(drawableElement: T) where T: Element, T: Drawable, T: Selectable {
        screen.addDrawableSelectableChildSlimekit(drawableElement)
    }

    fun remove(child: Element) = screen.removeSlimekit(child)

    fun updateConfiguration(configuration: ScreenConfiguration) {
        screen.configuration = configuration
    }

    val tickers
        get() = screen.tickers
}

class SlimekitScreen(
    title: Text,
    val element: (ctx: Context) -> Unit,
) : Screen(title) {
    private var ticksInScreen = 0

    internal var tickers = mutableSetOf<(Int) -> Unit>()
    internal var configuration = ScreenConfiguration()
    private val context = Context.Root(ScreenBuilders(this))

    override fun init() {
        contexts.push(context)
        element(context)
        contexts.pop()
    }

    internal fun <T> addDrawableSlimekit(drawableElement: T) where T: Drawable {
        this.addDrawable(drawableElement)
    }

    internal fun <T> addDrawableSelectableChildSlimekit(drawableElement: T) where T: Element, T: Drawable, T: Selectable {
        this.addDrawableChild(drawableElement)
    }

    internal fun removeSlimekit(child: Element) {
        this.remove(child)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        if (configuration.background) {
            renderBackground(context)
        }

        super.render(context, mouseX, mouseY, delta)
    }

    override fun shouldPause(): Boolean = configuration.pause

    override fun tick() {
        super.tick()
        tickers.forEach { it(ticksInScreen) }
        ticksInScreen++
    }

    override fun removed() {
        context.dismount()
    }
}