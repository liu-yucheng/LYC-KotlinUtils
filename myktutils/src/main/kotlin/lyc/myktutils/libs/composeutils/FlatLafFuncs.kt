package lyc.myktutils.libs.composeutils

import androidx.compose.runtime.Composable
import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.FlatLightLaf
import javax.swing.JFrame

/** FlatLaf functions. */
class FlatLafFuncs {
    companion object {
        /** Sets up FlatLaf theme.
         * @param darkEnabled: whether dark theme is enabled
         */
        @Composable
        fun SetupFlatLafTheme(darkEnabled: Boolean = false) {
            FlatLaf.registerCustomDefaultsSource("lyc.myktutils.libs.composeutils")

            if (darkEnabled) {
                FlatDarkLaf.setup()
            } else {
                FlatLightLaf.setup()
            } // end if

            JFrame.setDefaultLookAndFeelDecorated(true)
        } // end fun
    } // end companion
} // end class
