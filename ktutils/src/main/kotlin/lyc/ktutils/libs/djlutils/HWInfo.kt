// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.Device

/** Hardware information. */
class HWInfo private constructor() {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    companion object {
        /** Finds the GPU count.
         * @return result: the result
         */
        fun findGPUCount(): Int {
            val engine = DJLDefaults.engine
            val result = engine.gpuCount
            return result
        } // end fun

        /** Finds if any GPUs are available.
         * @return result: the result
         */
        fun findGPUsAvailable(): Boolean {
            val engine = DJLDefaults.engine
            val gpuCount = engine.gpuCount
            val result = gpuCount > 0
            return result
        } // end fun

        /** Finds the default GPU.
         *
         * If no GPU is available, the result is [Device.cpu].
         *
         * @return result: the result
         */
        fun findDefaultGPU(): Device {
            val gpuAvailable = findGPUsAvailable()
            val gpuID = 0

            val result = if (gpuAvailable) {
                Device.gpu(gpuID)
            } else {
                Device.cpu()
            } // end val

            return result
        } // end fun

        /** Finds all the GPUs.
         *
         * If no GPU is available, the result is an [ArrayList] that contains only [Device.cpu].
         *
         * @return result: the result
         */
        fun findAllGPUs(): ArrayList<Device> {
            val gpuAvailable = findGPUsAvailable()
            val gpuCount = findGPUCount()
            val result = ArrayList<Device>()

            if (gpuAvailable) {
                for (idx in 0 until gpuCount) {
                    val device = Device.gpu(idx)
                    result.add(device)
                } // end for
            } else {
                val device = Device.cpu()
                result.add(device)
            } // end if

            return result
        } // end fun
    } // end companion
} // end class
