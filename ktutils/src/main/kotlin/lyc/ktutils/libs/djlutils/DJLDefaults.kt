// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.Device
import ai.djl.engine.Engine
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.DataType
import ai.djl.pytorch.engine.PtEngine

/** DJL defaults.
 * @param inDimSizes: input dimension sizes
 * @param outDimSizes: output dimension sizes
 */
class DJLDefaults private constructor() {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    companion object {
        /** Engine.
         *
         * Currently [Engine].
         */
        val engine = Engine.getEngine(PtEngine.ENGINE_NAME)

        /** Device internal field. */
        private var deviceField = HWInfo.findDefaultGPU()

        /** Device. */
        val device: Device
            get() {
                val result = deviceField
                return result
            } // end get

        /** ND manager internal field. */
        private var ndManagerField = NDManager.newBaseManager(device, engine.engineName)

        /** ND manager. */
        val ndManager: NDManager
            get() {
                val result = ndManagerField
                return result
            } // end get

        /** Updates the defaults by a [gpuCount].
         * @param gpuCount: a GPU count
         */
        fun updateByGPUCount(gpuCount: Int) {
            if (gpuCount > 0) {
                deviceField = HWInfo.findDefaultGPU()
                ndManagerField = NDManager.newBaseManager(device, engine.engineName)
            } else {
                deviceField = Device.cpu()
                ndManagerField = NDManager.newBaseManager(device, engine.engineName)
            } // end if
        } // end fun

        /** CPU. */
        val cpu = Device.cpu()

        /** CPU ND manager. */
        val cpuNDManager = NDManager.newBaseManager(cpu, engine.engineName)

        /** Data type.
         *
         * Currently [DataType.FLOAT32].
         */
        val dataType = DataType.FLOAT32
    } // end companion
} // end class
