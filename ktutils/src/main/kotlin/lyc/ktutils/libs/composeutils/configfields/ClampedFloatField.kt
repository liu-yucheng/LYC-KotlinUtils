// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import kotlin.math.max
import kotlin.math.min

/** Clamped float field.
 * @param root: a JSON root
 * @param keys: some element keys
 * @param labelText: a label text
 * @param placeholderText: a placeholder text
 * @param bound1: clamp bound 1 (usually minimum)
 * @param bound2: clamp bound 2 (usually maximum)
 */
class ClampedFloatField(
    root: JsonElement, vararg keys: String = arrayOf(), labelText: String, placeholderText: String, bound1: Double,
    bound2: Double
) : JSONField<Double>(root, keys = keys, 0.0, labelText, placeholderText) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Clamp minimum. */
    private val minimum = min(bound1, bound2)

    /** Clamp maximum. */
    private val maximum = max(bound1, bound2)

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
                defaultValue
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
            defaultValue
        } // end val

        return result
    } // end fun

    override fun rectifyValue(value: Double): Double {
        var result = value

        if (result < minimum) {
            result = minimum
        } else if (result > maximum) {
            result = maximum
        } // end if

        if (result.isNaN()) {
            pendingRectifyErr = true
        } // end if

        return result
    } // end fun
} // end class
