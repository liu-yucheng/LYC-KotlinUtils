// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.Navs
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Circular pages of 4 demo. */
class CircPagesDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "CircPagesDemo", "circ-pages-demo", false,
        false
    ) {
        /** Main window content. */
        @Composable
        override fun WindowScope.WinContent() {
            Elems.AppFrame {
                Column(Modifier.fillMaxSize()) {
                    SettingsButtons()
                    Spacer(Modifier.size(8.dp))

                    Row(Modifier.fillMaxSize()) {
                        val termOutsColMod = if (States.termExtended.value) {
                            Modifier.weight(1f)
                        } else {
                            Modifier.wrapContentWidth().fillMaxHeight()
                        } // end val

                        Column(Modifier.weight(1f)) { Navs._4CircPages() }
                        Column(termOutsColMod) { TermOuts.TermOuts() }
                    } // end Row
                } // end Column
            } // end Elems
        } // end fun
    } // end companion
} // end class
