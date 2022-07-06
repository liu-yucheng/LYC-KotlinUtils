// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.gputils

import java.io.PrintStream
import java.io.PrintWriter

/** Logs. */
class Logs {
    companion object {
        /** Uses [String.lines] to find the lines in the string [str]. */
        private fun findLines(str: String): ArrayList<String> {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result = ArrayList<String>(str.lines())
            val lastIdx = result.size - 1
            val lastElem = result[lastIdx]

            if (lastIdx > 0 && lastElem == "") {
                result.removeAt(lastIdx)
            }

            return result
        } // end fun

        /** Logs the string lines [strLines] to the mutable list [log]. */
        private fun logStrLinesToMutableList(log: MutableList<String>, strLines: ArrayList<String>) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for ((index, line) in strLines.withIndex()) {
                if (index <= 0) {
                    if (log.isEmpty()) {
                        log.add(line)
                    } else {
                        log[log.size - 1] += line
                    } // end if
                } else {
                    log.add(line)
                } // end if
            } // end for
        } // end fun

        /** Logs the string [str] to all logs in [toLogs].
         * @param toLogs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter, PrintStream
         * @param str: a string
         * @param err: Whether to log an error.
         *  When [err] is true, all the `System.out` in [toLogs] will be turned into `System.err`.
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun logstr(toLogs: ArrayList<Any>, str: String = "", err: Boolean = false) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in toLogs) {
                if (log is MutableList<*>) {
                    val log = log as MutableList<String>
                    val strLines = findLines(str)
                    logStrLinesToMutableList(log, strLines)
                } else if (log is PrintWriter) {
                    log.print(str)
                } else if (log is PrintStream) {
                    val log = if (err and (log === System.out)) {
                        System.err
                    } else {
                        System.out
                    } // end if

                    log.print(str)
                } // end if
            } // end for
        } // end fun

        /** logs the ln lines [lnLines] to the mutable list [log]. */
        private fun logLnLinesToMutableList(log: MutableList<String>, lnLines: ArrayList<String>) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (line in lnLines) {
                log.add(line)
            } // end for
        } // end fun

        /** Logs the line [line] to all logs in [toLogs].
         * @param toLogs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter, PrintStream
         * @param line: a line
         * @param err: Whether to log an error.
         *  When [err] is true, all the `System.out` in [toLogs] will be turned into `System.err`.
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun logln(toLogs: ArrayList<Any>, line: String = "", err: Boolean = false) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in toLogs) {
                if (log is MutableList<*>) {
                    val log = log as MutableList<String>
                    val lnLines = findLines(line)
                    logLnLinesToMutableList(log, lnLines)
                } else if (log is PrintWriter) {
                    log.println(line)
                } else if (log is PrintStream) {
                    val log = if (err and (log === System.out)) {
                        System.err
                    } else {
                        System.out
                    } // end if

                    log.println(line)
                } // end if
            } // end for
        } // end fun

        /** Flushes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun flushLogs(logs: ArrayList<Any>) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in logs) {
                if (log is MutableList<*>) {
                    // val log = log as MutableList<String>
                    // Do nothing
                } else if (log is PrintWriter) {
                    log.flush()
                } // end if
            } // end for
        } // end fun

        /** Closes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun closeLogs(logs: ArrayList<Any>) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in logs) {
                if (log is MutableList<*>) {
                    val log = log as MutableList<String>
                    log.clear()
                } else if (log is PrintWriter) {
                    log.close()
                } // end if
            } // end for
        } // end fun
    } // end companion
} // end class
