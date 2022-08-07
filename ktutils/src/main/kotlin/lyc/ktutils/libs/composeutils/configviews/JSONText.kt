// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configviews

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

/** JSON text.
 * @param root: a JSON root
 * @param keys: some keys
 * @param defaultValue: a default value
 * @param textStyle: a text style
 */
class JSONText(
    root: JsonElement, vararg keys: String = arrayOf(), defaultValue: String,
    private val textStyle: @Composable () -> TextStyle = defaultTextStyle
) : JSONView<String>(root, keys = keys, defaultValue) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    override var childValue: String
        get() {
            val result = try {
                childElem.asString
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

    override fun valueToText(value: String): String {
        val result = value
        return result
    } // end fun

    override fun textToValue(text: String): String {
        val result = text
        return result
    } // end fun

    @Composable
    override fun Content(modifier: Modifier) {
        Text(text, modifier, style = textStyle())
    } // end fun

    companion object {
        val defaultTextStyle = @Composable {
            MaterialTheme.typography.body1.copy(
                MaterialTheme.colors.onBackground, fontFamily = FontFamily.Monospace
            ) // end (
        } // end val
    } // end companion
} // end class
