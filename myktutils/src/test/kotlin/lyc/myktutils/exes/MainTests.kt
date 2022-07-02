// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.myktutils.exes

import kotlin.test.Test
import kotlin.test.assertNotNull

/** Main tests. */
class MainTests {
    @Test fun mainNotNull() {
        // Part of liu-yucheng/MyKotlinUtils
        // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
        // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

        val mainRef = ::main
        assertNotNull(mainRef, "The main function reference needs to be non-null")
    }
}
