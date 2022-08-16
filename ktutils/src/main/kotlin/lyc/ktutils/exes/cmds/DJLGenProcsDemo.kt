// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import kotlin.system.exitProcess
import lyc.ktutils.exes.jarName
import lyc.ktutils.libs.demoutils.DemoInterface
import lyc.ktutils.libs.djlutils.classes.GenProc
import lyc.ktutils.libs.envs.Defaults
import lyc.ktutils.libs.envs.Funcs

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
            val logstr = { string: String -> print(string) }
            val logln = { line: String -> println(line) }
            val genProc = GenProc(Defaults.appDataPath, Defaults.sampleModelPath, logstr, logln)
            logln("DJLGenProcsDemo generation process")
            genProc.prep()
            genProc.start()
            logln("End DJLGenProcsDemo generation process")
        } // end fun
    } // end companion
} // end class
