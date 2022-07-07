// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Terminal emulator outputs demonstration. */
class TermOutsDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "TermOutsDemo", "term-outs-demo", false,
        false
    ) {
        /** Main window content. */
        @Composable
        override fun WindowScope.WinContent() {
            Elems.AppFrame {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    SettingsButtons()
                    Spacer(Modifier.size(8.dp))
                    TermOuts.TermOuts()
                } // end Row
            } // end Elems
        } // end fun
    } // end companion
} // end class
