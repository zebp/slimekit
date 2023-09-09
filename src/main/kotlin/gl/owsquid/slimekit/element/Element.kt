package gl.owsquid.slimekit.element

import gl.owsquid.slimekit.Context
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Selectable
import java.util.Stack

data class Measurements(
    val width: Int,
    val height: Int,
)

interface Layouter

object NoopLayouter : Layouter

interface Element {
    fun measurements(): Measurements

    fun layouter(): Layouter = NoopLayouter
}
