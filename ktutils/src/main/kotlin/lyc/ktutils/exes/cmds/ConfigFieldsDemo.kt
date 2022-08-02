// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.ConfigFields
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Configuration fields demonstration. */
class ConfigFieldsDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "ConfigFieldsDemo", "config-fields-demo", false,
        false
    ) {
        /** Fields content. */
        @Composable
        private fun FieldsContent(modifier: Modifier = Modifier) {
            Box(modifier) {
                Column(modifier.fillMaxSize().verticalScroll(ConfigFields.fieldsVertScroll)) {
                    ConfigFields.stringField.Content(Modifier.fillMaxWidth())
                    Spacer(Modifier.size(8.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                        ConfigFields.boolField.Content(Modifier.weight(1f))
                        Spacer(Modifier.size(8.dp))
                        Button({ ConfigFields.boolField.value = !ConfigFields.boolField.value }) { Text("Toggle") }
                    } // end Row

                    Spacer(Modifier.size(8.dp))
                    ConfigFields.evenIntGE0Field.Content(Modifier.fillMaxWidth())
                    Spacer(Modifier.size(8.dp))
                    ConfigFields.floatRange0To100Field.Content(Modifier.fillMaxWidth())
                    Spacer(Modifier.size(8.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                        ConfigFields.anyPathField.Content(Modifier.weight(1f))
                        Spacer(Modifier.size(8.dp))

                        Button(
                            {
                                ConfigFields.anyPathField.value =
                                    Utils.selectPathInExpl(ConfigFields.anyPathField.value)
                            } // end onClick
                        ) { Text("Select") } // end Button
                    } // end Row
                } // end Column

                VerticalScrollbar(
                    rememberScrollbarAdapter(ConfigFields.fieldsVertScroll),
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
                            FieldsContent(Modifier.weight(1f))
                            Spacer(Modifier.size(8.dp))

                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                                ConfigFields.LoadConfigsButton()
                                ConfigFields.SaveConfigsButton()
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
