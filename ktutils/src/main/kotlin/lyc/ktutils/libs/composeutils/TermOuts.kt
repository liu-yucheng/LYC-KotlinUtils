// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

/** Terminal outputs. */
class TermOuts private constructor() {
    companion object {
        /** Terminal outputs text area. */
        @Composable
        private fun TermOutsTextArea() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            SelectionContainer {
                Column(Modifier.fillMaxSize().padding(8.dp)) {
                    val maxOutsIdx = States.termOuts.size - 1
                    val maxOffset = minOf(maxOutsIdx, States.termDispLines - 1)

                    for (offset in maxOffset downTo 0) {
                        val outsIdx = maxOutsIdx - offset
                        val rowText = States.termOuts[outsIdx]
                        Row {
                            Text(rowText, color = States.Theme.extColors.onTerm, fontFamily = FontFamily.Monospace)
                        } // end Row
                    } // end for
                } // end Column
            } // end SelectionContainer
        } // end fun

        /** Terminal outputs handle. */
        @Composable
        private fun TermOutsHandle() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val termOutsHandlePainter =
                painterResource(Utils.joinResPaths(Defaults.iconsResPath, Defaults.termOutsHandleIconName)) // end val

            Column(Modifier.wrapContentWidth().fillMaxHeight()) {
                Button(
                    { States.termExtended.value = !States.termExtended.value }, Modifier.size(32.dp),
                    colors = ButtonDefaults.buttonColors(States.Theme.extColors.term, States.Theme.extColors.onTerm),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        termOutsHandlePainter, "Terminal outputs handle", Modifier.size(16.dp),
                        MaterialTheme.colors.onSurface
                    ) // end Icon
                } // end Button
            } // end Column
        } // end fun

        /** Extended terminal outputs. */
        @Composable
        private fun ExtendedTermOuts() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val goToBottomPainter =
                painterResource(Utils.joinResPaths(Defaults.iconsResPath, Defaults.goToBottomIconName)) // end val

            val clearOutsPainter =
                painterResource(Utils.joinResPaths(Defaults.iconsResPath, Defaults.clearOutsIconName)) // end val

            Row(Modifier.fillMaxSize()) {
                TermOutsHandle()
                Spacer(Modifier.size(8.dp))

                Column(Modifier.weight(1f), Arrangement.SpaceAround) {
                    Text(
                        "Terminal outputs:", Modifier.wrapContentSize().align(Alignment.Start),
                        MaterialTheme.colors.onBackground
                    ) // end Text

                    Spacer(Modifier.size(8.dp))

                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(4.dp)).background(States.Theme.extColors.term)
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize().horizontalScroll(States.termHoriScroll)
                                .verticalScroll(States.termVertScroll) // end Modifier
                        ) { TermOutsTextArea() } // end Box

                        HorizontalScrollbar(
                            rememberScrollbarAdapter(States.termHoriScroll),
                            Modifier.fillMaxWidth().height(16.dp).align(Alignment.BottomCenter)
                        ) // end HorizontalScrollbar

                        VerticalScrollbar(
                            rememberScrollbarAdapter(States.termVertScroll),
                            Modifier.width(16.dp).fillMaxHeight().align(Alignment.CenterEnd)
                        ) // end VerticalScrollbar
                    } // end Box

                    Spacer(Modifier.size(8.dp))

                    Row(Modifier.fillMaxWidth().wrapContentHeight(), Arrangement.SpaceAround) {
                        Button({ States.termVertGoToBottom.value = true }) {
                            Icon(
                                goToBottomPainter, "Go to bottom icon", Modifier.size(16.dp),
                                MaterialTheme.colors.onPrimary
                            ) // end Icon

                            Spacer(Modifier.size(8.dp))
                            Text("Go to bottom")
                        } // end Button

                        Button(
                            { States.termOuts.clear() },

                            colors = ButtonDefaults.buttonColors(
                                States.Theme.extColors.warn, States.Theme.extColors.onWarn
                            ) // end colors
                        ) {
                            Icon(
                                clearOutsPainter, "Clear outputs icon", Modifier.size(16.dp),
                                States.Theme.extColors.onWarn
                            ) // end Icon

                            Spacer(Modifier.size(8.dp))
                            Text("Clear outputs")
                        } // end Button
                    } // end Row

                    Row(Modifier.fillMaxWidth().wrapContentHeight(), Arrangement.SpaceAround) {
                        OutlinedButton(
                            {
                                States.termPlaceboClicks.value += 1
                                Funcs.logln("Clicked placebo  Count: ${States.termPlaceboClicks.value}")
                            }, // end onClick

                            border = BorderStroke(1.dp, MaterialTheme.colors.primary),

                            colors = ButtonDefaults.outlinedButtonColors(
                                States.Theme.transparentColor, MaterialTheme.colors.primary
                            ) // end colors
                        ) { Text("Placebo") } // end Button

                        OutlinedButton(
                            {
                                States.termPlaceboClicks.value = 0
                                Funcs.logln("Completed resetting placebo")
                            }, // end onClick

                            border = BorderStroke(1.dp, States.Theme.extColors.warn),

                            colors = ButtonDefaults.outlinedButtonColors(
                                States.Theme.transparentColor, States.Theme.extColors.warn
                            ) // end colors
                        ) { Text("Reset placebo") } // end Button
                    } // end Row
                } // end Column
            } // end Row

            LaunchedEffect(States.termVertGoToBottom.value) {
                States.termVertScroll.scrollTo(States.termVertBottomPos)
                States.termVertGoToBottom.value = false
            } // end LaunchedEffect

            LaunchedEffect(States.termVertScroll.value) {
                States.termVertAtBottom.value = States.termVertScroll.value >= States.termVertBottomPos
            } // end LaunchedEffect

            LaunchedEffect(States.termOutsContentDigest) {
                if (States.termVertAtBottom.value) {
                    States.termVertScroll.scrollTo(States.termVertBottomPos)
                } // end if
            } // end LaunchedEffect
        } // end fun

        /** Terminal outputs. */
        @Composable
        fun TermOuts() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            if (States.termExtended.value) {
                ExtendedTermOuts()
            } else {
                TermOutsHandle()
            } // end if
        } // end fun
    } // end companion
} // end class
