// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.io.File
import lyc.ktutils.libs.composeutils.ConfigFields

/** Path field.
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 * @param defaultPath: a default path
 */
class PathField(
    root: JsonElement, vararg keys: String = arrayOf(), labelText: String, defaultPath: String = ""
) : ConfigFields.JSONField<String>(root, keys = keys, labelText, "Path.") {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    private val defaultPath by lazy { defaultPath }

    /** Child value.
     *
     * JSON type: `String`
     * Kotlin type: [String]
     */
    override var childValue: String
        get() {
            val result = try {
                childElem.asString
            } catch (exc: Exception) {
                pendingConvertErr = true
                ""
            } // end val

            return result
        } // end get
        set(value) {
            val elem = JsonPrimitive(value)
            childElem = elem
        } // end set

    override fun valueToText(value: String): String {
        val result = value
        return result
    } // end fun

    override fun textToValue(text: String): String {
        val result = text
        return result
    } // end fun

    override fun rectifyValue(value: String): String {
        val valueFile = File(value)
        val valueExists = valueFile.exists()
        val valueIsDir = valueFile.isDirectory

        var result = if (valueExists and valueIsDir) {
            value
        } else {
            defaultPath
        } // end if

        result = File(result).absolutePath
        return result
    } // end fun
} // end class
