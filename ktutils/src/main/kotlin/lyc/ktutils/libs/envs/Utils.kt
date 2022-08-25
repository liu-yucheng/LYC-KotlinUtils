// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.envs

import com.google.gson.JsonElement
import java.io.InputStream
import java.time.Duration
import java.time.LocalDateTime
import lyc.ktutils.libs.gputils.JSONIO
import lyc.ktutils.libs.gputils.Logs
import lyc.ktutils.libs.gputils.PackInfo
import lyc.ktutils.libs.gputils.Paths
import lyc.ktutils.libs.gputils.Reses
import lyc.ktutils.libs.gputils.TextIO
import lyc.ktutils.libs.gputils.TimeFormats
import lyc.ktutils.libs.swingutils.ExplFuncs

/** Utilities. */
class Utils private constructor() {
    companion object {
        /**
         * Joins path [path1] and [paths2] with the system default path name separator.
         * @param path1: a path
         * @param paths2: one or more paths
         */
        fun joinPaths(path1: String, vararg paths2: String): String = Paths.joinPaths(path1, *paths2)

        /**
         * Joins resource paths [path1] and [paths2] with the resource path name separator, "/".
         * @param path1: a resource path
         * @param paths2: one or more resource paths
         */
        fun joinResPaths(path1: String, vararg paths2: String): String = Reses.joinResPaths(path1, *paths2)

        /**
         * Finds the stream of a resource file at [fromLoc].
         * NOTE: Please remember to manually close the stream after finishing reading it.
         * @param fromLoc: a resource location
         * @return result: the resource stream
         */
        fun findResStream(fromLoc: String): InputStream = Reses.findResStream(fromLoc)

        /**
         * Loads the text contents of the file at [fromLoc].
         * @param fromLoc: a location
         * @return result: the text contents of the file
         */
        fun loadText(fromLoc: String): String = TextIO.loadText(fromLoc)

        /**
         * Saves the file contents [fromString] to file at [toLoc].
         * @param fromString: a string of file contents
         * @param toLoc: a location
         */
        fun saveText(fromString: String, toLoc: String) = TextIO.saveText(fromString, toLoc)

        /** A Gson object used for JSON related operations. */
        val gson = JSONIO.gson

        /**
         * Loads the JSON contents of the file at [fromLoc].
         * @param fromLoc: a location
         * @return result: the JSON contents of the file
         */
        fun loadJson(fromLoc: String): JsonElement = JSONIO.loadJson(fromLoc)

        /**
         * Saves the file contents [fromJsonElement] to file at [toLoc].
         * @param: fromJsonElement: a JsonElement of the file contents
         * @param: toLoc: a location
         */
        fun saveJson(fromJsonElement: JsonElement, toLoc: String) = JSONIO.saveJson(fromJsonElement, toLoc)

        /** Logs the string [str] to all logs in [toLogs].
         * @param toLogs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         * @param str: a string
         * @param err: Whether to log an error.
         *  When [err] is true, all the `System.out` in [toLogs] will be turned into `System.err`.
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun logstr(toLogs: ArrayList<Any>, str: String = "", err: Boolean = false) = Logs.logstr(toLogs, str, err)

        /** Logs the line [line] to all logs in [toLogs].
         * @param toLogs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         * @param line: a line
         * @param err: Whether to log an error.
         *  When [err] is true, all the `System.out` in [toLogs] will be turned into `System.err`.
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun logln(toLogs: ArrayList<Any>, line: String = "", err: Boolean = false) = Logs.logln(toLogs, line, err)

        /** Flushes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun flushLogs(logs: ArrayList<Any>) = Logs.flushLogs(logs)

        /** Closes the logs [logs].
         * @param logs: A list of logs.
         *  Supported element types: MutableList<String>, PrintWriter
         */
        @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
        fun closeLogs(logs: ArrayList<Any>) = Logs.closeLogs(logs)

        /** Finds the package version.
         * @param versPropsResPath: versions.properties resource path
         */
        fun findPackVer(versPropsResPath: String = "versions.properties"): String =
            PackInfo.findPackVer(versPropsResPath)

        /** Opens a folder in the desktop file explorer.
         * @param path: a path
         * @return result: whether the operation is successful
         */
        fun openInExpl(path: String): Boolean = ExplFuncs.openInExpl(path)

        /** Selects a path in the desktop file explorer.
         * @param path: an initial path
         * @return result: the selected folder path
         */
        fun selectPathInExpl(path: String): String = ExplFuncs.selectPathInExpl(path)

        /** Selects a location in the desktop file explorer.
         * @param loc: an initial location
         * @return result: the selected file location
         */
        fun selectLocInExpl(loc: String): String = ExplFuncs.selectLocInExpl(loc)

        /** Selects a path or location in the desktop file explorer.
         * @param path: an initial path
         * @return result: the selected folder path or file location
         */
        fun selectInExpl(path: String): String = ExplFuncs.selectInExpl(path)

        /** Local date time to timestamp string.
         *
         * The timestamp format is like: 20220102-030405-678901.
         *
         * @param localDateTime: a local date time
         * @return result: the result*/
        fun timestampStringOf(localDateTime: LocalDateTime): String = TimeFormats.timestampStringOf(localDateTime)

        /** Duration to duration string.
         *
         * The duration format is like: 2 days, 3:4:5.678 (days, hours: minutes: seconds. milliseconds).
         *
         * @param duration: a duration
         * @return result: the result
         */
        fun durationStringOf(duration: Duration): String = TimeFormats.durationStringOf(duration)
    } // end companion
} // end class
