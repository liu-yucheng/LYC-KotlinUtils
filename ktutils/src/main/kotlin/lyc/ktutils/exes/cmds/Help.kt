// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import kotlin.system.exitProcess
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName

/** Help. */
class Help private constructor() {
    companion object {
        /** Arguments. */
        var args = arrayListOf<String>()

        /** Runs the command. */
        fun run() {
            if (args.size > 0) {
                val info = """
                    Help gets invalid arguments
                    Usage: java -jar $jarName help
                    Help: java -jar $jarName help
                """.trimIndent() // end val

                System.err.println(info)
                exitProcess(1)
            } // end if

            val info = """
                - $packName help info
                Usage:  java -jar $jarName <command> ...
                -- Commands
                Help:           java -jar $jarName help
                TermOutsDemo:   java -jar $jarName term-outs-demo
                TitleBarsDemo:  java -jar $jarName title-bars-demo
                -- End Commands
                - End $packName help info
            """.trimIndent() // end val

            println(info)
        } // end fun
    } // end companion
} // end class
