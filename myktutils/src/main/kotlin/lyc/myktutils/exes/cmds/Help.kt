// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.exes.cmds

import kotlin.system.exitProcess

/** Help. */
class Help private constructor() {
    companion object {
        /** Arguments. */
        var args = arrayListOf<String>()

        /** Runs the command. */
        fun run() {
            if (args.size > 0) {
                System.err.println("Help gets invalid arguments")
                System.err.println("Usage: java -jar lyc-myktutils.jar help")
                System.err.println("Help: java -jar lyc-myktutils.jar help")
                exitProcess(1)
            } // end if

            val info = """
                - lyc-myktutils.jar help info
                Usage:  java -jar lyc-myktutils.jar <command> ...
                -- Commands
                Help:           java -jar lyc-myktutils.jar help
                TermOutsDemo:   java -jar lyc-myktutils.jar term-outs-demo
                TitleBarsDemo:  java -jar lyc-myktutils.jar title-bars-demo
                -- End Commands
                - End lyc-myktutils.jar help info
            """.trimIndent()

            println(info)
        } // end fun
    } // end companion
} // end class
