// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
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
     * @param keys: some element keys
     * @param labelText: a label text
     * @param placeholderText: a placeholder text
     */
    abstract class JSONField<ValueType>(
        root: JsonElement, protected vararg val keys: String = arrayOf(), protected val labelText: String = "",
        protected val placeholderText: String = ""
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
                origChildValue = rectifyValue(childValue)
                onTextChange(valueToText(childValue))
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
                    ""
                } // end val

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

        /** Whether there is a pending rectification error to handle. */
        protected var pendingRectifyErr = false

        /** Rectifies [value] and returns the rectified value.
         *
         * Note: Might raise [pendingRectifyErr].
         *
         * @return result: the result
         */
        protected open fun rectifyValue(value: ValueType): ValueType {
            val result = value
            return result
        } // end fun

        /** Original child element value. */
        protected var origChildValue = lazy { rectifyValue(childValue) }.value

        /** Verifies and rectifies [textState].`value`.
         *
         * Note: Handles [pendingConvertErr], [pendingRectifyErr].
         *
         * @return result: the operation result
         */
        protected fun verifyValue(): OpResult {
            val textValue = textToValue(textState.value)
            var result: OpResult

            if (textState.value == valueToText(origChildValue)) {
                childValue = textValue
                result = OpResult.Unchanged
            } else {
                val rectified = rectifyValue(textValue)
                childValue = rectified

                if (textState.value == valueToText(rectified)) {
                    result = OpResult.Valid
                } else {
                    result = OpResult.Invalid
                } // end if
            } // end if

            textState.value = valueToText(childValue)

            if (pendingConvertErr or pendingRectifyErr) {
                pendingConvertErr = false
                pendingRectifyErr = false
                result = OpResult.Error
            } // end if

            return result
        } // end fun

        /** Rectifies [newText] and returns the rectified text.
         *
         * Note: Handles [pendingConvertErr].
         * Note: Might raise [pendingRectifyErr].
         *
         * @param newText: a new text
         */
        protected fun rectifyText(newText: String): String {
            var value = textToValue(newText)
            value = rectifyValue(value)
            val text = valueToText(value)

            if (pendingConvertErr) {
                pendingConvertErr = false
                pendingRectifyErr = true
            } // end if

            val result = text
            return result
        } // end fun

        /** Verifies and rectifies [newText].
         *
         * Note: Handles [pendingConvertErr], [pendingRectifyErr].
         *
         * @param newText: a new text
         * @return result: the operation result
         */
        protected fun verifyText(newText: String): OpResult {
            var result: OpResult

            if (newText == valueToText(origChildValue)) {
                result = OpResult.Unchanged
            } else {
                val rectified = rectifyText(newText)

                if (newText == rectified) {
                    result = OpResult.Valid
                } else {
                    result = OpResult.Invalid
                } // end if
            } // end if

            textState.value = newText

            if (pendingConvertErr or pendingRectifyErr) {
                pendingConvertErr = false
                pendingRectifyErr = false
                result = OpResult.Error
            } // end if

            return result
        } // end fun

        /** Outlined text field colors. */
        protected enum class Colors(val colors: @Composable () -> TextFieldColors) {
            /** Unchanged colors. */
            Unchanged(
                {
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.primary.copy(ContentAlpha.medium),
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.primary.copy(ContentAlpha.medium)
                    ) // end (
                } // end colors
            ), // end Unchanged

            /** Valid colors. */
            Valid(
                {
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = States.Theme.extColors.info,
                        unfocusedBorderColor = States.Theme.extColors.info.copy(ContentAlpha.medium),
                        focusedLabelColor = States.Theme.extColors.info,
                        unfocusedLabelColor = States.Theme.extColors.info.copy(ContentAlpha.medium)
                    ) // end (
                } // end colors
            ), // end Valid

            /** Invalid colors. */
            Invalid(
                {
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = States.Theme.extColors.warn,
                        unfocusedBorderColor = States.Theme.extColors.warn.copy(ContentAlpha.medium),
                        focusedLabelColor = States.Theme.extColors.warn,
                        unfocusedLabelColor = States.Theme.extColors.warn.copy(ContentAlpha.medium)
                    ) // end (
                } // end colors
            ), // end Invalid

            /** Error colors. */
            Error(
                {
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.error,
                        unfocusedBorderColor = MaterialTheme.colors.error.copy(ContentAlpha.medium),
                        focusedLabelColor = MaterialTheme.colors.error,
                        unfocusedLabelColor = MaterialTheme.colors.error.copy(ContentAlpha.medium)
                    ) // end (
                } // end colors
            ) // end Error
        } // end class

        /** Colors state. */
        protected val colorsState = mutableStateOf(Colors.Unchanged)

        init {
            val newText = lazy { valueToText(childValue) }.value
            val opResult = verifyText(newText)
            colorsState.value = opResultToColors(opResult)
        } // end init

        /** Project [OpResult] to [Colors]
         * @param opResult: an operation result
         */
        protected fun opResultToColors(opResult: OpResult): Colors {
            val result = when (opResult) {
                OpResult.Unchanged -> Colors.Unchanged
                OpResult.Valid -> Colors.Valid
                OpResult.Invalid -> Colors.Invalid
                OpResult.Error -> Colors.Error
            } // end when

            return result
        } // end fun

        /** Text field on value change. */
        protected val onTextChange = { newText: String ->
            val opResult = verifyText(newText)
            colorsState.value = opResultToColors(opResult)
        } // end val

        /** Prepares for saving.
         *
         * Note: To be invoked before saving the root.
         */
        fun prepSave() {
            val newText = textState.value
            val rectified = rectifyText(newText)
            var opResult = verifyText(rectified)
            colorsState.value = opResultToColors(opResult)
            opResult = verifyValue()
            colorsState.value = opResultToColors(opResult)
            root = root // Note: This operation has side effect
        } // end fun

        /** Opt out next on lose focus.
         *
         * Makes the content opt out the first on lose focus callback.
         */
        protected var optOutNextOnLoseFocus = true

        /** Text field on lose focus.
         *
         *  Note: Handles [optOutNextOnLoseFocus].
         */
        protected val onLoseFocus = {
            if (optOutNextOnLoseFocus) {
                optOutNextOnLoseFocus = false
            } else {
                val opResult = verifyValue()
                colorsState.value = opResultToColors(opResult)
            } // end if
        } // end val

        /** Text program side accessor. */
        var text: String
            get() {
                val result = textState.value
                return result
            } // end get
            set(value) {
                onTextChange(value)
                onLoseFocus()
            } // end set

        /** Value program side accessor. */
        var value: ValueType
            get() {
                val result = childValue
                return result
            } // end get
            set(value) {
                val text = valueToText(value)
                this.text = text
            } // end get

        /** Content text style. */
        protected open val textStyle = @Composable {
            TextStyle(
                color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ) // end TextStyle
        } // end val

        /** Content label. */
        protected open val label = @Composable { Text(labelText, fontWeight = FontWeight.Bold) }

        /** Content placeholder. */
        protected open val placeholder = @Composable {
            Text(placeholderText, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        } // end val

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
                textState.value, onTextChange, modifier, textStyle = textStyle(), label = label,
                placeholder = placeholder, colors = colorsState.value.colors()
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

        /** All fields. */
        val allFields = arrayListOf(stringField, boolField, evenIntGE0Field, floatRange0To100Field)

        /** Load configs button. */
        @Composable
        fun LoadConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                val loc = Utils.joinPaths(Defaults.appDataPath, Defaults.configFieldsDemoName)
                States.configFieldsDemoRoot = Utils.loadJson(loc)

                for (field in allFields) {
                    field.root = States.configFieldsDemoRoot
                } // end for

                Funcs.logln("Loaded the configs from ${Defaults.configFieldsDemoName} in app data")
            } // end val

            Button(onClick, modifier) { Text("Load configs") }
        } // end val

        /** Save configs button. */
        @Composable
        fun SaveConfigsButton(modifier: Modifier = Modifier) {
            val onClick = {
                val loc = Utils.joinPaths(Defaults.appDataPath, Defaults.configFieldsDemoName)

                for (field in allFields) {
                    field.prepSave()
                } // end for

                Utils.saveJson(States.configFieldsDemoRoot, loc)
                Funcs.logln("Saved the configs to ${Defaults.configFieldsDemoName} in app data")
            } // end val

            Button(onClick, modifier) { Text("Save configs") }
        } // end val
    } // end companion
} // end class
