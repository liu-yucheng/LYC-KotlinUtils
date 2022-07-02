// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.composeutils

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/** Terminal outputs. */
val termOuts = mutableStateListOf<String>()

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
                        rowText, color = CustTheme.extColors.termFg,
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

    Column(Modifier.fillMaxSize(), Arrangement.SpaceAround) {
        Text(
            "Terminal emulator outputs:", Modifier.wrapContentSize().align(Alignment.Start),
            MaterialTheme.colors.onBackground
        ) // end Text

        Box(
            Modifier
                .fillMaxWidth().fillMaxHeight(0.8f).background(CustTheme.extColors.termBg)
        ) {
            Box(
                Modifier.fillMaxSize().horizontalScroll(termHoriScroll)
                    .verticalScroll(termVertScroll)
            ) {
                TermOutsTextArea()
            } // end Box

            HorizontalScrollbar(
                rememberScrollbarAdapter(termHoriScroll),
                Modifier.fillMaxWidth().height(16.dp).align(Alignment.BottomCenter)
            ) // end HorizontalScrollbar

            VerticalScrollbar(
                rememberScrollbarAdapter(termVertScroll),
                Modifier.width(16.dp).fillMaxHeight().align(Alignment.CenterEnd)
            ) // end VerticalScrollbar
        } // end Box

        Row(Modifier.fillMaxWidth().wrapContentHeight(), Arrangement.SpaceAround) {
            Button({ termVertGoToBottom.value = true }) { Text("Go to bottom") }
            Button({ termOuts.clear() }) { Text("Clear outputs") }
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