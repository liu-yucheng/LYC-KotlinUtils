// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.foundation.ScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lyc.ktutils.libs.composeutils.configviews.JSONImage
import lyc.ktutils.libs.composeutils.configviews.JSONText
import lyc.ktutils.libs.composeutils.configviews.JSONView
import lyc.ktutils.libs.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.envs.Utils

/** Configuration views. */
class ConfigViews private constructor() {
    companion object {
        private val configLoc = Utils.joinPaths(Defaults.sampleModelPath, Defaults.generatorConfigName)

        /** Configuration JSON root. */
        private var configRoot = Utils.loadJson(configLoc)

        /** Generator image resolution text. */
        val genImageResText = JSONText(
            configRoot, keys = arrayOf("image_resolution"), "<Cannot read image resolution>"
        ) // end val

        /** Generator image channel count text. */
        val genImageChannelCountText = JSONText(
            configRoot, keys = arrayOf("image_channel_count"), "<Cannot read image channel count>"
        ) // end val

        /** Generator preview image. */
        val genPreviewImage = JSONImage(
            configRoot, keys = arrayOf("preview_name"), configLoc, "Generator preview"
        ) // end val

        /** All views. */
        val allViews = arrayListOf<JSONView<*>>(genImageResText, genImageChannelCountText, genPreviewImage)

        /** Load configs button.
         * @param modifier: a modifier
         * */
        @Composable
        fun LoadConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                configRoot = Utils.loadJson(configLoc)

                for (field in allViews) {
                    field.root = configRoot
                } // end for

                Funcs.logln(
                    "Loaded the configs from ${Defaults.generatorConfigName}" +
                        " in sample model at: ${Defaults.sampleModelPath}"
                ) // emd Funcs
            } // end val

            val warnColors = ButtonDefaults.buttonColors(States.Theme.extColors.warn, States.Theme.extColors.onWarn)
            Button(onClick, modifier, colors = warnColors) { Text("Load configs") }
        } // end fun

        /** Views horizontal scroll state. */
        val viewsHoriScroll = ScrollState(0)

        /** Views vertical scroll state. */
        val viewsVertScroll = ScrollState(0)
    } // end companion
} // end class
