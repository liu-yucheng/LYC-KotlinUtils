// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.gputils

import java.util.Properties

fun findPackVer(verPropResPath: String = "versions.properties"): String {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    val propParser = Properties()
    val verPropStream = object {}.javaClass.classLoader.getResourceAsStream(verPropResPath)
    propParser.load(verPropStream)
    val ver = propParser["version"].toString()
    verPropStream.close()
    return ver
} // end fun
