package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.ExampleMod.logger
import gl.owsquid.slimekit.element
import gl.owsquid.slimekit.maybeContext
import gl.owsquid.slimekit.observables.Signal
import gl.owsquid.slimekit.observables.dismount
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text

fun TextField(
    textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer,
    textSignal: Signal<String>,
    placeholder: Text,
) = element {
    var text by textSignal
    val currentText = textSignal.currentValue()
    val widget = TextFieldWidget(textRenderer, 5, 100, 200, 20, currentText.literal)

    widget.text = currentText
    widget.setChangedListener {
        val ctx = maybeContext()
        text = it
    }
    widget.setPlaceholder(placeholder)

    dismount {
        logger.info("WE DISMOUNTED!")
    }

    DrawableSelectableChild(widget)
}