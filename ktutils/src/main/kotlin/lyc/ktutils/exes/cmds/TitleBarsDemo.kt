// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.exes.cmds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lyc.ktutils.exes.jarName
import lyc.ktutils.exes.packName
import lyc.ktutils.exes.packVer
import lyc.ktutils.libs.composeutils.TermOuts
import lyc.ktutils.libs.composeutils.TitleBars.Companion.AppFrame
import lyc.ktutils.libs.composeutils.envs.Defaults
import lyc.ktutils.libs.composeutils.envs.Funcs
import lyc.ktutils.libs.composeutils.envs.States
import lyc.ktutils.libs.composeutils.envs.Utils

class TitleBarsDemo private constructor() {
    companion object {
        /** Arguments. */
        var args = arrayListOf<String>()

        /** Sets-up the demonstration. */
        private fun setup() {
            Funcs.ensureAppData()
            Funcs.logln("- $packName TitleBarsDemo logs")
            Funcs.logln("-- Demo setup")
            Funcs.logln("Log file location: ${States.logLoc}")
            States.darkEnabled.value = Utils.findSysDarkEnabled()
            Funcs.logln("System dark themes enabled: ${States.darkEnabled.value}")

            if (args.size > 0) {
                Funcs.logln("TitleBarsDemo gets invalid arguments", true)
                Funcs.logln("Usage: java -jar $jarName title-bars-demo", true)
                Funcs.logln("Help: java -jar $jarName help", true)
                exitDemo(1)
            } // end if

            Funcs.logln("-- End Demo setup")
        } // end fun

        /** Tears-down the demonstration. */
        fun teardown() {
            Funcs.logln("-- Demo tear-down")
            Funcs.logln("-- End Demo tear-down")
            Funcs.logln("- End $packName TitleBarsDemo logs")
            Funcs.flushLogs()
            Funcs.closeLogs()
        } // end fun

        /** Exits the demonstration.
         * @param statusCode: a status code
         */
        fun exitDemo(statusCode: Int = 0) {
            teardown()
            Funcs.exitApp(statusCode)
        } // end fun

        /** Logs licenses. */
        fun logLics() {
            Funcs.logln("==== Open-source Licenses ====")

            Funcs.logln(
                Utils.findResStream(Utils.joinResPaths(Defaults.licsResPath, Defaults.openLicsName))
                    .bufferedReader().readText() // end Utils
            ) // end Funcs

            Funcs.logln("==== End Of Open-source Licenses ====")
            Funcs.logln("==== License ====")

            Funcs.logln(
                Utils.findResStream(Utils.joinResPaths(Defaults.licsResPath, Defaults.licName))
                    .bufferedReader().readText() // end Utils
            ) // end Funcs

            Funcs.logln("==== End Of License ====")
        }

        /** Main window. */
        @Composable
        fun MainWin() {
            Window(
                ::exitDemo, States.mainWinState, true, "$packName $packVer TitleBarsDemo",
                undecorated = true, transparent = true, resizable = true
            ) {
                AppFrame("TitleBarsDemo") {
                    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                        Row(
                            Modifier.fillMaxWidth().wrapContentHeight(),
                            Arrangement.SpaceAround,
                            Alignment.CenterVertically
                        ) {
                            Row(
                                Modifier.fillMaxWidth().wrapContentHeight(),
                                Arrangement.SpaceAround,
                                Alignment.CenterVertically
                            ) {
                                Button(
                                    {
                                        TermOutsDemo.logLics()
                                        Funcs.logln("Displayed licenses")
                                    } // end onClick
                                ) { Text("Display licenses") } // end Button

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
                                        Funcs.logln("App data: ${Defaults.appDataPath}")
                                        val succ = Funcs.openInExpl(Defaults.appDataPath)

                                        if (succ) {
                                            Funcs.logln("Opened app data folder on desktop")
                                        } else {
                                            Funcs.logln("Cannot open app data folder on desktop")
                                        } // end if
                                    } // end onClick
                                ) { Text("Browse app data") }
                                Button(
                                    {
                                        Funcs.ensureAppData(true)
                                        Funcs.logln("Cleared app data")
                                    }, // end onClick

                                    colors = ButtonDefaults.buttonColors(backgroundColor = States.Theme.extColors.warn)
                                ) { Text("Clear app data", color = States.Theme.extColors.onWarn) } // end Button
                            } // end Row
                        } // end Row

                        TermOuts.TermOuts()
                    } // end Row
                } // end Elems
            } // end Window
        } // end fun

        /** Runs the command. */
        fun run() {
            setup()
            application(false) { MainWin() }
        } // end fun
    } // end companion
} // end class
