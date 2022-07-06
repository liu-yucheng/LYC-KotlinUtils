// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.exes

import kotlin.system.exitProcess
import lyc.myktutils.exes.cmds.Help
import lyc.myktutils.exes.cmds.TermOutsDemo
import lyc.myktutils.exes.cmds.TitleBarsDemo
import lyc.myktutils.libs.gputils.PackInfo

/**
 * Main function.
 * @param args: an array of app invocation arguments
 */
fun main(args: Array<String>) {
    mainArgs = ArrayList(args.asList())
    println("liu-yucheng/MyKotlinUtils ${PackInfo.findPackVer()}")
    System.err.println("Help: java -jar lyc-myktutils.jar help")
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
                System.err.println("liu-yucheng/MyKotlinUtils gets invalid arguments")
                System.err.println("Usage: java -jar lyc-myktutils.jar <command> ...")
                System.err.println("Help: java -jar lyc-myktutils.jar help")
                exitProcess(1)
            } // end else
        } // end when
    } // end if
} // end fun

/** Main arguments. */
var mainArgs = ArrayList<String>()
