// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils

import java.io.InputStream

/** Resources. */
class Reses {
    companion object {
        /**
         * Joins resource paths [path1] and [paths2] with the resource path name separator, "/".
         * @param path1: a resource path
         * @param paths2: one or more resource paths
         */
        fun joinResPaths(path1: String, vararg paths2: String): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            var result = path1

            for (path2 in paths2) {
                if (result.equals("")) {
                    result = path2
                } else {
                    result = "$result/$path2"
                } // end if
            } // end for

            return result
        } // end fun

        /**
         * Finds the stream of a resource file at [fromLoc].
         * NOTE: Please remember to manually close the stream after finishing reading it.
         * @param fromLoc: a resource location
         * @return result: the resource stream
         */
        fun findResStream(fromLoc: String): InputStream {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val result = object {}.javaClass.classLoader.getResourceAsStream(fromLoc)
            return result
        } // end fun
    } // end companion
} // end class
