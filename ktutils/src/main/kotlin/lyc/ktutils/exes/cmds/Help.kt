// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import kotlin.system.exitProcess
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.libs.demoutils.DemoInterface

/** Help. */
class Help private constructor() {
    companion object : DemoInterface {
        /** Arguments. */
        var args = arrayListOf<String>()

        override fun run() {
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
                Help:               java -jar $jarName help
                TermOutsDemo:       java -jar $jarName term-outs-demo
                TitleBarsDemo:      java -jar $jarName title-bars-demo
                CircPagesDemo:      java -jar $jarName circ-pages-demo
                ConfigFieldsDemo:   java -jar $jarName config-fields-demo
                ConfigViewsDemo:    java -jar $jarName config-views-demo
                DJLGenProcsDemo:    java -jar $jarName djl-gen-procs-demo
                -- End Commands
                - End $packName help info
            """.trimIndent() // end val

            println(info)
        } // end fun
    } // end companion
} // end class
