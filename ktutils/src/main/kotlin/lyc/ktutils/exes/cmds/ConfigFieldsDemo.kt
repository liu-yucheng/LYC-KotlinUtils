// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
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
import lyc.ktutils.libs.composeutils.ConfigFields
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.envs.Elems
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.demoutils.ComposeDemo

/** Configuration fields demonstration. */
class ConfigFieldsDemo private constructor() {
    companion object : ComposeDemo(
        packName, packVer, jarName, "ConfigFieldsDemo", "config-fields-demo", false,
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

                        Column(Modifier.weight(1f)) {
                            Spacer(Modifier.weight(1f))
                            ConfigFields.stringField.Content(Modifier.fillMaxWidth())
                            Spacer(Modifier.weight(1f))

                            Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                                ConfigFields.boolField.Content(Modifier.weight(1f))
                                Spacer(Modifier.size(8.dp))

                                Button(
                                    { ConfigFields.boolField.valueAccess = !ConfigFields.boolField.valueAccess }
                                ) {
                                    Text("Toggle")
                                } // end Button
                            } // end Row

                            Spacer(Modifier.weight(1f))
                            ConfigFields.evenIntGE0Field.Content(Modifier.fillMaxWidth())
                            Spacer(Modifier.weight(1f))
                            ConfigFields.floatRange0To100Field.Content(Modifier.fillMaxWidth())
                            Spacer(Modifier.weight(1f))
                            ConfigFields.SaveConfigsButton(Modifier.align(Alignment.CenterHorizontally))
                            Spacer(Modifier.weight(1f))
                        } // end Column

                        Column(termOutsColMod) { TermOuts.TermOuts() }
                    } // end Row
                } // end Column
            } // end Elems
        } // end fun
    } // end companion
} // end class
