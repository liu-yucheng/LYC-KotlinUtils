// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.envs

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import lyc.ktutils.libs.composeutils.Themes

/** States. */
class States private constructor() {
    companion object {
        /** Main window state. */
        val mainWinState = WindowState(position = WindowPosition(Alignment.Center), size = DpSize(1288.dp, 724.dp))

        /** Log location. */
        val logLoc = Utils.joinPaths(Defaults.appDataPath, Defaults.logName)

        /** Log file. */
        private val logFile = File(logLoc)

        /** Log writer.
         * NOTE: Please remember to manually close the writer after finishing writing to it.
         */
        val logWriter = PrintWriter(FileOutputStream(logFile, true))

        /** Terminal outputs. */
        val termOuts = mutableStateListOf<String>()

        /** All logs. */
        val allLogs = arrayListOf<Any>(termOuts, logWriter)

        /** Terminal outputs content digest. */
        val termOutsContentDigest: Int
            get() {
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

        /** Whether to enable dark theme. */
        val darkEnabled = mutableStateOf(true)

        /** Compose theme. */
        @Composable
        fun Theme(content: @Composable () -> Unit) {
            if (darkEnabled.value) {
                Themes.DarkTheme(content)
            } else {
                Themes.LightTheme(content)
            } // end Theme
        } // end fun

        /** Compose theme object. */
        val Theme = object : Themes.Companion.CustThemeType() {}
    } // end companion
} // end class
