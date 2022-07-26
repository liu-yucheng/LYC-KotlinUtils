// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.configfields.BoolField
import lyc.ktutils.libs.composeutils.configfields.ClampedFloatField
import lyc.ktutils.libs.composeutils.configfields.EvenIntGE0Field
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils
import lyc.ktutils.libs.gputils.jsonio.JSONTree

/** Configuration fields. */
class ConfigFields private constructor() {
    /** JSON field.
     * @param ValueType: a value type
     * @param root: a JSON root
     * @param elemKeys: some element keys
     * @param labelText: a label text
     * @param placeholderText: a placeholder text
     */
    abstract class JSONField<ValueType>(
        protected open val root: JsonElement, protected open vararg val keys: String = arrayOf(),
        protected open val labelText: String = "", protected open val placeholderText: String = ""
    ) {
        // Part of LYC-KotlinUtils
        // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
        // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

        /** Tree. */
        protected val tree by lazy { JSONTree(root) }

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

        /** Converts [value] from the JSON type of [childValue] to the type of [textState].`value`.
         * @return result: the conversion result
         */
        protected abstract fun valueToText(value: ValueType): String

        /** Converts [text] from the type of [textState].`value` to the JSON type of [childValue].
         * @return result: the conversion result
         */
        protected abstract fun textToValue(text: String): ValueType

        /** Child element value.
         *
         * JSON type: `String`
         * Kotlin type: [ValueType]
         */
        protected open var childValue: ValueType
            get() {
                val childString = childElem.toString()
                val result = textToValue(childString)
                return result
            } // end get
            set(value) {
                val valueString = value.toString()
                val elem = JsonPrimitive(valueString)
                childElem = elem
            } // end set

        /** Operation results. */
        protected enum class OpResult {
            /** Unchanged. */
            Unchanged,

            /** Valid. */
            Valid,

            /** Invalid. */
            Invalid,

            /** Error. */
            Error
        } // end class

        /** Text state. */
        protected val textState by lazy { mutableStateOf(valueToText(childValue)) }

        /** Rectifies [value] and returns the rectified value.
         *
         * @return result: the result
         */
        protected open fun rectifyValue(value: ValueType): ValueType {
            val result = value
            return result
        } // end fun

        /** Original child element value. */
        protected val origChildValue by lazy { childValue }

        /** Verifies and rectifies [textState].`value`.
         * @return result: the operation result */
        protected fun verifyValue(): OpResult {
            var result: OpResult

            try {
                val textValue = textToValue(textState.value)
                childValue = textValue

                if (textState.value == valueToText(origChildValue)) {
                    result = OpResult.Unchanged
                } else {
                    val rectified = rectifyValue(textValue)
                    childValue = rectified

                    result = if (textState.value == valueToText(rectified)) {
                        OpResult.Valid
                    } else {
                        OpResult.Invalid
                    } // end if
                } // end if
            } catch (exc: Exception) {
                result = OpResult.Error
            } // end try

            textState.value = valueToText(childValue)
            return result
        } // end fun

        /** Rectifies [newText] and returns the rectified text.
         * @param newText: a new text
         */
        protected fun rectifyText(newText: String): String {
            var value = textToValue(newText)
            value = rectifyValue(value)
            val text = valueToText(value)
            val result = text
            return result
        } // end fun

        /** Verifies and rectifies [newText].
         * @param newText: a new text
         * @return result: the operation result
         */
        protected fun verifyText(newText: String): OpResult {
            var result: OpResult

            try {
                textState.value = newText

                if (newText == valueToText(origChildValue)) {
                    result = OpResult.Unchanged
                } else {
                    val rectified = rectifyText(newText)

                    result = if (newText == rectified) {
                        OpResult.Valid
                    } else {
                        OpResult.Invalid
                    } // end if
                } // end if
            } catch (exc: Exception) {
                result = OpResult.Error
            } // end try

            return result
        } // end fun

        /** Unchanged colors. */
        protected val unchangedColors = @Composable {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.primary
            ) // end (
        } // end val

        /** Valid colors. */
        protected val validColors = @Composable {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = States.Theme.extColors.info,
                unfocusedBorderColor = States.Theme.extColors.info,
                focusedLabelColor = States.Theme.extColors.info,
                unfocusedLabelColor = States.Theme.extColors.info
            ) // end (
        } // end val

        /** Invalid colors. */
        protected val invalidColors = @Composable {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = States.Theme.extColors.warn,
                unfocusedBorderColor = States.Theme.extColors.warn,
                focusedLabelColor = States.Theme.extColors.warn,
                unfocusedLabelColor = States.Theme.extColors.warn
            ) // end (
        } // end val

        /** Error colors. */
        protected val errorColors = @Composable {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.error,
                unfocusedBorderColor = MaterialTheme.colors.error,
                focusedLabelColor = MaterialTheme.colors.error,
                unfocusedLabelColor = MaterialTheme.colors.error
            ) // end (
        } // end val

        /** Colors state. */
        protected val colorsState = mutableStateOf(unchangedColors)

        /** Content text style. */
        protected open val contentTextStyle = @Composable {
            TextStyle(
                color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ) // end TextStyle
        } // end val

        /** Content label. */
        protected open val contentLabel = @Composable {
            Text(labelText, fontWeight = FontWeight.Bold)
        } // end val

        /** Content placeholder. */
        protected open val contentPlaceholder = @Composable {
            Text(placeholderText, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        } // end val

        /** Text field on value change. */
        protected val onTextChange = { newText: String ->
            val opResult = verifyText(newText)

            when (opResult) {
                OpResult.Unchanged -> {
                    colorsState.value = unchangedColors
                } // end ->

                OpResult.Valid -> {
                    colorsState.value = validColors
                } // end ->

                OpResult.Invalid -> {
                    colorsState.value = invalidColors
                } // end ->

                OpResult.Error -> {
                    colorsState.value = errorColors
                } // end ->
            } // end when
        } // end val

        /** Text field on lose focus. */
        protected val onLoseFocus = {
            val opResult = verifyValue()

            when (opResult) {
                OpResult.Unchanged -> {
                    colorsState.value = unchangedColors
                } // end ->

                OpResult.Valid -> {
                    colorsState.value = validColors
                } // end ->

                OpResult.Invalid -> {
                    colorsState.value = invalidColors
                } // end ->

                OpResult.Error -> {
                    colorsState.value = errorColors
                } // end ->
            } // end when
        } // end val

        /** Text accessor. */
        var textAccess: String
            get() {
                val result = textState.value
                return result
            } // end get
            set(value) {
                onTextChange(value)
                onLoseFocus()
            } // end set

        /** Value accessor. */
        var valueAccess: ValueType
            get() {
                val result = childValue
                return result
            } // end get
            set(value) {
                val text = valueToText(value)
                textAccess = text
            } // end get

        /** Composes the content of the field.
         * @param modifier: a modifier
         */
        @Composable
        @Suppress("NAME_SHADOWING")
        fun Content(modifier: Modifier = Modifier) {
            /** Text field on focus changed. */
            val onFocusChange = { focusState: FocusState ->
                if (!focusState.hasFocus) {
                    onLoseFocus()
                } // end if
            } // end val

            val modifier = modifier.onFocusChanged(onFocusChange)

            OutlinedTextField(
                textState.value, onTextChange, modifier, textStyle = contentTextStyle(), label = contentLabel,
                placeholder = contentPlaceholder, colors = colorsState.value()
            ) // end OutlinedTextField
        } // end fun
    } // end class

    /** JSON string field.
     *
     * A basic JSONField child class example.
     *
     * @param root: a JSON root
     * @param elemKeys: some element keys
     * @param labelText: a label text
     */
    class StringField(
        override val root: JsonElement, override vararg val keys: String = arrayOf(), override val labelText: String
    ) : JSONField<String>(root, keys = keys, labelText, "String. Examples: foo, bar") {
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
                val result = childElem.asString
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

    companion object {
        /** String field. */
        val stringField = StringField(States.configFieldsDemoRoot, keys = arrayOf("string"), "String")

        /** Boolean field. */
        val boolField = BoolField(States.configFieldsDemoRoot, keys = arrayOf("boolean"), "Boolean")

        /** Even integer greater than or equal to 0 field. */
        val evenIntGE0Field = EvenIntGE0Field(
            States.configFieldsDemoRoot, keys = arrayOf("even_integer_ge_0"),
            "Even integer greater than or equal to 0"
        ) // end val

        /** Float range 0 to 100 field. */
        val floatRange0To100Field = ClampedFloatField(
            States.configFieldsDemoRoot, keys = arrayOf("float_range_0_to_100"),
            "Clamped float range 0 to 100", "Float, range [0.0, 100.0]. Example: 25.0, 75.0",
            0.0, 100.0
        ) // end val

        /** Save configs button. */
        @Composable
        fun SaveConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                val loc = Utils.joinPaths(Defaults.appDataPath, Defaults.configFieldsDemoName)
                Utils.saveJson(States.configFieldsDemoRoot, loc)
                Funcs.logln("Saved the ConfigFieldsDemo configs to ${Defaults.configFieldsDemoName} in app data")
            } // end val

            Button(onClick, modifier) { Text("Save the above configs") }
        } // end val
    } // end companion
} // end class
