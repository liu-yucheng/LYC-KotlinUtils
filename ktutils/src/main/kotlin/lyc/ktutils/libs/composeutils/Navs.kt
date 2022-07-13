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
import lyc.ktutils.libs.composeutils.aliases.Content
import lyc.ktutils.libs.composeutils.aliases.UnitCallback
import lyc.ktutils.libs.composeutils.envs.Funcs

/** Navigators. */
class Navs private constructor() {
    companion object {
        /** Configuration. */
        private sealed class PageConfig : Parcelable {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            /** Previous-next page configuration.
             * @param idx: an index
             */
            @Parcelize
            data class PrevNext(val idx: Int) : PageConfig()
        } // end class

        /** Previous-next page.
         * @param idx: an index
         * @param count: a page count
         * @param onPrevClick: something to run when the previous button is clicked
         * @param onNextClick: something to run when the next button is clicked
         */
        @Composable
        private fun PrevNextPage(idx: Int, count: Int, onPrevClick: UnitCallback, onNextClick: UnitCallback) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            val pageNum = idx + 1

            Column(Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally) {
                Text("Previous-next page", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.weight(1f))
                Text("Page index: $idx", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(8.dp))
                Text("Page number: $pageNum", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.size(8.dp))
                Text("Page count: $count", color = MaterialTheme.colors.onBackground)
                Spacer(Modifier.weight(1f))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
                    Button(onPrevClick) { Text("Previous") }
                    Text("Page $pageNum / $count", color = MaterialTheme.colors.onBackground)
                    Button(onNextClick) { Text("Next") }
                } // end Row
            } // end Column
        } // end fun

        /** Circular pages navigator.
         * @param context: a component context
         * @param pageCount: a page count
         */
        private class CircPagesNav(context: ComponentContext, val pageCount: Int) : ComponentContext by context {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            /** Makes an initial stack.
             * @return result: the initial stack
             */
            private fun makeInitStack(): List<PageConfig> {
                val initStack = ArrayList<PageConfig>()

                for (idx in pageCount - 1 downTo 0) {
                    val config = PageConfig.PrevNext(idx)
                    initStack.add(config)
                } // end for

                val result = initStack.toList()
                return result
            } // end fun

            /** Makes page content.
             * @param config: a page config
             * @return result: the page compose content
             */
            private fun makePageContent(config: PageConfig.PrevNext): Content {
                val idx = config.idx

                val prevIdx = (idx - 1).mod(pageCount)
                val nextIdx = (idx + 1).mod(pageCount)

                val onPrevClick = {
                    router.bringToFront(PageConfig.PrevNext(prevIdx))
                    val pageNum = prevIdx + 1
                    Funcs.logln("Switched to page $pageNum / $pageCount")
                } // end val

                val onNextClick = {
                    router.bringToFront(PageConfig.PrevNext(nextIdx))
                    val pageNum = nextIdx + 1
                    Funcs.logln("Switched to page $pageNum / $pageCount")
                } // end val

                val result = @Composable { PrevNextPage(idx, pageCount, onPrevClick, onNextClick) }
                return result
            } // end fun

            /** Makes a child component.
             * @param config: a config
             * @param context: a component context
             * @return result: the child component
             */
            @Suppress("UNUSED_PARAMETER")
            private fun makeChildComp(config: PageConfig, context: ComponentContext): Content {
                val result: Content

                when (config) {
                    is PageConfig.PrevNext -> {
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
        private fun CircPages(nav: CircPagesNav) {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            Children(nav.routerState) { child -> child.instance() }
        } // end fun

        /** Lifecycle. */
        private val lifecycle = LifecycleRegistry()

        /** 4 circular pages navigator. */
        private val _4CircPagesNav = CircPagesNav(DefaultComponentContext(lifecycle), 4)

        /** 4 circular pages. */
        @Composable
        fun _4CircPages() {
            // Part of LYC-KotlinUtils
            // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
            // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

            CircPages(_4CircPagesNav)
        } // end fun
    } // end companion
} // end class
