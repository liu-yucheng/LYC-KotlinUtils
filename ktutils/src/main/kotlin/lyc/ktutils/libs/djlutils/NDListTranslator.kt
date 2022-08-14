// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.ndarray.NDList
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.Shape
import ai.djl.translate.Translator
import ai.djl.translate.TranslatorContext

/** ND list translator.
 * @param inDimSizes: input dimension sizes
 * @param outDimSizes: output dimension sizes
 */
class NDListTranslator(inDimSizes: Array<out Long>, outDimSizes: Array<out Long>) :
    Translator<NDList, NDList> {
    /** Input shape. */
    val inShape: Shape

    init {
        // Initialize inShape
        val dimSizesList = inDimSizes.toList()
        inShape = Shape(dimSizesList)
    } // end init

    /** Output shape. */
    val outShape: Shape

    init {
        // Initialize outShape
        val dimSizesList = outDimSizes.toList()
        outShape = Shape(dimSizesList)
    } // end init

    /** Finds a dummy ND list.
     *
     * The list contains 1 element with the given [shape].
     * The element has random entries.
     *
     * @param manager: a manager
     * @param shape: a shape
     * @return result: the result
     */
    private fun findDummyNDList(manager: NDManager, shape: Shape): NDList {
        val elem = manager.randomUniform(Float.MIN_VALUE, Float.MAX_VALUE, shape, DJLDefaults.dataType)
        val result = NDList(elem)
        return result
    } // end fun

    override fun processInput(ctx: TranslatorContext?, input: NDList?): NDList {
        val manager = if (ctx == null) {
            DJLDefaults.ndManager
        } else {
            ctx.ndManager
        } // end val

        val result = input ?: findDummyNDList(manager, inShape)
        return result
    } // end fun

    override fun processOutput(ctx: TranslatorContext?, list: NDList?): NDList {
        val manager = if (ctx == null) {
            DJLDefaults.ndManager
        } else {
            ctx.ndManager
        } // end val

        val result = list ?: findDummyNDList(manager, outShape)
        return result
    } // end fun
} // end class
