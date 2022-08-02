// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Even integer (greater than or equal to 0) field.
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 */
class EvenIntGE0Field(root: JsonElement, vararg keys: String = arrayOf(), labelText: String) :
    ConfigFields.JSONField<Long>(
        root, keys = keys, labelText, "Even non-negative integer. Examples: 0, 2, 4..."
    ) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: `Int`
     * Kotlin type: [Long]
     */
    override var childValue: Long
        get() {
            val result = try {
                childElem.asLong
            } catch (exc: Exception) {
                pendingConvertErr = true
                0L
            } // end val

            return result
        } // end get
        set(value) {
            val elem = JsonPrimitive(value)
            childElem = elem
        } // end set

    override fun valueToText(value: Long): String {
        val result = value.toString()
        return result
    } // end fun

    override fun textToValue(text: String): Long {
        val result = try {
            text.toLong()
        } catch (exc: Exception) {
            pendingConvertErr = true
            0L
        } // end val

        return result
    } // end fun

    override fun rectifyValue(value: Long): Long {
        var result = value

        if (result < 0) {
            result = -result
        } // end if

        if (result.mod(2) != 0) {
            result += 1
        } // end if

        return result
    } // end fun
} // end class
