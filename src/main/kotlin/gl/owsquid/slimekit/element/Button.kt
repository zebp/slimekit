package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.Context
import gl.owsquid.slimekit.context
import gl.owsquid.slimekit.element
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

data class ButtonPosition(val x: Int = 5, val y: Int = 20)

fun Button(
    onClick: () -> Unit,
    text: Text,
    position: ButtonPosition = ButtonPosition()
) = element {
    val widget = ButtonWidget
        .builder(text) { onClick() }
        .position(position.x, position.y)
        .build()

    DrawableSelectableChild(widget)
}