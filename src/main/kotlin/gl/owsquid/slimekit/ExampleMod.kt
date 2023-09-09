package gl.owsquid.slimekit

import gl.owsquid.slimekit.ExampleMod.logger
import gl.owsquid.slimekit.element.Button
import gl.owsquid.slimekit.element.ButtonPosition
import gl.owsquid.slimekit.element.Screen
import gl.owsquid.slimekit.element.Text
import gl.owsquid.slimekit.hooks.every
import gl.owsquid.slimekit.observables.Signal
import gl.owsquid.slimekit.observables.dismount
import gl.owsquid.slimekit.observables.effect
import gl.owsquid.slimekit.screen.SlimekitScreen
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory
import java.util.*


object ExampleMod : ModInitializer {
    val logger = LoggerFactory.getLogger("slimekit")

    lateinit var debugKey: KeyBinding
    override fun onInitialize() {
        logger.info("Hello Fabric world!")

        debugKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.examplemod.spook",  // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM,  // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R,  // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            while (debugKey.wasPressed()) {
                val screen = SlimekitScreen(Text.literal("lol!")) { TabbedScreen(it) }
                MinecraftClient.getInstance().setScreen(screen)
            }
        })
    }
}

fun CounterScreen(ctx: Context) = ctx.element {
    var count by signal(0)
    var seconds by signal(0)

    effect { logger.info("Count is now $count") }

    every(1.seconds) { seconds++ }

    Screen(ctx, pause = false) {
        Text(
            ctx = this,
            text = "Clicked $count times ($seconds seconds)".literal,
            shadow = true,
        )

        Button(
            ctx = this,
            text = "Increment count".literal,
            onClick = { count++ },
        )
    }
}

fun TabbedScreen(ctx: Context) = ctx.element {
    var tab by Signal(0)

    Screen(ctx) {
        TabButton(ctx = this, 0) { tab = 0 }
        TabButton(ctx = this, 1) { tab = 1 }

        Fragment {
            when (tab) {
                0 -> FirstTab(ctx = this)
                1 -> SecondTab(ctx = this)
            }
        }
    }
}

fun FirstTab(ctx: Context) = ctx.element {
    var count by signal(0)

    Fragment {
        Button(
            ctx = this,
            text = "Increment $count".literal,
            onClick = { count++ },
            position = ButtonPosition(y = 45)
        )
    }
}

fun SecondTab(ctx: Context) = ctx.element {
    var time by signal(Date())

    every(1.seconds) { time = Date() }

    Fragment {
        Text(
            ctx = this,
            text = "The time is $time".literal,
            shadow = true,
        )
    }
}

fun TabButton(
    ctx: Context,
    tabIndex: Int = 0,
    onClick: () -> Unit,
) = ctx.element {
    Button(
        ctx = this,
        text = "Tab ${tabIndex + 1}".literal,
        onClick = onClick,
        position = ButtonPosition(x = 160 * tabIndex + 5, y = 5)
    )
}