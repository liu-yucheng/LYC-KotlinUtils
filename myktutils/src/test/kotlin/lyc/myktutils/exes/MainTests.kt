// Copyright 2022 Yucheng Liu. All rights reserved.
// First added by: Yucheng Liu
// Last updated by: Yucheng Liu

package aidesign.gantestgui.exes

import kotlin.test.Test
import kotlin.test.assertNotNull

/** Main tests. */
class MainTests {
    @Test fun mainNotNull() {
        val mainRef = ::main
        assertNotNull(mainRef, "The main function reference needs to be non-null")
    }
}
