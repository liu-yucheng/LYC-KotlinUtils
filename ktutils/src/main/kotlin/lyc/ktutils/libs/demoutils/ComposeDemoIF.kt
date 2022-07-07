// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.demoutils

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope

/** Compose demonstration interface. */
interface ComposeDemoIF {
    /** Exits the demonstration.
     * @param statusCode: a status code
     */
    fun exitDemo(statusCode: Int = 0)

    /** Main window content. */
    @Composable
    fun WindowScope.WinContent()

    /** Runs the demonstration. */
    fun run()
} // end interface
