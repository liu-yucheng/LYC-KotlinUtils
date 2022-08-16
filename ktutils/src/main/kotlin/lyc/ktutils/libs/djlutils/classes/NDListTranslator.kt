// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils.classes

import ai.djl.ndarray.NDList
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.index.NDIndex
import ai.djl.ndarray.types.Shape
import ai.djl.translate.NoBatchifyTranslator
import ai.djl.translate.TranslatorContext
import lyc.ktutils.libs.djlutils.DJLDefaults

/** ND list translator.
 * @param inDimSizes: input dimension sizes
 * @param outDimSizes: output dimension sizes
 */
class NDListTranslator(inDimSizes: Array<out Long>, outDimSizes: Array<out Long>) :
    NoBatchifyTranslator<NDList, NDList> {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

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

    /** Finds a dummy input ND list.
     *
     * The list contains 1 element with the given [shape].
     * The element has random entries.
     *
     * @param manager: a manager
     * @param shape: a shape
     * @return result: the result
     */
    private fun findDummyInNDList(manager: NDManager, shape: Shape): NDList {
        val elem = manager.randomNormal(0f, 1f, shape, DJLDefaults.dataType)
        val result = NDList(elem)
        return result
    } // end fun

    /** Finds a dummy output ND list.
     *
     * The list contains 1 element with the given [shape].
     * The element has random entries.
     *
     * @param manager: a manager
     * @param shape: a shape
     * @return result: the result
     */
    private fun findDummyOutNDList(manager: NDManager, shape: Shape): NDList {
        val elem = manager.randomUniform(-0.75f, 0.75f, shape, DJLDefaults.dataType)
        val result = NDList(elem)
        return result
    } // end fun

    override fun processInput(ctx: TranslatorContext?, input: NDList?): NDList {
        val manager = DJLDefaults.ndManager
        // println(input!![0].get(NDIndex("0:3, :, :"))) // Debug

        val result = if (input == null) {
            findDummyInNDList(manager, inShape)
        } else {
            var elemIdx = 0
            val shape = Shape(input.size.toLong(), *inShape.shape)
            val inputs = manager.create(shape)

            for (inputElem in input) {
                val indices = NDIndex("$elemIdx")
                inputs.set(indices, inputElem.duplicate())
                elemIdx += 1
            } // end for

            val ndList = NDList()
            ndList.add(inputs)
            ndList
        } // end val

        return result
    } // end fun

    override fun processOutput(ctx: TranslatorContext?, list: NDList?): NDList {
        val manager = DJLDefaults.ndManager
        // println(list!![0][0].get(NDIndex(":, 0:3, 0:3"))) // Debug

        val result = if (list == null) {
            findDummyOutNDList(manager, outShape)
        } else {
            if (list.isEmpty()) {
                findDummyOutNDList(manager, outShape)
            } else {
                val outputs = list[0]
                val outputCount = outputs.shape[0]
                val ndList = NDList()

                for (idx in 0 until outputCount) {
                    val indices = NDIndex("$idx")
                    val output = outputs.get(indices)
                    ndList.add(output.duplicate())
                } // end for

                ndList
            } // end if
        } // end val

        return result
    } // end fun
} // end class
