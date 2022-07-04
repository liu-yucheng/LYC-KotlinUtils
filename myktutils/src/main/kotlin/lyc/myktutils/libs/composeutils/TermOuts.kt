// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.composeutils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import lyc.myktutils.libs.gputils.Logs
import lyc.myktutils.libs.gputils.Reses

/** Terminal outputs. */
class TermOuts {
    companion object {
        /** Terminal outputs. */
        val termOuts = mutableStateListOf<String>()

        /** All logs. */
        val allLogs = arrayListOf<Any>(termOuts)

        /** Terminal outputs content digest. */
        val termOutsContentDigest: Int
            get() {
                // Part of liu-yucheng/MyKotlinUtils
                // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
                // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

                var result = 0

                for ((index, elem) in termOuts.withIndex()) {
                    result = result xor index.hashCode()
                    result = result xor elem.hashCode()
                } // end for

                return result
            } // end get

        /** Terminal display line count. */
        const val termDispLines = 8192

        /** Terminal horizontal scroll. */
        val termHoriScroll = ScrollState(0)

        /** Terminal vertical scroll. */
        val termVertScroll = ScrollState(0)

        /** Terminal vertical scroll bottom position. */
        val termVertBottomPos: Int
            get() = maxOf(termVertScroll.maxValue - 4, 0)

        /** Terminal vertical scroll: whether at bottom. */
        val termVertAtBottom = mutableStateOf(true)

        /** Terminal vertical scroll: whether to go to bottom. */
        var termVertGoToBottom = mutableStateOf(false)

        /** Terminal placebo clicks. */
        val termPlaceboClicks = mutableStateOf(0)

        /** Icons resource path. */
        const val iconsResPath = "icons"

        /** Go to bottom icon name. */
        const val goToBottomIconName = "border-bottom-variant.svg"

        /** Clear outputs icon name. */
        const val clearOutsIconName = "backspace-outline.svg"

        /** Terminal outputs text area. */
        @Composable
        private fun TermOutsTextArea() {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            SelectionContainer {
                Column(Modifier.fillMaxSize().padding(8.dp)) {
                    val maxOutsIdx = termOuts.size - 1
                    val maxOffset = minOf(maxOutsIdx, termDispLines - 1)

                    for (offset in maxOffset downTo 0) {
                        val outsIdx = maxOutsIdx - offset
                        val rowText = termOuts[outsIdx]
                        Row {
                            Text(
                                rowText, color = Themes.CustTheme.extColors.onTerm,
                                fontFamily = FontFamily.Monospace
                            )
                        } // end Row
                    } // end for
                } // end Column
            } // end SelectionContainer
        } // end fun

        /** Terminal outputs. */
        @Composable
        fun TermOuts() {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val goToBottomPainter = painterResource(Reses.joinResPaths(iconsResPath, goToBottomIconName))
            val clearOutsPainter = painterResource(Reses.joinResPaths(iconsResPath, clearOutsIconName))

            Column(Modifier.fillMaxSize(), Arrangement.SpaceAround) {
                Text(
                    "Terminal emulator outputs:", Modifier.wrapContentSize().align(Alignment.Start),
                    MaterialTheme.colors.onBackground
                ) // end Text

                Spacer(Modifier.size(8.dp))

                Box(
                    Modifier
                        .weight(1f).clip(RoundedCornerShape(4.dp)).background(Themes.CustTheme.extColors.term)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize().horizontalScroll(termHoriScroll)
                            .verticalScroll(termVertScroll) // end Modifier
                    ) { TermOutsTextArea() } // end Box

                    HorizontalScrollbar(
                        rememberScrollbarAdapter(termHoriScroll),
                        Modifier.fillMaxWidth().height(16.dp).align(Alignment.BottomCenter)
                    ) // end HorizontalScrollbar

                    VerticalScrollbar(
                        rememberScrollbarAdapter(termVertScroll),
                        Modifier.width(16.dp).fillMaxHeight().align(Alignment.CenterEnd)
                    ) // end VerticalScrollbar
                } // end Box

                Spacer(Modifier.size(8.dp))

                Row(Modifier.fillMaxWidth().wrapContentHeight(), Arrangement.SpaceAround) {
                    Button({ termVertGoToBottom.value = true }) {
                        Icon(
                            goToBottomPainter, "Go to bottom icon", Modifier.size(16.dp),
                            MaterialTheme.colors.onPrimary
                        ) // end Icon

                        Spacer(Modifier.size(8.dp))
                        Text("Go to bottom")
                    } // end Button

                    Button(
                        { termOuts.clear() },

                        colors = ButtonDefaults.buttonColors(
                            Themes.CustTheme.extColors.warn, Themes.CustTheme.extColors.onWarn
                        ) // end colors
                    ) {
                        Icon(
                            clearOutsPainter, "Clear outputs icon", Modifier.size(16.dp),
                            Themes.CustTheme.extColors.onWarn
                        ) // end Icon

                        Spacer(Modifier.size(8.dp))
                        Text("Clear outputs")
                    } // end Button
                } // end Row

                Row(Modifier.fillMaxWidth().wrapContentHeight(), Arrangement.SpaceAround) {
                    OutlinedButton(
                        {
                            termPlaceboClicks.value += 1
                            Logs.logln(allLogs, "Clicked placebo  Count: ${termPlaceboClicks.value}")
                        }, // end onClick

                        border = BorderStroke(1.dp, MaterialTheme.colors.primary),

                        colors = ButtonDefaults.outlinedButtonColors(
                            Themes.CustTheme.transparentColor, MaterialTheme.colors.primary
                        ) // end colors
                    ) { Text("Placebo") } // end Button

                    OutlinedButton(
                        {
                            termPlaceboClicks.value = 0
                            Logs.logln(allLogs, "Completed resetting placebo")
                        }, // end onClick

                        border = BorderStroke(1.dp, Themes.CustTheme.extColors.warn),

                        colors = ButtonDefaults.outlinedButtonColors(
                            Themes.CustTheme.transparentColor, Themes.CustTheme.extColors.warn
                        ) // end colors
                    ) { Text("Reset placebo") } // end Button
                } // end Row
            } // end Column

            LaunchedEffect(termVertGoToBottom.value) {
                termVertScroll.scrollTo(termVertBottomPos)
                termVertGoToBottom.value = false
            } // end LaunchedEffect

            LaunchedEffect(termVertScroll.value) {
                termVertAtBottom.value = termVertScroll.value >= termVertBottomPos
            } // end LaunchedEffect

            LaunchedEffect(termOutsContentDigest) {
                if (termVertAtBottom.value) {
                    termVertScroll.scrollTo(termVertBottomPos)
                } // end if
            } // end LaunchedEffect
        } // end fun
    } // end companion
} // end class
