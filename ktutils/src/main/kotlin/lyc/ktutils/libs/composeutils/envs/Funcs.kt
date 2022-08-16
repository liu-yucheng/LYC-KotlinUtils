// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.envs

import lyc.ktutils.libs.composeutils.FlatLafFuncs
import lyc.ktutils.libs.composeutils.SysInfo
import lyc.ktutils.libs.envs.Funcs
import lyc.ktutils.libs.envs.Utils

typealias LibEnvFuncs = Funcs

/** Functions. */
class Funcs private constructor() {
    companion object {
        /** Clears an exportation.
         * @param exportPath: exportation path
         */
        fun clearExport(exportPath: String) = LibEnvFuncs.clearExport(exportPath)

        /** Ensures user data.
         *
         * User data includes: [app data, sample exportation]
         *
         * @param overwrite: whether to overwrite the data
         */
        fun ensureUserData(overwrite: Boolean = false) = LibEnvFuncs.ensureUserData(overwrite)

        /** Logs the string [str].
         * @param str: a string
         * @param err: whether to log an error
         */
        fun logstr(str: String, err: Boolean = false) {
            Utils.logstr(States.allLogs, str, err)
        } // end fun

        /** Logs the line [line].
         * @param line: a line
         * @param err: whether to log an error
         */
        fun logln(line: String, err: Boolean = false) {
            Utils.logln(States.allLogs, line, err)
        } // end fun

        /** Flushes the logs. */
        fun flushLogs() {
            Utils.flushLogs(States.allLogs)
        } // end fun

        /** Closes the logs. */
        fun closeLogs() {
            Utils.closeLogs(States.allLogs)
        } // end fun

        /** Sets-up FlatLaf theme. */
        fun setupFlatLafTheme() {
            FlatLafFuncs.setupFlatLafTheme(States.darkEnabled.value)
        } // end fun

        /** Updates FlatLaf theme. */
        fun updateFlatLafTheme() {
            FlatLafFuncs.updateFlatLafTheme(States.darkEnabled.value)
        } // end fun

        /** Find whether the system has dark themes enabled.
         * @return result: whether dark themes is enabled
         */
        fun findSysDarkEnabled(): Boolean = SysInfo.findSysDarkEnabled()

        /** Logs copyrights. */
        fun logCrs() {
            val info = """
==== Copyright ====
${States.crText}
==== End Of Copyright ====
==== License ====
${States.licText}
==== End Of License ====
==== Open-source Licenses ====
${States.openLicsText}
==== End Of Open-source Licenses ====
            """.trim() // end val

            logln(info)
        } // end fun
    } // end companion
} // end class
