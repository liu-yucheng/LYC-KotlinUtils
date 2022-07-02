// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.composeutils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import kotlin.system.exitProcess
import lyc.myktutils.libs.gputils.joinResPaths

/** Icons resource path. */
const val iconsResPath = "icons"

/** Minimize window icon name. */
const val minWinIconName = "window-minimize.svg"

/** Maximize window icon name. */
const val maxWinIconName = "window-maximize.svg"

/** Close window icon name. */
const val closeWinIconName = "window-close.svg"

/** Restore window icon name. */
const val restoreWinIconName = "window-restore.svg"

/** Main window state. */
val mainWinState = WindowState(
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
    position = WindowPosition(Alignment.Center), size = DpSize(1288.dp, 724.dp)
)

/** Exits the app. */
fun exitApp() {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
    exitProcess(0)
}

/** Custom title bar.
 * @param title: a title
 * @param iconResLoc: an icon resource location
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustTitleBar(title: String = "", iconResLoc: String = "") {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    val appIconPainter = if (iconResLoc == "") {
        null
    } else {
        painterResource(iconResLoc)
    } // end val

    val minIconLoc = joinResPaths(iconsResPath, minWinIconName)
    val maxIconLoc = joinResPaths(iconsResPath, maxWinIconName)
    val restoreIconLoc = joinResPaths(iconsResPath, restoreWinIconName)
    val closeIconLoc = joinResPaths(iconsResPath, closeWinIconName)

    val minIconPainter = painterResource(minIconLoc)
    val maxIconPainter = painterResource(maxIconLoc)
    val restoreIconPainter = painterResource(restoreIconLoc)
    val closeIconPainter = painterResource(closeIconLoc)

    val minHovering = remember { mutableStateOf(false) }
    val maxRestoreHovering = remember { mutableStateOf(false) }
    val closeHovering = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth().height(32.dp).background(MaterialTheme.colors.surface)) {
        Row(
            Modifier
                .wrapContentWidth().fillMaxHeight().align(Alignment.CenterStart)
                .padding(8.dp, 0.dp, 0.dp, 0.dp),

            Arrangement.spacedBy(8.dp), Alignment.CenterVertically
        ) {
            if (appIconPainter != null) {
                Image(appIconPainter, "App logo", Modifier.size(16.dp))
            }

            Text(title, color = MaterialTheme.colors.onSurface, fontSize = 11.sp)
        } // end Row

        Row(
            Modifier
                .wrapContentWidth().fillMaxHeight().align(Alignment.CenterEnd)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),

            Arrangement.spacedBy(0.dp), Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(48.dp).fillMaxHeight()

                    .background(
                        if (!minHovering.value) {
                            CustTheme.transparentColor
                        } else {
                            CustTheme.extColors.hoverColor
                        } // end if
                    ) // end .background

                    .onPointerEvent(PointerEventType.Enter, onEvent = { minHovering.value = true })
                    .onPointerEvent(PointerEventType.Exit, onEvent = { minHovering.value = false })
                    .clickable(onClickLabel = "Minimize") { mainWinState.isMinimized = true },

                Alignment.Center
            ) {
                Icon(
                    minIconPainter, "Minimize", Modifier.size(16.dp),
                    MaterialTheme.colors.onSurface
                ) // end Icon
            } // end Box

            Box(
                Modifier
                    .width(48.dp).fillMaxHeight()

                    .background(
                        if (!maxRestoreHovering.value) {
                            CustTheme.transparentColor
                        } else {
                            CustTheme.extColors.hoverColor
                        } // end if
                    ) // end .background

                    .onPointerEvent(PointerEventType.Enter, onEvent = { maxRestoreHovering.value = true })
                    .onPointerEvent(PointerEventType.Exit, onEvent = { maxRestoreHovering.value = false })

                    .clickable(onClickLabel = "Maximize or Restore") {
                        if (mainWinState.placement == WindowPlacement.Maximized) {
                            mainWinState.placement = WindowPlacement.Floating
                        } else {
                            mainWinState.placement = WindowPlacement.Maximized
                        } // end if
                    }, // end .clickable

                Alignment.Center
            ) {
                val iconPainter = if (mainWinState.placement == WindowPlacement.Maximized) {
                    restoreIconPainter
                } else {
                    maxIconPainter
                } // end if

                Icon(
                    iconPainter, "Maximize or Restore", Modifier.size(16.dp),
                    MaterialTheme.colors.onSurface
                ) // end Icon
            } // end Box

            Box(
                Modifier
                    .width(48.dp).fillMaxHeight()

                    .background(
                        if (!closeHovering.value) {
                            CustTheme.transparentColor
                        } else {
                            CustTheme.extColors.closeHoverColor
                        } // end if
                    ) // end .background

                    .onPointerEvent(PointerEventType.Enter, onEvent = { closeHovering.value = true })
                    .onPointerEvent(PointerEventType.Exit, onEvent = { closeHovering.value = false })
                    .clickable(onClickLabel = "Close") { exitApp() },

                Alignment.Center
            ) {
                Icon(
                    closeIconPainter, "Close", Modifier.size(16.dp),
                    MaterialTheme.colors.onSurface
                ) // end Icon
            } // end Box
        } // end Row
    } // end Box
} // end fun

/** Draggable area.
 * Part of the custom title bar.
 */
@Composable
private fun WindowScope.DraggableArea() {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    Box(
        Modifier.fillMaxWidth().height(32.dp).clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
        Alignment.TopStart
    ) {
        WindowDraggableArea(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize())
        } // end WindowDraggableArea
    } // end Box
} // end fun

/** App frame.
 * @param content: some composable contents
 */
@Composable
fun WindowScope.AppFrame(title: String = "", iconResLoc: String = "", content: @Composable () -> Unit) {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    MaterialTheme {
        Box(
            Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colors.background),
            Alignment.TopStart
        ) {
            DraggableArea() // Custom title bar
            CustTitleBar(title, iconResLoc) // Custom title bar
            Box(Modifier.fillMaxSize().padding(16.dp, 8.dp, 16.dp, 16.dp), Alignment.TopStart) {
                content()
            } // end Box
        } // end Box
    } // end States.Theme
} // end fun
