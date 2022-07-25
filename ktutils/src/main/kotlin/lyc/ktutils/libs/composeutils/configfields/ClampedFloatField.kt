// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Clamped float field.
 * @param root: a JSON root
 * @param elemKeys: some element keys
 * @param labelText: a label text
 * @param placeholderText: a placeholder text
 * @param min: a clamp minimum
 * @param max: a clamp maximum
 */
class ClampedFloatField(
    override val root: JsonElement, override vararg val keys: String = arrayOf(), override val labelText: String,
    override val placeholderText: String, private val min: Double, private val max: Double
) : ConfigFields.JSONField(root, keys = keys, labelText, placeholderText) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: Int
     */
    override var childValue: Any
        get() {
            val result = childElem.asDouble
            return result
        } // end get
        set(value) {
            val elem: JsonElement

            when (value) {
                is Double -> {
                    elem = JsonPrimitive(value)
                } // end is

                else -> {
                    val valueString = value.toString()
                    val valueDouble = valueString.toDouble()
                    elem = JsonPrimitive(valueDouble)
                } // end else
            } // end when

            childElem = elem
        } // end set

    override fun valueToText(value: Any): String {
        val result = value.toString()
        return result
    } // end fun

    override fun textToValue(text: String): Any {
        val result = text.toDouble()
        return result
    } // end fun

    override fun rectifyValue(textValue: Any): Any {
        var textDouble: Double

        when (textValue) {
            is Double -> {
                textDouble = textValue
            } // end is

            else -> {
                val text = valueToText(textValue)
                textDouble = text.toDouble()
            } // end else
        } // end when

        if (textDouble < min) {
            textDouble = min
        } else if (textDouble > max) {
            textDouble = max
        } // end if

        val result = textDouble
        return result
    } // end fun
} // end class
