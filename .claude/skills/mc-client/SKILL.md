---
name: mc-client
description: Guía de arquitectura para el cliente MinLauncher (Minecraft 26.1.2, Java 25, Fabric/Loom 1.17, Mojang Official Names, sin Yarn).
---

# MinLauncher Client — Architecture & Dev Skill

Cliente de Minecraft personalizado (PvP + Survival), estilo Lunar/Badlion, construido sobre:
**Minecraft 26.1.2 · Java 25 · Fabric Loader 0.19.5 · fabric-loom 1.17.20 (plugin `net.fabricmc.fabric-loom` / NoRemap) · Fabric API 0.155.2+26.1.2 · Mojang Official Names (el client.jar YA viene pre-nombrado; NO existe Yarn para 26.x).**

## 1. Datos CRÍTICOS del toolchain 26.x (verificado en client-26.1.2.jar)
- **No existen `client_mappings.txt` ni Yarn**. Loom NoRemap (`id 'net.fabricmc.fabric-loom'`) evita `officialMojangMappings()` y la config `mappings` (que FALLA: "no dependencies"). Con NoRemap las deps de mods van como `implementation`, NO `modImplementation` (esa config no existe).
- **Gradle 9.5.1 + Java 25** (Temurin 25.0.4 en `org.gradle.java.home`).
- **Coordenadas 26.x** (fabla): `net.minecraft.resources.Identifier` (factory `Identifier.fromNamespaceAndPath`); component de texto = `net.minecraft.network.chat.Component`/`MutableComponent` (`Component.literal`); `Minecraft.getInstance()`, `.options`, `.font`, `.screen`, `.gameDirectory`, `.getFps()`, `.setScreen(..)`; `Options.keyUp/keyLeft/keyDown/keyRight/keyJump/keyAttack/keyUse/keyShift` son `KeyMapping` con `.isDown()`; `Options.fov()/gamma()` son `OptionInstance<T>` con `.set(..)`.

## 2. RENDER/HUD en 26.x (¡cambió el pipeline!)
- **Ya NO existe `GuiGraphics` ni `InGameHud` ni `RenderTickCounter`.** El HUD ahora es *render-state extraction*:
  - `net.minecraft.client.gui.Gui` → hook de HUD: `extractRenderState(GuiGraphicsExtractor, DeltaTracker)` (inyectar `@Inject method="extractRenderState" @At("TAIL")`).
  - El "lienzo" es `GuiGraphicsExtractor` (`guiWidth()/guiHeight()`, `fill(...)`, `text(Font,String,int,int,int,boolean)`, `horizontalLine`, `verticalLine`).
  - `DeltaTracker.getGameTimeDeltaPartialTick(boolean)` da el delta.
- **PlayerTabOverlay** (ex PlayerListHud): hook de insignias en TabList → `@Inject method="getNameForDisplay" at=@At("RETURN")` sobre `(PlayerInfo, CallbackInfoReturnable<Component>)`; UUID del jugador: `entry.getProfile().id()` (GameProfile record-style).
- **Render de jugadores**: ya no hay `PlayerRenderer`; hay `AvatarRenderer<...>` en `net.minecraft.client.renderer.entity.player` (con `EntityRendererProvider$Context`). Cosméticos 3D (capas/alas/gorros) pendientes de port a este pipeline.
- **Eventos de entrada** (record-based): `KeyboardHandler.keyPress(long, int, KeyEvent)` es private (mixinear en HEAD; `KeyEvent.input() == 1` = GLFW_PRESS); `Screen` ya no tiene `render(DrawContext,..)` → override `public void extractRenderState(GuiGraphicsExtractor, int mouseX, int mouseY, float delta)` + `super.extractRenderState(...)`; clics: `boolean mouseClicked(MouseButtonEvent, boolean captured)`, `mouseReleased(MouseButtonEvent)`, `mouseDragged(MouseButtonEvent, double, double)`; `event.x()/y()/button()`. `isPauseScreen()` para que no pause.

## 3. Estado del proyecto (2026-09-05)
- **Funciona**: compila (`gradle build`) y arranca (`gradle runClient`) → "MinLauncher v1.0.0 inicializado (Minecraft 26.1.2, Java 25)" + menú principal OK.
- **Incluido**: red de módulos + settings (Boolean/Number/Mode/Color); HUD (Keystrokes, CPS, FPS, ArmorStatus) dibujado en `Gui.extractRenderState`; PvP (ToggleSprint) y Survival (Fullbright, Zoom via gamma/fov); cosméticos con UI + insignias en TabList (`PlayerTabOverlayMixin`); config JSON en `.minecraft/minlauncher/config.json`; apertura ClickGUI con **RSHIFT**.
- **Pendiente**: render 3D de capas/alas/gorros sobre `AvatarRenderer` (research del pipeline `SubmitNodeCollector`/`AvatarRenderState`); integración con launcher externo (perfil Fabric 26.1.2); textos de cosméticos/badges (assets).
- **Repos**: `.port/` contiene el código original Yarn (referencia de lógica); el código vivo está en `src/main/java/net/minlauncher/client/`.

## 4. Reglas (YAGNI & Rendimiento)
- Cero deps nuevas: usar `com.google.code.gson` ya presente.
- Mixins quirúrgicos (`@Inject` HEAD/TAIL/RETURN), nunca `@Overwrite`.
- El render HUD se hace UNA vez por frame en el mixin de `Gui`; `try/catch` por módulo para que un fallo de UI no crashee el juego.
- Guard de null en todo render: `mc.player`/`mc.options` pueden no estar inicializados.