// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.composeutils.aliases

import androidx.compose.runtime.Composable

/** Compose content. */
typealias Content = @Composable () -> Unit

/** Scoped compose content. */
typealias ScopedContent<Scope> = @Composable Scope.() -> Unit
