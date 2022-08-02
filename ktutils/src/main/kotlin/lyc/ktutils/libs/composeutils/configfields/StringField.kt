package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

/** JSON string field.
 *
 * A basic JSONField child class example.
 *
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 */
class StringField(root: JsonElement, vararg keys: String = arrayOf(), labelText: String) :
    JSONField<String>(root, keys = keys, labelText, "String. Examples: foo, bar") {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

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
        val result = value
        return result
    } // end fun
} // end class
