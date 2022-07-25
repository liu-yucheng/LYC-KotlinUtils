// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Even integer (greater than or equal to 0) field.
 * @param root: a JSON root
 * @param elemKeys: some element keys
 * @param labelText: a label text
 */
class EvenIntGE0Field(
    override val root: JsonElement, override vararg val keys: String = arrayOf(), override val labelText: String
) : ConfigFields.JSONField(root, keys = keys, labelText, "Even non-negative integer. Examples: 0, 2, 4...") {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: Int
     */
    override var childValue: Any
        get() {
            val result = childElem.asInt
            return result
        } // end get
        set(value) {
            val elem: JsonElement

            when (value) {
                is Int -> {
                    elem = JsonPrimitive(value)
                } // end is

                else -> {
                    val valueString = value.toString()
                    val valueInt = valueString.toInt()
                    elem = JsonPrimitive(valueInt)
                } // end else
            } // end when

            childElem = elem
        } // end set

    override fun valueToText(value: Any): String {
        val result = value.toString()
        return result
    } // end fun

    override fun textToValue(text: String): Any {
        val result = text.toInt()
        return result
    } // end fun

    override fun rectifyValue(textValue: Any): Any {
        var textInt: Int

        when (textValue) {
            is Int -> {
                textInt = textValue
            } // end is

            else -> {
                val text = valueToText(textValue)
                textInt = text.toInt()
            } // end else
        } // end when

        if (textInt < 0) {
            textInt = -textInt
        } // end if

        if (textInt.mod(2) != 0) {
            textInt += 1
        } // end if

        val result = textInt
        return result
    } // end fun
} // end class
