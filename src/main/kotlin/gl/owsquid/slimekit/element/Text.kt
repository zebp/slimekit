package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.context
import gl.owsquid.slimekit.element
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.text.Text

fun Text(
    text: Text,
    textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer,
    shadow: Boolean = false,
    color: Int = -1,
) = element {
    DrawableChild(TextDrawable(text, textRenderer, shadow, color))
}

class TextDrawable(
    private val text: Text,
    private val textRenderer: TextRenderer,
    private val shadow: Boolean,
    private val color: Int,
) : Drawable, Element {
    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        context?.drawText(textRenderer, text, 5, 50, color, shadow)
    }

    override fun setFocused(focused: Boolean) = Unit

    override fun isFocused(): Boolean = false
}