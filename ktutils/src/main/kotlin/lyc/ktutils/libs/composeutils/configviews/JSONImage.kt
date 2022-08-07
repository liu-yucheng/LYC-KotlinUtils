// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.configviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.io.File
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

/** JSON image.
 * @param root: a JSON root
 * @param keys: some keys
 * @param jsonLoc: a JSON location
 * @param contentDesc: a content description
 */
class JSONImage(
    root: JsonElement, vararg keys: String = arrayOf(), private val jsonLoc: String, private val contentDesc: String,
    private val iconColor: @Composable () -> Color = defaultIconColor
) : JSONView<String>(root, keys = keys, "") {
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

    /** JSON path */
    private val jsonPath: String

    init {
        // Initialize jsonPath
        val file = File(jsonLoc)

        val filePath = if (file.isDirectory) {
            file.absolutePath
        } else {
            file.parentFile.absolutePath
        } // end val

        jsonPath = filePath
    } // end init

    @Suppress("NAME_SHADOWING")
    @Composable
    override fun Content(modifier: Modifier) {
        var painter: Painter
        var excRaised: Boolean

        try {
            val loc = Utils.joinPaths(jsonPath, text)
            val file = File(loc)
            val stream = file.inputStream()
            val bitmap = loadImageBitmap(stream)
            stream.close()
            painter = BitmapPainter(bitmap)
            excRaised = false
        } catch (exc: Exception) {
            painter = painterResource(cannotOpenImageIconResLoc)
            excRaised = true
        } // end val

        Card(
            Modifier, RoundedCornerShape(4.dp), States.Theme.transparentColor, MaterialTheme.colors.onBackground,
            BorderStroke(4.dp, imageBorderColor()), 0.dp
        ) {
            val modifier = modifier.padding(4.dp)

            if (excRaised) {
                Icon(painter, contentDesc, modifier, iconColor())
            } else {
                Image(painter, contentDesc, modifier)
            } // end if
        } // end Card
    } // end fun

    companion object {
        /** Cannot open image icon resource location. */
        val cannotOpenImageIconResLoc = Utils.joinResPaths(Defaults.iconsResPath, Defaults.cannotOpenImageIconName)

        /** Default icon color. */
        val defaultIconColor = @Composable { MaterialTheme.colors.onBackground.copy(ContentAlpha.medium) }

        /** Image border color. */
        val imageBorderColor = @Composable { MaterialTheme.colors.primary.copy(ContentAlpha.medium) }
    } // end companion
} // end class
