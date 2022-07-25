// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.File

/** JSON IO. */
class JSONIO {
    companion object {
        /** A Gson object used for JSON related operations. */
        val gson = GsonBuilder().setPrettyPrinting().create()

        /**
         * Loads the JSON contents of the file at [fromLoc].
         * @param fromLoc: a location
         * @return result: the JSON contents of the file
         */
        fun loadJson(fromLoc: String): JsonElement {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val file = File(fromLoc)
            val text = file.readText()
            val result = gson.fromJson(text, JsonElement::class.java)
            return result
        } // end fun

        /**
         * Saves the file contents [fromJsonElement] to file at [toLoc].
         * @param: fromJsonElement: a JsonElement of the file contents
         * @param: toLoc: a location
         */
        fun saveJson(fromJsonElement: JsonElement, toLoc: String) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val text = gson.toJson(fromJsonElement)
            val file = File(toLoc)
            file.writeText(text)
        } // end fun
    } // end companion
} // end class
