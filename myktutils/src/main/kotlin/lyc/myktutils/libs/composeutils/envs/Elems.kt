// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.composeutils.envs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/** Elements. */
class Elems private constructor() {
    companion object {
        /** App frame.
         * @param content: some composable contents
         */
        @Composable
        fun AppFrame(content: @Composable () -> Unit) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            States.Theme {
                Box(
                    Modifier
                        .fillMaxSize().clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                        .background(MaterialTheme.colors.background).padding(16.dp, 0.dp, 16.dp, 16.dp),
                    // end Modifier

                    Alignment.TopStart
                ) { content() } // end Box
            } // end States
        } // end fun
    } // end companion
} // end class
