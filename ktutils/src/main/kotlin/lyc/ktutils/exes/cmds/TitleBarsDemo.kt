// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.TitleBars.Companion.AppFrame
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Title bars demonstration. */
class TitleBarsDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "TitleBarDemo", "title-bar-demo", true,
        true
    ) {
        /** Main window content. */
        @Composable
        override fun WindowScope.WinContent() {
            AppFrame({ exitDemo(0) }, demoName) {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    SettingsButtons()
                    TermOuts.TermOuts()
                } // end Row
            } // end AppFrame
        } // end fun
    } // end companion
} // end class
