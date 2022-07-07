// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes

import kotlin.system.exitProcess
import lyc.ktutils.exes.cmds.Help
import lyc.ktutils.exes.cmds.TermOutsDemo
import lyc.ktutils.exes.cmds.TitleBarsDemo
import lyc.ktutils.libs.gputils.PackInfo

/**
 * Main function.
 * @param args: an array of app invocation arguments
 */
fun main(args: Array<String>) {
    mainArgs = ArrayList(args.asList())
    println("$packName $packVer")
    System.err.println("Help: java -jar $jarName help")
    println("- mainArgs")

    for ((idx, arg) in mainArgs.withIndex()) {
        println("  [$idx]: $arg")
    }

    println("- End mainArgs")

    if (mainArgs.size > 0) {
        val arg1 = mainArgs[0]
        val argsRemain = ArrayList(mainArgs.slice(1 until mainArgs.size))

        when (arg1) {
            "help" -> {
                Help.args = argsRemain
                Help.run()
            } // end "help"

            "term-outs-demo" -> {
                TermOutsDemo.args = argsRemain
                TermOutsDemo.run()
            } // end "term-outs-demo"

            "title-bars-demo" -> {
                TitleBarsDemo.args = argsRemain
                TitleBarsDemo.run()
            } // end "title-bars-demo"

            else -> {
                System.err.println("$packName gets an unknown command: $arg1")
                System.err.println("Usage: java -jar $jarName <command> ...")
                System.err.println("Help: java -jar $jarName help")
                exitProcess(1)
            } // end else
        } // end when
    } // end if
} // end fun

/** Main arguments. */
var mainArgs = ArrayList<String>()

/** Package name. */
const val packName = "LYC-KotlinUtils"

/** Package version. */
val packVer = PackInfo.findPackVer()

/** JAR file name. */
val jarName = "ktutils-${packVer}.jar"
