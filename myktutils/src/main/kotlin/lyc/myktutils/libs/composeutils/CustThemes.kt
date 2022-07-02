// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.libs.composeutils

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Extended colors. */
@Immutable
data class ExtColors(
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Terminal emulator background color. */
    val termBg: Color,

    /** Terminal emulator foreground color. */
    val termFg: Color,

    /** Mouse hover color. */
    val hoverColor: Color,

    /** Close mouse hover color. */
    val closeHoverColor: Color
) // end data class

/** Local extended colors. */
val LocalExtColors = staticCompositionLocalOf {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    ExtColors(Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified)
} // end val

/** Custom theme.
 * @param colors: a set of material theme standard colors
 * @param extColors: a set of extended colors
 * @param content: some display content
 */
@Composable
fun CustTheme(colors: Colors, extColors: ExtColors, content: @Composable () -> Unit) {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    CompositionLocalProvider(LocalExtColors provides extColors) {
        MaterialTheme(colors, content = content)
    } // end CompositionLocalProvider
} // end fun

/** Custom theme type. */
open class CustThemeType {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Extra colors. */
    val extColors: ExtColors
        @Composable get() = LocalExtColors.current

    /** Transparent color. */
    val transparentColor = Color(0x00_80_80_80)
} // end open class

/** Custom theme object. */
val CustTheme = object : CustThemeType() {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
}

/** Light theme. */
@Composable
fun LightTheme(content: @Composable () -> Unit) {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    val colors = lightColors(
        Color(0xff_00_60_c0), Color(0xff_00_37_8f), // Primary colors
        Color(0xff_60_00_c0), Color(0xff_21_00_8e), // Secondary colors
        Color(0xff_fa_fa_fa), Color(0xff_ff_ff_ff), // Background and surface colors
        Color(0xff_b0_00_20), // Error color
        Color(0xff_ff_ff_ff), Color(0xff_ff_ff_ff), // On-primary and on-secondary colors
        Color(0xff_00_00_00), Color(0xff_00_00_00), // On-background and on-surface colors
        Color(0xff_ff_ff_ff) // On-Error color
    ) // end val

    val extColors = ExtColors(
        Color(0xff_e0_e0_e0), Color(0xff_21_21_21), // Terminal bg and fg colors
        Color(0xff_ee_ee_ee), Color(0xff_cf_66_79) // Hover colors
    ) // end val

    CustTheme(colors, extColors, content)
} // end fun

/** Light theme object. */
val LightTheme = object : CustThemeType() {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
}

/** Dark theme. */
@Composable
fun DarkTheme(content: @Composable () -> Unit) {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    val colors = darkColors(
        Color(0xff_5f_af_ff), Color(0xff_97_e1_ff), // Primary colors
        Color(0xff_af_5f_ff), Color(0xff_e5_90_ff), // Secondary colors
        Color(0xff_12_12_12), Color(0xff_17_17_17), // Background and surface colors
        Color(0xff_cf_66_79), // Error color
        Color(0xff_00_00_00), Color(0xff_00_00_00), // On-primary and on-secondary colors
        Color(0xff_ff_ff_ff), Color(0xff_ff_ff_ff), // On-background and on-surface colors
        Color(0xff_00_00_00) // On-Error color
    ) // end val

    val extColors = ExtColors(
        Color(0xff_21_21_21), Color(0xff_e0_e0_e0), // Terminal bg and fg colors
        Color(0xff_42_42_42), Color(0xff_b0_00_20) // Hover colors
    ) // end val

    CustTheme(colors, extColors, content)
} // end fun

/** Dark theme object. */
val DarkTheme = object : CustThemeType() {
    // Part of liu-yucheng/MyKotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0
}