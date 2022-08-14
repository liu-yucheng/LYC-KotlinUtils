// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.Model
import ai.djl.inference.Predictor
import ai.djl.ndarray.NDList
import ai.djl.ndarray.types.Shape

/** Model operations. */
class ModelOps private constructor() {
    companion object {
        /** Generate a random noise batch (math notation: Z).
         * @param manager: a manager
         * @param batchSize: a batch size
         * @param dimSizes: some dimension sizes
         * @return result: the result
         */
        fun genRandNoiseBatch(batchSize: Int, dimSizes: Array<out Long>): NDList {
            val dimSizesList = dimSizes.toList()
            val shape = Shape(dimSizesList)
            val dataType = DJLDefaults.dataType
            val list = NDList()

            for (idx in 0 until batchSize) {
                val manager = DJLDefaults.ndManager
                val elem = manager.randomUniform(Float.MIN_VALUE, Float.MAX_VALUE, shape, dataType)
                list.add(elem)
            } // end for

            val result = list
            return result
        } // end fun

        /** Generate an image batch (math notation: G(Z)) by feeding a noise batch to a model.
         * @param manager: a manager
         * @param model: a model
         * @param noiseBatch: a noise batch
         * @return result: the result
         */
        fun genImage(
            model: Model, noiseBatch: NDList, inDimSizes: Array<out Long>, outDimSizes: Array<out Long>
        ): NDList {
            val translator = NDListTranslator(inDimSizes, outDimSizes)
            val device = DJLDefaults.device
            val predictor = Predictor(model, translator, device, true)
            val imageBatch = predictor.predict(noiseBatch)
            val result = imageBatch
            return result
        } // end fun
    } // end companion
} // end class
