package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import lyc.ktutils.libs.composeutils.NavPages
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
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
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    SettingsButtons()
                    Spacer(Modifier.size(8.dp))

                    Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { NavPages.CircPagesOf4() }
                        Column(Modifier.weight(1f)) { TermOuts.TermOuts() }
                    } // end Row
                } // end Column
            } // end Elems
        } // end fun
    } // end companion
} // end class
