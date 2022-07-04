// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.gputils

import java.io.File

/** Text IO. */
class TextIO {
    companion object {
        /**
         * Loads the text contents of the file at [fromLoc].
         * @param fromLoc: a location
         * @return result: the text contents of the file
         */
        fun loadText(fromLoc: String): String {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val file = File(fromLoc)
            val result = file.readText()
            return result
        } // end fun

        /**
         * Saves the file contents [fromString] to file at [toLoc].
         * @param fromString: a string of file contents
         * @param toLoc: a location
         */
        fun saveText(fromString: String, toLoc: String) {
            // Part of liu-yucheng/MyKotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val file = File(toLoc)
            file.writeText(fromString)
        } // end fun
    } // end companion
} // end class
