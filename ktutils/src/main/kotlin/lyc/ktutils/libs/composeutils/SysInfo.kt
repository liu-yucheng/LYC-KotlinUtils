// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.awaitApplication
import kotlinx.coroutines.runBlocking

/** System info. */
class SysInfo {
    companion object {
        /** Find whether the system has dark themes enabled.
         *
         * NOTE: To use this function, invoke once before the main application starts.
         * WARNING: This is a blocking function and might freeze or brick the main application.
         *
         * @return result: whether dark themes is enabled
         */
        fun findSysDarkEnabled(): Boolean {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            var result = false
            runBlocking { awaitApplication { result = isSystemInDarkTheme() } }
            return result
        } // end fun
    } // end companion
} // end class
