// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.ConfigViews
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Configuration views demonstration. */
class ConfigViewsDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "ConfigViewsDemo", "config-views-demo", false,
        false
    ) {
        /** Composes the views content.
         * @param modifier: a modifier
         */
        @Composable
        private fun Views(modifier: Modifier = Modifier) {
            Box(modifier) {
                Column(
                    Modifier.fillMaxSize().horizontalScroll(ConfigViews.viewsHoriScroll)
                        .verticalScroll(ConfigViews.viewsVertScroll)
                    // end Modifier
                ) {
                    Text("Sample exportation configs:", color = MaterialTheme.colors.onBackground)
                    Spacer(Modifier.size(8.dp))

                    Row {
                        Spacer(Modifier.size(16.dp)) // Indentation level 1

                        Column {
                            Row(Modifier, Arrangement.Start, Alignment.CenterVertically) {
                                Text("Generator image resolution:", color = MaterialTheme.colors.onBackground)
                                Spacer(Modifier.size(8.dp))
                                ConfigViews.genImageResText.Content(Modifier)
                            } // end Row

                            Spacer(Modifier.size(8.dp))

                            Row(Modifier, Arrangement.Start, Alignment.CenterVertically) {
                                Text("Generator channel count:", color = MaterialTheme.colors.onBackground)
                                Spacer(Modifier.size(8.dp))
                                ConfigViews.genImageChannelCountText.Content(Modifier)
                            } // end Row

                            Spacer(Modifier.size(8.dp))

                            Column(Modifier, Arrangement.Top, Alignment.Start) {
                                Text("Generator preview:", color = MaterialTheme.colors.onBackground)
                                Spacer(Modifier.size(8.dp))

                                Row {
                                    Spacer(Modifier.size(16.dp)) // Indentation level 2
                                    ConfigViews.genPreviewImage.Content(Modifier.size(256.dp))
                                } // end Row
                            } // end Column
                        } // end Column
                    } // end Row
                } // end Column

                HorizontalScrollbar(
                    rememberScrollbarAdapter(ConfigViews.viewsHoriScroll),
                    Modifier.fillMaxWidth().height(16.dp).align(Alignment.BottomCenter)
                ) // end HorizontalScrollbar

                VerticalScrollbar(
                    rememberScrollbarAdapter(ConfigViews.viewsVertScroll),
                    Modifier.width(16.dp).fillMaxHeight().align(Alignment.CenterEnd)
                ) // end VerticalScrollbar
            } // end Box
        } // end fun

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

                        Column(Modifier.weight(1f)) {
                            Views(Modifier.weight(1f))
                            Spacer(Modifier.size(8.dp))

                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                                ConfigViews.LoadConfigsButton()
                            } // end Row
                        } // end Column

                        Spacer(Modifier.size(16.dp))
                        Column(termOutsColMod) { TermOuts.TermOuts() }
                    } // end Row
                } // end Column
            } // end Elems
        } // end fun
    } // end companion
} // end class
