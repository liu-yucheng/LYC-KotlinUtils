// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import lyc.ktutils.libs.composeutils.aliases.Content
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.gputils.jsonio.JSONTree

/** Configuration fields. */
class ConfigFields private constructor() {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    companion object {
        /** JSON field.
         * @param root: a JSON root
         * @param elemKeys: some element keys
         */
        open class JSONField(protected val root: JsonElement, protected vararg val keys: String) {
            /** Tree. */
            protected val tree = JSONTree(root)

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

            /** Child element value. */
            protected open var childValue: String
                get() {
                    val result = childElem.toString()
                    return result
                } // end get
                set(value) {
                    val valueElem = JsonPrimitive(value)
                    childElem = valueElem
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
            protected open val textState by lazy { mutableStateOf(childValue) }

            /** Converts [text] from the type of [textState].`value` to the type of [childValue].
             * @return result: the conversion result
             */
            protected open fun textToValue(text: String): String {
                val result = text
                return result
            } // end fun

            /** Rectifies [textValue] and passes the rectified value to [childValue]. */
            protected open fun rectifyValue(textValue: String) {
                childValue = textValue
            } // end fun

            /** Verifies and rectifies [textState].`value`.
             * @return result: the operation result */
            protected open fun verifyValue(): OpResult {
                var result: OpResult

                try {
                    val textValue = textToValue(textState.value)

                    if (childValue == textValue) {
                        result = OpResult.Unchanged
                    } else {
                        rectifyValue(textValue)

                        result = if (childValue == textValue) {
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

            /** Rectifies [newText] and passes the rectified text to [textState].`value`.
             * @param newText: a new text
             */
            protected open fun rectifyText(newText: String) {
                textState.value = newText
            } // end fun

            /** Verifies and rectifies [newText].
             * @param newText: a new text
             * @return result: the operation result
             */
            protected open fun verifyText(newText: String): OpResult {
                var result: OpResult

                try {
                    if (textState.value == newText) {
                        result = OpResult.Unchanged
                    } else {
                        rectifyText(newText)

                        result = if (textState.value == newText) {
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
            protected val unchangedColors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary
            ) // end val

            /** Valid colors. */
            protected val validColors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = States.Theme.extColors.info
            ) // end val

            /** Invalid colors. */
            protected val invalidColors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = States.Theme.extColors.warn
            ) // end val

            /** Error colors. */
            protected val errorColors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.error
            ) // end val

            /** Colors state. */
            protected val colorsState = mutableStateOf(unchangedColors)

            /** Default text style. */
            val defaultTextStyle = TextStyle.Default

            /** Empty content. */
            val emptyContent = @Composable {}

            /** Composes the content of the field.
             * @param modifier: a modifier
             */
            @Composable
            @Suppress("NAME_SHADOWING")
            open fun Content(
                modifier: Modifier, textStyle: TextStyle = defaultTextStyle, label: Content = emptyContent,
                placeholder: Content = emptyContent
            ) {
                val onValueChange = { newValue: String ->
                    val result = verifyText(newValue)

                    when (result) {
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

                val modifier = modifier.onFocusChanged { focusState: FocusState ->
                    if (!focusState.hasFocus) {
                        val result = verifyValue()

                        when (result) {
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
                    } // end if
                } // end val

                OutlinedTextField(
                    textState.value, onValueChange, modifier, textStyle = textStyle, label = label,
                    placeholder = placeholder, colors = colorsState.value
                ) // end OutlinedTextField
            } // end fun
        } // end class
    } // end companion
} // end class
