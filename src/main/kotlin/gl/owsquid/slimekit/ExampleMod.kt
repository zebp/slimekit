package gl.owsquid.slimekit

import gl.owsquid.slimekit.ExampleMod.logger
import gl.owsquid.slimekit.element.*
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
import kotlin.time.Duration.Companion.seconds


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
                val screen = SlimekitScreen(Text.literal("lol!")) { TabbedScreen() }
                MinecraftClient.getInstance().setScreen(screen)
            }
        })
    }
}

fun TabbedScreen() = element {
    var tab by Signal(0)

    Screen {
        TabButton(tabIndex = 0) { tab = 0 }
        TabButton(tabIndex = 1) { tab = 1 }
        TabButton(tabIndex = 2) { tab = 2 }

        Fragment {
            when (tab) {
                0 -> FirstTab()
                1 -> SecondTab()
                2 -> ThirdTab()
            }
        }
    }
}

fun FirstTab() = element {
    var count by Signal(0)

    Fragment {
        Button(
            text = "Increment $count".literal,
            onClick = { count++ },
            position = ButtonPosition(y = 45)
        )
    }
}

fun SecondTab() = element {
    var time by Signal(Date())

    every(1.seconds) { time = Date() }

    Fragment {
        Text(
            text = "The time is $time".literal,
            shadow = true,
        )
    }
}

fun ThirdTab() = element {
    val textSignal = Signal("")
    val text by textSignal

    Fragment {
        Fragment {
            Text(
                text = "You typed \"$text\"".literal,
                shadow = true,
            )
        }
        TextField(textSignal = textSignal, placeholder = "Placeholder".literal)
    }
}

fun TabButton(
    tabIndex: Int = 0,
    onClick: () -> Unit,
) = element {
    Button(
        text = "Tab ${tabIndex + 1}".literal,
        onClick = onClick,
        position = ButtonPosition(x = 160 * tabIndex + 5, y = 5)
    )
}