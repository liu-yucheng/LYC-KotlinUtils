// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.engine.Engine
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.DataType

/** DJL defaults.
 * @param inDimSizes: input dimension sizes
 * @param outDimSizes: output dimension sizes
 */
class DJLDefaults private constructor() {
    companion object {
        /** Engine.
         *
         * Currently [Engine].
         */
        val engine = Engine.getInstance()

        /** Device. */
        val device = HWInfo.findDefaultGPU()

        /** ND manager. */
        val ndManager = NDManager.newBaseManager(device, engine.engineName)

        /** Data type.
         *
         * Currently [DataType.FLOAT32].
         */
        val dataType = DataType.FLOAT32
    } // end companion
} // end class
