// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configfields

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.ConfigFields

/** Boolean field.
 * @param root: a JSON root
 * @param elemKeys: some element keys
 * @param labelText: a label text
 */
class BoolField(
    override val root: JsonElement, override vararg val keys: String = arrayOf(), override val labelText: String
) : ConfigFields.JSONField(root, keys = keys, labelText, "Boolean. Values: true, false.") {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Child value.
     *
     * JSON type: Boolean
     */
    override var childValue: Any
        get() {
            val result = childElem.asBoolean
            return result
        } // end get
        set(value) {
            val elem: JsonElement

            when (value) {
                is Boolean -> {
                    elem = JsonPrimitive(value)
                } // end is

                else -> {
                    val valueString = value.toString()
                    val valueInt = valueString.toBoolean()
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
        val result = text.toBoolean()
        return result
    } // end fun

    override fun rectifyValue(textValue: Any): Any {
        var textBool: Boolean

        when (textValue) {
            is Boolean -> {
                textBool = textValue
            } // end is

            else -> {
                val text = valueToText(textValue)
                textBool = text.toBoolean()
            } // end else
        } // end when

        val result = textBool
        return result
    } // end fun
} // end class
