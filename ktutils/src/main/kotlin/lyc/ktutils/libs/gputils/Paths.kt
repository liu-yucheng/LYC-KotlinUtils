// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils

import java.io.File

/** Paths. */
class Paths {
    companion object {
        /**
         * Joins path [path1] and [paths2] with the system default path name separator.
         * @param path1: a path
         * @param paths2: one or more paths
         */
        fun joinPaths(path1: String, vararg paths2: String): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            var file = File(path1)

            for (path2 in paths2) {
                file = file.resolve(path2)
            }

            val result = file.path
            return result
        } // end fun
    } // end companion
} // end class
