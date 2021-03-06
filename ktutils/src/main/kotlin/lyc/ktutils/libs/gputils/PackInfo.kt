// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils

import java.util.Properties

/** Package information. */
class PackInfo {
    companion object {
        /** Finds the package version.
         * @param versPropsResPath: versions.properties resource path
         */
        fun findPackVer(versPropsResPath: String = "versions.properties"): String {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val propsParser = Properties()
            val stream = object {}.javaClass.classLoader.getResourceAsStream(versPropsResPath)
            propsParser.load(stream)
            val ver = propsParser["version"].toString()
            stream.close()
            return ver
        } // end fun
    } // end companion
} // end class
