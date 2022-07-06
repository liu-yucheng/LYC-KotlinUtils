// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.gputils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.awaitApplication
import kotlinx.coroutines.runBlocking

/** System info. */
class SysInfo {
    companion object {
        /** Find whether the system has dark themes enabled.
         * @return result: whether dark themes is enabled
         */
        fun findSysDarkEnabled(): Boolean {
            var result = false
            runBlocking { awaitApplication { result = isSystemInDarkTheme() } }
            return result
        } // end fun
    } // end companion
} // end class
