// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import kotlin.system.exitProcess
import lyc.ktutils.exes.jarName
import lyc.ktutils.libs.demoutils.DemoInterface
import lyc.ktutils.libs.djlutils.classes.GenProc
import lyc.ktutils.libs.envs.Defaults
import lyc.ktutils.libs.envs.Funcs
import lyc.ktutils.libs.envs.Utils

/** DJL generation processes demonstration. */
class DJLGenProcsDemo private constructor() {
    companion object : DemoInterface {
        /** Arguments. */
        var args = arrayListOf<String>()

        override fun run() {
            if (args.size > 0) {
                val info = """
                    DJLGenProcsDemo gets invalid arguments
                    Usage: java -jar $jarName djl-gen-procs-demo
                    Help: java -jar $jarName help
                """.trimIndent() // end info

                System.err.println(info)
                exitProcess(1)
            } // end if

            Funcs.ensureUserData()
            val logLoc = Utils.joinPaths(Defaults.appDataPath, Defaults.logName)
            val logFile = File(logLoc)
            val logAppendStream = FileOutputStream(logFile, true)
            val logWriter = PrintWriter(logAppendStream)

            val logstr = { string: String ->
                print(string)
                logWriter.print(string)
            } // end val

            val logln = { line: String ->
                println(line)
                logWriter.println(line)
            } // end val

            val genProc = GenProc(Defaults.appDataPath, Defaults.sampleExportPath, logstr, logln)
            logln("DJLGenProcsDemo generation process")
            logln("Generation process completed: ${genProc.completed}")
            genProc.prep()
            genProc.start()
            val exeTime = genProc.exeTime
            val exeTimeString = Utils.durationStringOf(exeTime)
            logln("Execution time: $exeTimeString")
            logln("Generation process completed: ${genProc.completed}")
            logln("End DJLGenProcsDemo generation process")

            logWriter.close()
        } // end fun
    } // end companion
} // end class
