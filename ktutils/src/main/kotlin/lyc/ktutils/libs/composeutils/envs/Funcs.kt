// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.envs

import java.awt.Desktop
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import lyc.ktutils.libs.composeutils.FlatLafFuncs

/** Functions. */
class Funcs private constructor() {
    companion object {
        /** Copies [inStream] to [outStream] and closes both streams after copying. */
        private fun copyStreamAndClose(inStream: InputStream, outStream: OutputStream) {
            inStream.copyTo(outStream)
            inStream.close()
            outStream.close()
        } // end fun

        /** Copies the files [fileNames] from [inPath] to [outPath].
         * Does force overwriting or not as [overwrite] specified. */
        private fun copyFiles(inPath: String, outPath: String, fileNames: ArrayList<String>, overwrite: Boolean) {
            for (name in fileNames) {
                val inLoc = Utils.joinResPaths(inPath, name)
                val outLoc = Utils.joinPaths(outPath, name)
                val inStream = Utils.findResStream(inLoc)
                val outFile = File(outLoc)
                val outStream: OutputStream

                if (overwrite) {
                    outStream = outFile.outputStream() // NOTE: This changes outFile.exists()
                    copyStreamAndClose(inStream, outStream)
                } else {
                    if (!outFile.exists()) {
                        outStream = outFile.outputStream() // NOTE: This changes outFile.exists()
                        copyStreamAndClose(inStream, outStream)
                    } // end if
                } // end if
            } // end for
        } // end fun

        /** Ensures app data.
         * @param overwrite: whether to force overwrite the app data
         */
        fun ensureAppData(overwrite: Boolean = false) {
            val inPath = Defaults.defaultAppDataResPath
            val outPath = Defaults.appDataPath
            val outDir = File(outPath)

            if (!outDir.exists()) {
                outDir.mkdirs()
            }

            val fileNames = arrayListOf(
                Defaults.logName,
                Defaults.configFieldsDemoName
            ) // end val

            copyFiles(inPath, outPath, fileNames, overwrite)
        } // end fun

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

        /** Opens a folder in the desktop file explorer.
         * @param path: a path
         * @return result: whether the operation is successful
         */
        fun openInExpl(path: String): Boolean {
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
    } // end companion
} // end class
