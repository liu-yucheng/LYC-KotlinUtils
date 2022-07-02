// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.exes

import lyc.myktutils.libs.gputils.findPackVer

/**
 * Main function.
 * @param args: an array of app invocation arguments
 */
fun main(args: Array<String>) {
    mainArgs = ArrayList(args.asList())
    println("liu-yucheng/MyKotlinUtils ${findPackVer()}")
    println("- mainArgs")

    for ((idx, arg) in mainArgs.withIndex()) {
        println("  [$idx]: $arg")
    }

    println("- End mainArgs")
} // end fun

/** Main arguments. */
var mainArgs = ArrayList<String>()
