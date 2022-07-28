// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils

import java.io.PrintStream
import java.io.PrintWriter

/** Logs. */
class Logs {
    companion object {
        /** Uses [String.lines] to find the lines in the string [str]. */
        private fun findLines(str: String): ArrayList<String> {
            val result = ArrayList<String>(str.lines())
            val lastIdx = result.size - 1
            val lastElem = result[lastIdx]

            if ((lastIdx > 0) and (lastElem == "")) {
                result.removeAt(lastIdx)
            } // end if

            return result
        } // end fun

        /** Logs the string lines [strLines] to the mutable list [log]. */
        private fun logStrLinesToMutableList(log: MutableList<String>, strLines: ArrayList<String>) {
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

        /** Based on [err], optionally turns [log] from `System.out` to `System.err` and returns `result`. */
        private fun optSysOutToErr(log: PrintStream, err: Boolean): PrintStream {
            val result = if (err and (log === System.out)) {
                System.err
            } else {
                log
            } // end val

            return result
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
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in toLogs) {
                when (log) {
                    is MutableList<*> -> {
                        val log = log as MutableList<String>
                        val strLines = findLines(str)
                        logStrLinesToMutableList(log, strLines)
                    } // end is

                    is PrintWriter -> {
                        log.print(str)
                    } // end is

                    is PrintStream -> {
                        val log = optSysOutToErr(log, err)
                        log.print(str)
                    } // end is

                    else -> {
                        // Do nothing
                    } // end else
                } // end when
            } // end for
        } // end fun

        /** logs the ln lines [lnLines] to the mutable list [log]. */
        private fun logLnLinesToMutableList(log: MutableList<String>, lnLines: ArrayList<String>) {
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
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in toLogs) {
                when (log) {
                    is MutableList<*> -> {
                        val log = log as MutableList<String>
                        val lnLines = findLines(line)
                        logLnLinesToMutableList(log, lnLines)
                    } // end is

                    is PrintWriter -> {
                        log.println(line)
                    } // end is

                    is PrintStream -> {
                        val log = optSysOutToErr(log, err)
                        log.println(line)
                    } // end is

                    else -> {
                        // Do nothing
                    } // end else
                } // end when
            } // end for
        } // end fun

        /** Flushes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter, PrintStream
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun flushLogs(logs: ArrayList<Any>) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in logs) {
                when (log) {
                    is MutableList<*> -> {
                        // val log = log as MutableList<String>
                        // Do nothing
                    } // end is

                    is PrintWriter -> {
                        log.flush()
                    } // end is

                    is PrintStream -> {
                        log.flush()
                    } // end is

                    else -> {
                        // Do nothing
                    } // end else
                } // end when
            } // end for
        } // end fun

        /** Closes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter, PrintStream
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun closeLogs(logs: ArrayList<Any>) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            for (log in logs) {
                when (log) {
                    is MutableList<*> -> {
                        val log = log as MutableList<String>
                        log.clear()
                    } // end is

                    is PrintWriter -> {
                        log.close()
                    } // end is

                    is PrintStream -> {
                        log.close()
                    } // end is

                    else -> {
                        // Do nothing
                    } // end else
                } // end when
            } // end for
        } // end fun
    } // end companion
} // end class
