// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.envs

/** Defaults. */
class Defaults private constructor() {
    companion object {
        // Paths

        /** User home. */
        val userHomePath = System.getProperty("user.home")

        /** Data path. */
        val dataPath = Utils.joinPaths(userHomePath, ".lyc_ktutils")

        /** App data path. */
        val appDataPath = Utils.joinPaths(dataPath, "app_data")

        // End; Resource paths

        /** Default app data resource path. */
        const val defaultAppDataResPath = "default_app_data"

        /** Icons resource path. */
        const val iconsResPath = "icons"

        /** Licenses resource path. */
        const val licsResPath = "licenses"

        // End; Names

        /** Log name. */
        const val logName = "log.txt"

        /** Open-source license name. */
        const val openLicsName = "open_source_licenses.txt"

        /** License name. */
        const val licName = "license.txt"

        /** Minimize window icon name. */
        const val minWinIconName = "window-minimize.svg"

        /** Maximize window icon name. */
        const val maxWinIconName = "window-maximize.svg"

        /** Close window icon name. */
        const val closeWinIconName = "window-close.svg"

        /** Restore window icon name. */
        const val restoreWinIconName = "window-restore.svg"

        /** Go to bottom icon name. */
        const val goToBottomIconName = "border-bottom-variant.svg"

        /** Clear outputs icon name. */
        const val clearOutsIconName = "backspace-outline.svg"

        /** Terminal outputs handle icon name. */
        const val termOutsHandleIconName = "console.svg"

        /** Configuration fields demonstration name. */
        const val configFieldsDemoName = "config_fields_demo.json"
    } // end companion
} // end class
