// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.envs

import java.io.File
import java.io.InputStream
import java.io.OutputStream

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
                Defaults.djlGenProcsDemoName,
                Defaults.logName
            ) // end val

            copyFiles(inPath, outPath, fileNames, overwrite)
        } // end fun

        /** Ensures sample exportation root files.
         * @param overwrite: whether to overwrite the sample exportation
         */
        private fun ensureSampleExportRootFiles(overwrite: Boolean) {
            val inPath = Defaults.sampleExportResPath
            val outPath = Defaults.sampleExportPath
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

        /** Ensures sample exportation - model saves files.
         * @param overwrite: whether to overwrite the sample exportation
         */
        private fun ensureSampleExportSavesFiles(overwrite: Boolean) {
            val inPath = Utils.joinResPaths(Defaults.sampleExportResPath, Defaults.modelSavesName)
            val outPath = Utils.joinPaths(Defaults.sampleExportPath, Defaults.modelSavesName)
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

        /** Clears an exportation.
         * @param exportPath: exportation path
         */
        fun clearExport(exportPath: String) {
            val genResultPath = Utils.joinPaths(exportPath, Defaults.genResultsName)
            val genResultFile = File(genResultPath)

            if (genResultFile.exists()) {
                genResultFile.deleteRecursively()
            } // end if
        } // end fun

        /** Clears sample exportation. */
        private fun clearSampleExport() {
            clearExport(Defaults.sampleExportPath)
        } // end fun

        /** Ensures sample exportation.
         * @param overwrite: whether to overwrite the sample exportation
         */
        private fun ensureSampleExport(overwrite: Boolean) {
            ensureSampleExportRootFiles(overwrite)
            ensureSampleExportSavesFiles(overwrite)
            val clear = overwrite

            if (clear) {
                clearSampleExport()
            } // end if
        } // end fun

        /** Ensures user data.
         *
         * User data includes: [app data, sample exportation]
         *
         * @param overwrite: whether to overwrite the data
         */
        fun ensureUserData(overwrite: Boolean = false) {
            ensureAppData(overwrite)
            ensureSampleExport(overwrite)
        } // end fun
    } // end companion
} // end class
