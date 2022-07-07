// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

/** Custom title bars. */
class TitleBars private constructor() {
    companion object {
        /** Custom title bar.
         * @param title: a title
         * @param iconResLoc: an icon resource location
         */
        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        fun CustTitleBar(onCloseClicked: () -> Unit, title: String = "", iconResLoc: String = "") {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val appIconPainter = if (iconResLoc == "") {
                null
            } else {
                painterResource(iconResLoc)
            } // end val

            val minIconLoc = Utils.joinResPaths(Defaults.iconsResPath, Defaults.minWinIconName)
            val maxIconLoc = Utils.joinResPaths(Defaults.iconsResPath, Defaults.maxWinIconName)
            val restoreIconLoc = Utils.joinResPaths(Defaults.iconsResPath, Defaults.restoreWinIconName)
            val closeIconLoc = Utils.joinResPaths(Defaults.iconsResPath, Defaults.closeWinIconName)

            val minIconPainter = painterResource(minIconLoc)
            val maxIconPainter = painterResource(maxIconLoc)
            val restoreIconPainter = painterResource(restoreIconLoc)
            val closeIconPainter = painterResource(closeIconLoc)

            val minHovering = remember { mutableStateOf(false) }
            val maxRestoreHovering = remember { mutableStateOf(false) }
            val closeHovering = remember { mutableStateOf(false) }

            Box(Modifier.fillMaxWidth().height(32.dp).background(MaterialTheme.colors.background)) {
                Row(
                    Modifier
                        .wrapContentWidth().fillMaxHeight().align(Alignment.CenterStart)
                        .padding(8.dp, 0.dp, 0.dp, 0.dp), // end Modifier

                    Arrangement.spacedBy(8.dp), Alignment.CenterVertically
                ) {
                    if (appIconPainter != null) {
                        Image(appIconPainter, "App logo", Modifier.size(16.dp))
                    }

                    Text(title, color = MaterialTheme.colors.onBackground, fontSize = 11.sp)
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
                                if (minHovering.value) {
                                    Themes.CustTheme.extColors.hover
                                } else {
                                    Themes.CustTheme.transparentColor
                                } // end if
                            ) // end .background

                            .onPointerEvent(PointerEventType.Enter, onEvent = { minHovering.value = true })
                            .onPointerEvent(PointerEventType.Exit, onEvent = { minHovering.value = false })
                            .clickable(onClickLabel = "Minimize") { States.mainWinState.isMinimized = true },
                        // end Modifier

                        Alignment.Center
                    ) {
                        val iconColor = if (minHovering.value) {
                            Themes.CustTheme.extColors.onHover
                        } else {
                            MaterialTheme.colors.onBackground
                        } // end if

                        Icon(minIconPainter, "Minimize icon", Modifier.size(16.dp), iconColor)
                    } // end Box

                    Box(
                        Modifier
                            .width(48.dp).fillMaxHeight()

                            .background(
                                if (maxRestoreHovering.value) {
                                    Themes.CustTheme.extColors.hover
                                } else {
                                    Themes.CustTheme.transparentColor
                                } // end if
                            ) // end .background

                            .onPointerEvent(PointerEventType.Enter, onEvent = { maxRestoreHovering.value = true })
                            .onPointerEvent(PointerEventType.Exit, onEvent = { maxRestoreHovering.value = false })

                            .clickable(onClickLabel = "Maximize or restore") {
                                if (States.mainWinState.placement == WindowPlacement.Maximized) {
                                    States.mainWinState.placement = WindowPlacement.Floating
                                } else {
                                    States.mainWinState.placement = WindowPlacement.Maximized
                                } // end if
                            }, // end .clickable

                        Alignment.Center
                    ) {
                        val iconPainter = if (States.mainWinState.placement == WindowPlacement.Maximized) {
                            restoreIconPainter
                        } else {
                            maxIconPainter
                        } // end if

                        val iconColor = if (maxRestoreHovering.value) {
                            Themes.CustTheme.extColors.onHover
                        } else {
                            MaterialTheme.colors.onBackground
                        } // end if

                        Icon(iconPainter, "Maximize or restore icon", Modifier.size(16.dp), iconColor)
                    } // end Box

                    Box(
                        Modifier
                            .width(48.dp).fillMaxHeight()

                            .background(
                                if (closeHovering.value) {
                                    Themes.CustTheme.extColors.errHover
                                } else {
                                    Themes.CustTheme.transparentColor
                                } // end if
                            ) // end .background

                            .onPointerEvent(PointerEventType.Enter, onEvent = { closeHovering.value = true })
                            .onPointerEvent(PointerEventType.Exit, onEvent = { closeHovering.value = false })
                            .clickable(onClickLabel = "Close") { onCloseClicked() }, // end Modifier

                        Alignment.Center
                    ) {
                        val iconColor = if (closeHovering.value) {
                            Themes.CustTheme.extColors.onErrHover
                        } else {
                            MaterialTheme.colors.onBackground
                        } // end if

                        Icon(closeIconPainter, "Close icon", Modifier.size(16.dp), iconColor)
                    } // end Box
                } // end Row
            } // end Box
        } // end fun

        /** Draggable area.
         * Part of the custom title bar.
         */
        @Composable
        private fun WindowScope.DraggableArea() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            Box(
                Modifier.fillMaxWidth().height(32.dp).clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
                Alignment.TopStart
            ) {
                WindowDraggableArea(Modifier.fillMaxSize()) { Box(Modifier.fillMaxSize()) } // end WindowDraggableArea
            } // end Box
        } // end fun

        /** App frame.
         * @param content: some composable contents
         */
        @Composable
        fun WindowScope.AppFrame(
            onCloseClicked: () -> Unit, title: String = "", iconResLoc: String = "", content: @Composable () -> Unit
        ) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            States.Theme {
                Box(
                    Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colors.background),
                    Alignment.TopStart
                ) {
                    Box(
                        Modifier
                            .fillMaxSize().clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                            .background(MaterialTheme.colors.background)
                            .padding(16.dp, 32.dp, 16.dp, 16.dp), // end Modifier

                        Alignment.TopStart
                    ) { content() } // end Box

                    DraggableArea() // Custom title bar
                    CustTitleBar(onCloseClicked, title, iconResLoc) // Custom title bar
                } // end Box
            } // end States
        } // end fun
    } // end companion
} // end class
