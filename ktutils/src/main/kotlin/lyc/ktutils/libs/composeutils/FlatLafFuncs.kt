// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.FlatLightLaf
import java.awt.Window
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

/** FlatLaf functions. */
class FlatLafFuncs private constructor() {
    companion object {
        /** Sets up FlatLaf theme.
         * @param darkEnabled: whether dark themes are enabled
         */
        fun setupFlatLafTheme(darkEnabled: Boolean = false) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            // TODO: Please set your custom default source here
            FlatLaf.registerCustomDefaultsSource("lyc.ktutils.libs.composeutils")

            if (darkEnabled) {
                FlatDarkLaf.setup()
            } else {
                FlatLightLaf.setup()
            } // end if

            JFrame.setDefaultLookAndFeelDecorated(true)
        } // end fun

        /** Updates FlatLaf theme and refreshes all Swing windows.
         * @param darkEnabled: whether dark themes are enabled
         */
        fun updateFlatLafTheme(darkEnabled: Boolean = false) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            // TODO: Please set your custom default source here
            FlatLaf.registerCustomDefaultsSource("lyc.ktutils.libs.composeutils")

            val laf = if (darkEnabled) {
                FlatDarkLaf()
            } else {
                FlatLightLaf()
            } // end val

            UIManager.setLookAndFeel(laf)
            JFrame.setDefaultLookAndFeelDecorated(true)

            for (win in Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(win)
            } // end for
        } // end fun
    } // end companion
} // end class
