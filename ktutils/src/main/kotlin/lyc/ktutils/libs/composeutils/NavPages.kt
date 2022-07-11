// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import lyc.ktutils.libs.composeutils.envs.Funcs

/** Compose content. */
typealias ComposeContent = @Composable () -> Unit

/** Navigation pages. */
class NavPages private constructor() {
    companion object {
        /** Configuration. */
        private sealed class Config : Parcelable {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            /** Page configuration.
             * @param idx: an index
             * @param count: a count
             */
            @Parcelize
            data class PageConfig(val idx: Int, val count: Int) : Config()
        } // end class

        /** Previous-next page.
         * @param idx: an index
         * @param count: a count
         * @param onPrevClick: something to run when the previous button is clicked
         * @param onNextClick: something to run when the next button is clicked
         */
        @Composable
        private fun PrevNextPage(idx: Int, count: Int, onPrevClick: () -> Unit, onNextClick: () -> Unit) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val pageNum = idx + 1
            Funcs.logln("Entered page $pageNum / $count")

            Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                Text("Previous-next page", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(8.dp))
                Text("Page index: $idx", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(8.dp))
                Text("Page number: $pageNum", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(8.dp))
                Text("Page count: $count", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(32.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
                    Button(onPrevClick) { Text("Previous") }
                    Text("Page $pageNum / $count", color = MaterialTheme.colors.onBackground)
                    Button(onNextClick) { Text("Next") }
                } // end Row
            } // end Column
        } // end fun

        /** Circular pages navigator.
         * @param context: a component context
         * @param count: a page count
         */
        private class CircPagesNav(context: ComponentContext, val count: Int) : ComponentContext by context {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            /** Makes an initial stack.
             * @return result: the initial stack
             */
            private fun makeInitStack(): List<Config> {
                val initStack = ArrayList<Config>()

                for (idx in count - 1 downTo 0) {
                    val config = Config.PageConfig(idx, count)
                    initStack.add(config)
                } // end for

                val result = initStack.toList()
                return result
            } // end fun

            /** Makes page content.
             * @param config: a page config
             * @return result: the page compose content
             */
            private fun makePageContent(config: Config.PageConfig): ComposeContent {
                val idx = config.idx
                val count = config.count

                val prevIdx = (idx - 1).mod(count)
                val nextIdx = (idx + 1).mod(count)

                val onPrevClick = { router.bringToFront(Config.PageConfig(prevIdx, count)) }
                val onNextClick = { router.bringToFront(Config.PageConfig(nextIdx, count)) }

                val result = @Composable { PrevNextPage(idx, count, onPrevClick, onNextClick) }
                return result
            } // end fun

            /** Makes a child component.
             * @param config: a config
             * @param context: a component context
             * @return result: the child component
             */
            @Suppress("UNUSED_PARAMETER")
            private fun makeChildComp(config: Config, context: ComponentContext): ComposeContent {
                val result: ComposeContent

                when (config) {
                    is Config.PageConfig -> {
                        result = makePageContent(config)
                    } // end is
                } // end when

                return result
            } // end fun

            /** Router. */
            private val router = router(::makeInitStack, childFactory = ::makeChildComp)

            /** Router state. */
            val routerState = router.state
        } // end class

        /** Circular pages navigator content.
         * @param nav: a circular pages navigator
         */
        @Composable
        @OptIn(com.arkivanov.decompose.ExperimentalDecomposeApi::class)
        private fun CircPagesNavContent(nav: CircPagesNav) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            Children(nav.routerState) { child -> child.instance() }
        } // end fun

        /** Life cycle. */
        private val lifecycle = LifecycleRegistry()

        /** Circular pages of 4 navigator. */
        private val circPagesOf4Nav = CircPagesNav(DefaultComponentContext(lifecycle), 4)

        /** Circular pages of 4. */
        @Composable
        fun CircPagesOf4() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            CircPagesNavContent(circPagesOf4Nav)
        } // end fun
    } // end companion
} // end class
