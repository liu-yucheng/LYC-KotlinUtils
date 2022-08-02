// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.foundation.ScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lyc.ktutils.libs.composeutils.configfields.BoolField
import lyc.ktutils.libs.composeutils.configfields.ClampedFloatField
import lyc.ktutils.libs.composeutils.configfields.EvenIntGE0Field
import lyc.ktutils.libs.composeutils.configfields.JSONField
import lyc.ktutils.libs.composeutils.configfields.PathField
import lyc.ktutils.libs.composeutils.configfields.StringField
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

/** Configuration fields. */
class ConfigFields private constructor() {
    companion object {
        /** String field. */
        val stringField = StringField(States.configFieldsDemoRoot, keys = arrayOf("string"), "String")

        /** Boolean field. */
        val boolField = BoolField(States.configFieldsDemoRoot, keys = arrayOf("boolean"), "Boolean")

        /** Even integer greater than or equal to 0 field. */
        val evenIntGE0Field = EvenIntGE0Field(
            States.configFieldsDemoRoot, keys = arrayOf("even_integer_ge_0"),
            "Even integer greater than or equal to 0"
        ) // end val

        /** Float range 0 to 100 field. */
        val floatRange0To100Field = ClampedFloatField(
            States.configFieldsDemoRoot, keys = arrayOf("float_range_0_to_100"),
            "Clamped float range 0 to 100", "Float, range [0.0, 100.0]. Example: 25.0, 75.0",
            0.0, 100.0
        ) // end val

        /** Any path field. */
        val anyPathField = PathField(
            States.configFieldsDemoRoot, keys = arrayOf("any_path"), "Any path", Defaults.userDataPath
        ) // end val

        /** All fields. */
        val allFields = arrayListOf<JSONField<*>>(
            stringField, boolField, evenIntGE0Field, floatRange0To100Field, anyPathField
        ) // end val

        /** Fields vertical scroll state. */
        val fieldsVertScroll = ScrollState(0)

        /** Load configs button. */
        @Composable
        fun LoadConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                val loc = Utils.joinPaths(Defaults.appDataPath, Defaults.configFieldsDemoName)
                States.configFieldsDemoRoot = Utils.loadJson(loc)

                for (field in allFields) {
                    field.root = States.configFieldsDemoRoot
                } // end for

                Funcs.logln("Loaded the configs from ${Defaults.configFieldsDemoName} in app data")
            } // end val

            val warnColors = ButtonDefaults.buttonColors(States.Theme.extColors.warn, States.Theme.extColors.onWarn)
            Button(onClick, modifier, colors = warnColors) { Text("Load configs") }
        } // end val

        /** Save configs button. */
        @Composable
        fun SaveConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                val loc = Utils.joinPaths(Defaults.appDataPath, Defaults.configFieldsDemoName)

                for (field in allFields) {
                    field.prepSave()
                } // end for

                Utils.saveJson(States.configFieldsDemoRoot, loc)
                Funcs.logln("Saved the configs to ${Defaults.configFieldsDemoName} in app data")
            } // end val

            Button(onClick, modifier) { Text("Save configs") }
        } // end val
    } // end companion
} // end class
