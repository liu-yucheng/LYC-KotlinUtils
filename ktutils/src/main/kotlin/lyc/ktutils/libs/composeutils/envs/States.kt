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
import lyc.ktutils.libs.envs.Defaults
import lyc.ktutils.libs.envs.Utils

/** States. */
class States private constructor() {
    companion object {
        /** Main window state. */
        val mainWinState = WindowState(position = WindowPosition(Alignment.Center), size = DpSize(1288.dp, 724.dp))

        /** Log location. */
        val logLoc = Utils.joinPaths(Defaults.appDataPath, Defaults.logName)

        /** Log writer.
         * NOTE: Please remember to manually close the writer after finishing writing to it.
         */
        val logWriter: PrintWriter

        init {
            // Initialize logWriter
            val file = File(logLoc)
            val appendStream = FileOutputStream(file, true)
            logWriter = PrintWriter(appendStream)
        } // end init

        /** Terminal outputs. */
        val termOuts = mutableStateListOf<String>()

        /** All logs. */
        val allLogs = arrayListOf(termOuts, logWriter)

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

        /** Terminal extended. */
        val termExtended = mutableStateOf(true)

        /** Whether system dark themes are enabled. */
        val sysDarkEnabled = mutableStateOf(true)

        /** Whether to enable dark themes. */
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
        val Theme = object : Themes.CustThemeType() {}

        /** Copyright text. */
        val crText = """
            LYC-KotlinUtils, LYC's personal Kotlin utility collection library.
            Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
        """.trimIndent() // end val

        /** License text. */
        val licText: String

        init {
            // Initialize licText
            val loc = Utils.joinResPaths(Defaults.licsResPath, Defaults.licName)
            val stream = Utils.findResStream(loc)
            val reader = stream.bufferedReader()
            licText = reader.readText()
            stream.close()
        } // end init

        /** Open-source licenses text. */
        val openLicsText: String

        init {
            // Initialize openLicsText
            val loc = Utils.joinResPaths(Defaults.licsResPath, Defaults.openLicsName)
            val stream = Utils.findResStream(loc)
            val reader = stream.bufferedReader()
            openLicsText = reader.readText()
        } // end init
    } // end companion
} // end class
