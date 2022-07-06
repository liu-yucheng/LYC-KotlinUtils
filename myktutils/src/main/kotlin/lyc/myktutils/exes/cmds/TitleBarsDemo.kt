// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.exes.cmds

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
import lyc.myktutils.libs.composeutils.TermOuts
import lyc.myktutils.libs.composeutils.TitleBars.Companion.AppFrame
import lyc.myktutils.libs.composeutils.envs.Defaults
import lyc.myktutils.libs.composeutils.envs.Funcs
import lyc.myktutils.libs.composeutils.envs.States
import lyc.myktutils.libs.composeutils.envs.Utils

class TitleBarsDemo private constructor() {
    companion object {
        /** Arguments. */
        var args = arrayListOf<String>()

        /** Sets-up the demonstration. */
        private fun setup() {
            Funcs.ensureAppData()
            Funcs.logln("- liu-yucheng/MyKotlinUtils TitleBarsDemo logs")

            if (args.size > 0) {
                Funcs.logln("TitleBarsDemo gets invalid arguments", true)
                Funcs.logln("Usage: java -jar lyc-myktutils.jar title-bars-demo", true)
                Funcs.logln("Help: java -jar lyc-myktutils.jar help", true)
                Funcs.logln("- End liu-yucheng/MyKotlinUtils TitleBarsDemo logs", true)
                Funcs.exitApp(1)
            } // end if

            Funcs.logln("-- Demo setup")
            Funcs.logln("Log file location: ${States.logLoc}")
            States.darkEnabled.value = Utils.findSysDarkEnabled()
            Funcs.logln("System dark themes enabled: ${States.darkEnabled.value}")
            Funcs.logln("-- End Demo setup")
        } // end fun

        /** Tears-down the demonstration. */
        fun teardown() {
            Funcs.logln("-- Demo tear-down")
            Funcs.logln("-- End Demo tear-down")
            Funcs.logln("- End liu-yucheng/MyKotlinUtils TitleBarsDemo logs")
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
            Funcs.setupFlatLafTheme()

            Window(
                Funcs.Companion::exitApp, States.mainWinState, true, "TitleBarsDemo",
                undecorated = true, transparent = true, resizable = true
            ) {
                AppFrame("TitleBarsDemo") {
                    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                        Row(
                            Modifier.fillMaxWidth().wrapContentHeight(),
                            Arrangement.SpaceAround,
                            Alignment.CenterVertically
                        ) {
                            Button({ logLics() }) {
                                Text("Display licenses", color = States.Theme.extColors.onWarn)
                            } // end Button

                            Button(
                                { States.darkEnabled.value = !States.darkEnabled.value },
                                colors = ButtonDefaults.buttonColors(backgroundColor = States.Theme.extColors.warn)
                            ) {
                                Text("Toggle light/dark themes", color = States.Theme.extColors.onWarn)
                            } // end Button
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
            teardown()
        } // end fun
    } // end companion
} // end class
