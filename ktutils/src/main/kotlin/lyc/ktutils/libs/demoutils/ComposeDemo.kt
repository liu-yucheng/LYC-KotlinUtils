// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.demoutils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import kotlin.system.exitProcess
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

/** Compose demonstration.
 * @param packName: a package name
 * @param packVer: a package version
 * @param jarName: a JAR name
 * @param demoName: a demonstration name
 * @param cmdName: a command name
 * @param winUndecorated: whether the window is undecorated
 * @param winTransparent: whether the window is transparent
 */
open class ComposeDemo(
    protected val packName: String, protected val packVer: String, protected val jarName: String,
    protected val demoName: String, protected val cmdName: String, protected val winUndecorated: Boolean = false,
    protected val winTransparent: Boolean = false
) : ComposeDemoInterface {
    /** Arguments. */
    var args = arrayListOf<String>()

    /** Sets-up the demonstration. */
    protected fun setup() {
        Funcs.ensureUserData()

        var info = """
                - $packName $demoName logs
                -- Demo setup
                Log file location: ${States.logLoc}
            """.trimIndent() // end var

        Funcs.logln(info)
        States.sysDarkEnabled.value = Funcs.findSysDarkEnabled()
        States.darkEnabled.value = States.darkEnabled.value
        Funcs.logln("System dark themes enabled: ${States.sysDarkEnabled.value}")

        if (args.size > 0) {
            info = """
                    $demoName gets invalid arguments
                    Usage: java -jar $jarName $cmdName
                    Help: java -jar $jarName help
                """.trimIndent() // end info

            System.err.println(info)
            Funcs.logln(info, true)
            exitDemo(1)
        } // end if

        Funcs.logln("-- End Demo setup")
    } // end fun

    /** Tears-down the demonstration. */
    protected fun teardown() {
        val info = """
                -- Demo tear-down
                -- End Demo tear-down
                - End $packName $demoName logs
            """.trimIndent() // end val

        Funcs.logln(info)
        Funcs.flushLogs()
        Funcs.closeLogs()
    } // end fun

    /** Exits the demonstration.
     * @param statusCode: a status code
     */
    override fun exitDemo(statusCode: Int) {
        teardown()
        exitProcess(statusCode)
    } // end fun

    /** Settings buttons. */
    @Composable
    protected fun SettingsButtons() {
        Row(
            Modifier.fillMaxWidth().wrapContentHeight(),
            Arrangement.SpaceAround,
            Alignment.CenterVertically
        ) {
            Button(
                {
                    Funcs.logCrs()
                    Funcs.logln("Displayed copyrights")
                } // end onClick
            ) { Text("Display copyrights") } // end Button

            Button(
                {
                    States.darkEnabled.value = !States.darkEnabled.value
                    Funcs.updateFlatLafTheme()
                    Funcs.logln("Toggled light/dark themes")
                }, // end onClick

                colors = ButtonDefaults.buttonColors(backgroundColor = States.Theme.extColors.warn)
            ) {
                Text("Toggle light/dark themes", color = States.Theme.extColors.onWarn)
            } // end Button

            Button(
                {
                    Funcs.logln("User data: ${Defaults.userDataPath}")
                    val succ = Utils.openInExpl(Defaults.userDataPath)

                    if (succ) {
                        Funcs.logln("Opened user data folder on desktop")
                    } else {
                        Funcs.logln("Cannot open user data folder on desktop")
                    } // end if
                } // end onClick
            ) { Text("Browse user data") } // end Button

            Button(
                {
                    Funcs.ensureUserData(true)
                    Funcs.logln("Cleared user data")
                }, // end onClick

                colors = ButtonDefaults.buttonColors(backgroundColor = States.Theme.extColors.warn)
            ) { Text("Clear user data", color = States.Theme.extColors.onWarn) } // end Button
        } // end Row
    } // end fun

    /** Main window content. */
    @Composable
    override fun WindowScope.WinContent() {
        // Do nothing
    } // end fun

    /** Window title. */
    protected val winTitle = "$packName $packVer $demoName"

    /** Main window. */
    @Composable
    protected fun MainWin() {
        Funcs.setupFlatLafTheme()

        Window(
            ::exitDemo, States.mainWinState, true, winTitle, undecorated = winUndecorated,
            transparent = winTransparent, resizable = true
        ) { WinContent() } // end Window
    } // end fun

    /** Runs the command. */
    override fun run() {
        setup()
        application(false) { MainWin() }
    } // end fun
} // end class
