// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Boolean field.
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 */
class BoolField(root: JsonElement, vararg keys: String = arrayOf(), labelText: String) :
    JSONField<Boolean>(root, keys = keys, labelText, "Boolean. Values: true, false.") {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: `Boolean`
     * Kotlin type: [Boolean]
     */
    override var childValue: Boolean
        get() {
            val result = try {
                childElem.asBoolean
            } catch (exc: Exception) {
                pendingConvertErr = true
                false
            } // end val

            return result
        } // end get
        set(value) {
            val elem = JsonPrimitive(value)
            childElem = elem
        } // end set

    override fun valueToText(value: Boolean): String {
        val result = value.toString()
        return result
    } // end fun

    override fun textToValue(text: String): Boolean {
        val result = try {
            text.toBoolean()
        } catch (exc: Exception) {
            pendingConvertErr = true
            false
        } // end val

        return result
    } // end fun

    override fun rectifyValue(value: Boolean): Boolean {
        val result = value
        return result
    } // end fun
} // end class
