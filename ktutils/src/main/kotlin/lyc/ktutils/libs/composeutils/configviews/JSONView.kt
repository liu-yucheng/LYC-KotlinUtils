// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configviews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.gputils.jsonio.JSONTree

/** JSON view.
 * @param ValueType: a value type
 * @param root: a JSON root
 * @param keys: some element keys
 * @param defaultValue: a default value
 */
abstract class JSONView<ValueType : Any>(
    root: JsonElement, protected vararg val keys: String = arrayOf(), protected val defaultValue: ValueType
) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** JSON root. */
    var root = root
        get() {
            val result = field
            return result
        } // end get
        set(value) {
            field = value
            textState.value = valueToText(childValue)
        } // end get

    /** Tree. */
    protected val tree: JSONTree
        get() {
            val result = JSONTree(root)
            return result
        } // end get

    /** Child element. */
    protected var childElem: JsonElement
        get() {
            val result = tree.get(*keys).elem
            return result
        } // end get
        set(value) {
            val valueTree = JSONTree(value)
            tree.set(keys = keys, valueTree)
        } // end set

    /** Whether there is a pending conversion error to handle. */
    protected var pendingConvertErr = false

    /** Converts [value] from the JSON type of [childValue] to the type of [textState].`value`.
     *
     * Note: Might raise (set to true) [pendingConvertErr].
     *
     * @return result: the conversion result
     */
    protected abstract fun valueToText(value: ValueType): String

    /** Converts [text] from the type of [textState].`value` to the JSON type of [childValue].
     *
     * Note: Might raise [pendingConvertErr].
     *
     * @return result: the conversion result
     */
    protected abstract fun textToValue(text: String): ValueType

    /** Default text. */
    protected val defaultText = ""

    /** Child element value.
     *
     * Note: The get accrssor might raise [pendingConvertErr].
     *
     * JSON type: `String`
     * Kotlin type: [ValueType]
     */
    protected open var childValue: ValueType
        get() {
            val childString = try {
                childElem.asString
            } catch (exc: Exception) {
                pendingConvertErr = true
                defaultText
            } // end val

            val result = textToValue(childString)
            return result
        } // end get
        set(value) {
            val valueString = value.toString()
            val elem = JsonPrimitive(valueString)
            childElem = elem
        } // end set

    /** Whether [textState] is initialized. */
    private var textStateInitted = false

    /** Text state. */
    protected val textState = mutableStateOf("<Not initialized>")
        get() {
            if (!textStateInitted) {
                textStateInitted = true
                field.value = valueToText(childValue)
            } // end if

            val result = field
            return result
        } // end get

    /** Text accessor. */
    val text: String
        get() {
            val result = textState.value
            return result
        } // end get

    /** Composes the view content.
     * @param modifier: a modifier
     */
    @Composable
    abstract fun Content(modifier: Modifier)
} // end class
