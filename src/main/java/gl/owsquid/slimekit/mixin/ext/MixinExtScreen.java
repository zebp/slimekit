package gl.owsquid.slimekit.mixin.ext;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface MixinExtScreen {
//    @Invoker("addDrawable")
//    <T extends Drawable> T addDrawable(T drawable);

//    @Invoker("addDrawableChild")
//    <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);

//    @Invoker("addSelectableChild")
//    <T extends Element & Selectable> T addSelectableChild(T child);

//    @Invoker("remove")
//    void remove(Element child);
}

