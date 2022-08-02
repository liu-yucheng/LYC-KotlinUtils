// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.envs

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import lyc.ktutils.libs.composeutils.FlatLafFuncs
import lyc.ktutils.libs.composeutils.SysInfo

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

                val inStream: InputStream

                try {
                    inStream = Utils.findResStream(inLoc)
                } catch (exc: NullPointerException) {
                    throw NullPointerException(
                        """
                            exes.app.Funcs.copyFiles::inStream:
                                Utils.findResStream(inLoc) cannot be null
                                inLoc: $inLoc
                        """.trimIndent()
                    ) // end throw
                } // end try

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
        private fun ensureAppData(overwrite: Boolean) {
            val inPath = Defaults.defaultAppDataResPath
            val outPath = Defaults.appDataPath
            val outDir = File(outPath)

            if (!outDir.exists()) {
                outDir.mkdirs()
            } // end if

            val fileNames = arrayListOf(
                Defaults.configFieldsDemoName,
                Defaults.logName
            ) // end val

            copyFiles(inPath, outPath, fileNames, overwrite)
        } // end fun

        /** Ensures sample model root files.
         * @param overwrite: whether to overwrite the sample model
         */
        private fun ensureSampleModelRootFiles(overwrite: Boolean) {
            val inPath = Defaults.sampleModelResPath
            val outPath = Defaults.sampleModelPath
            val outDir = File(outPath)

            if (!outDir.exists()) {
                outDir.mkdirs()
            } // end if

            val fileNames = arrayListOf(
                Defaults.discConfigName,
                "discriminator_struct.py",
                Defaults.formatConfigName,
                Defaults.generatorConfigName,
                Defaults.generatorPreviewName,
                "generator_struct.py",
                "README.md"
            ) // end val

            copyFiles(inPath, outPath, fileNames, overwrite)
        } // end fun

        /** Ensures sample model - model saves files.
         * @param overwrite: whether to overwrite the sample model
         */
        private fun ensureSampleModelSavesFiles(overwrite: Boolean) {
            val inPath = Utils.joinResPaths(Defaults.sampleModelResPath, Defaults.modelSavesName)
            val outPath = Utils.joinPaths(Defaults.sampleModelPath, Defaults.modelSavesName)
            val outDir = File(outPath)

            if (!outDir.exists()) {
                outDir.mkdirs()
            } // end if

            val fileNames = arrayListOf(
                "discriminator_state.onnx",
                "discriminator_state_script.pt",
                "generator_state.onnx",
                Defaults.generatorStateScriptName
            ) // end val

            copyFiles(inPath, outPath, fileNames, overwrite)
        } // end fun

        /** Clears a model.
         * @param modelPath: model path
         */
        fun clearModel(modelPath: String) {
            val genResultPath = Utils.joinPaths(modelPath, Defaults.genResultsName)
            val genResultFile = File(genResultPath)

            if (genResultFile.exists()) {
                genResultFile.deleteRecursively()
            } // end if
        } // end fun

        /** Clears sample model. */
        private fun clearSampleModel() {
            clearModel(Defaults.sampleModelPath)
        } // end fun

        /** Ensures sample model.
         * @param overwrite: whether to overwrite the sample model
         */
        private fun ensureSampleModel(overwrite: Boolean) {
            ensureSampleModelRootFiles(overwrite)
            ensureSampleModelSavesFiles(overwrite)
            val clear = overwrite

            if (clear) {
                clearSampleModel()
            } // end if
        } // end fun

        /** Ensures user data.
         *
         * User data includes: [app data, sample model]
         *
         * @param overwrite: whether to overwrite the data
         */
        fun ensureUserData(overwrite: Boolean = false) {
            ensureAppData(overwrite)
            ensureSampleModel(overwrite)
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
