// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Clamped float field.
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 * @param placeholderText: a placeholder text
 * @param min: a clamp minimum
 * @param max: a clamp maximum
 */
class ClampedFloatField(
    root: JsonElement, vararg keys: String = arrayOf(), labelText: String, placeholderText: String,
    private val min: Double, private val max: Double
) : JSONField<Double>(root, keys = keys, labelText, placeholderText) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: `Float`
     * Kotlin type: [Double]
     */
    override var childValue: Double
        get() {
            val result = try {
                childElem.asDouble
            } catch (exc: Exception) {
                pendingConvertErr = true
                0.0
            } // end val

            return result
        } // end get
        set(value) {
            val elem = JsonPrimitive(value)
            childElem = elem
        } // end set

    override fun valueToText(value: Double): String {
        val result = value.toString()
        return result
    } // end fun

    override fun textToValue(text: String): Double {
        val result = try {
            text.toDouble()
        } catch (exc: Exception) {
            pendingConvertErr = true
            0.0
        } // end val

        return result
    } // end fun

    override fun rectifyValue(value: Double): Double {
        var result = value

        if (result < min) {
            result = min
        } else if (result > max) {
            result = max
        } // end if

        if (result.isNaN()) {
            pendingRectifyErr = true
        } // end if

        return result
    } // end fun
} // end class
