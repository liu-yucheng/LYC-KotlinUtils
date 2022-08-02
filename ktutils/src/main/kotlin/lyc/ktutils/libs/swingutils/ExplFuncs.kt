// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.swingutils

import java.awt.Desktop
import java.awt.Window
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

/** Desktop file explorer functions. */
class ExplFuncs private constructor() {
    companion object {
        /** Opens a folder in the desktop file explorer.
         * @param path: a path
         * @return result: whether the operation is successful
         */
        fun openInExpl(path: String): Boolean {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result: Boolean

            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                val file = File(path)
                desktop.open(file)
                result = true
            } else {
                result = false
            } // end if

            return result
        } // end fun

        /** Selects a path or location by mode in the desktop file explorer.
         * @param path: an initial path
         * @param mode: a file selection mode
         * @return result: the selection result
         */
        private fun selectByModeInExpl(path: String, mode: Int): String {
            val result: String

            if (Desktop.isDesktopSupported()) {
                val fileChooser = JFileChooser(path)
                fileChooser.fileSelectionMode = mode
                val wins = Window.getWindows()

                if (wins.isEmpty()) {
                    result = path
                } else {
                    val rootComp = SwingUtilities.getRoot(wins[wins.size - 1])
                    val selectResult = fileChooser.showDialog(rootComp, "Select")

                    if (selectResult == JFileChooser.APPROVE_OPTION) {
                        result = fileChooser.selectedFile.absolutePath
                    } else {
                        result = path
                    } // end if
                } // end if
            } else {
                result = path
            } // end if

            return result
        } // end fun

        /** Selects a path in the desktop file explorer.
         * @param path: an initial path
         * @return result: the selected folder path
         */
        fun selectPathInExpl(path: String): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result = selectByModeInExpl(path, JFileChooser.DIRECTORIES_ONLY)
            return result
        } // end fun

        /** Selects a location in the desktop file explorer.
         * @param loc: an initial location
         * @return result: the selected file location
         */
        fun selectLocInExpl(loc: String): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result = selectByModeInExpl(loc, JFileChooser.FILES_ONLY)
            return result
        } // end fun

        /** Selects a path or location in the desktop file explorer.
         * @param path: an initial path
         * @return result: the selected folder path or file location
         */
        fun selectInExpl(path: String): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result = selectByModeInExpl(path, JFileChooser.FILES_AND_DIRECTORIES)
            return result
        } // end fun
    } // end companion
} // end class
