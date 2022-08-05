// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import lyc.ktutils.libs.composeutils.navs.CircPagesNav

/** Navigators. */
class Navs private constructor() {
    companion object {
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

            CircPagesNav.ContentOf(_4CircPagesNav)
        } // end fun
    } // end companion
} // end class
