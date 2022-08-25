// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils.classes

import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDList
import ai.djl.ndarray.index.NDIndex
import ai.djl.ndarray.types.DataType
import ai.djl.ndarray.types.Shape
import com.google.gson.JsonElement
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.stream.MemoryCacheImageOutputStream
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.properties.Delegates
import kotlin.random.Random
import lyc.ktutils.libs.djlutils.DJLDefaults
import lyc.ktutils.libs.djlutils.ModelIO
import lyc.ktutils.libs.djlutils.ModelOps
import lyc.ktutils.libs.envs.Defaults
import lyc.ktutils.libs.envs.Utils
import lyc.ktutils.libs.gputils.jsonio.JSONTree

/** Generation process.
 * @param appDataPath: an app data path
 * @param exportPath: an exportation path
 */
class GenProc(
    val appDataPath: String, val exportPath: String, private val logstr: (String) -> Unit,
    private val logln: (String) -> Unit
) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Generation results path */
    val genResultsPath = Utils.joinPaths(exportPath, Defaults.genResultsName)

    /** Generation configuration location. */
    private val genConfigLoc = Utils.joinPaths(appDataPath, Defaults.djlGenProcsDemoName)

    /** Generator configuration location. */
    private val generatorConfigLoc = Utils.joinPaths(exportPath, Defaults.generatorConfigName)

    /** Generation configuration root. */
    private lateinit var genConfigRoot: JsonElement

    /** Generator configuration root. */
    private lateinit var generatorConfigRoot: JsonElement

    /** Whether auto seed is enabled. */
    private var autoSeedEnabled by Delegates.notNull<Boolean>()

    /** Manual seed. */
    private var manualSeed by Delegates.notNull<Long>()

    /** GPU count. */
    private var gpuCount by Delegates.notNull<Int>()

    /** Image count. */
    private var imageCount by Delegates.notNull<Int>()

    /** Images per batch. */
    private var imagesPerBatch by Delegates.notNull<Int>()

    /** Image quality. */
    private var imageQuality by Delegates.notNull<Int>()

    /** Whether grid mode is enabled. */
    private var gridModeEnabled by Delegates.notNull<Boolean>()

    /** Grid mode images per grid. */
    private var gridModeImagesPerGrid by Delegates.notNull<Int>()

    /** Grid mode padding. */
    private var gridModePadding by Delegates.notNull<Int>()

    /** Tries to get a value with default.
     * @param ValueType: a value type
     * @param opsToTry: some operations to try
     * @param default: a default value
     * @return result: the result
     */
    private fun <ValueType> tryValue(default: ValueType, opsToTry: () -> ValueType): ValueType {
        val result = try {
            opsToTry()
        } catch (exc: Exception) {
            default
        } // end val

        return result
    } // end fun

    /** Rectifies [inValue] to be an [Int] that is greater than or equal to 0.
     * @param inValue: an input value
     * @return result: the result
     */
    private fun rectifyIntGE0(inValue: Int): Int {
        var result = inValue

        if (result < 0) {
            result *= -1
        } // end if

        return result
    } // end fun

    /** Rectifies [inValue] to be an [Int] that is greater than or equal to 1.
     * @param inValue: an input value
     * @return result: the result
     */
    private fun rectifyIntGE1(inValue: Int): Int {
        var result = inValue

        if (result < 0) {
            result *= -1
        } // end if

        if (result < 1) {
            result = 1
        } // end if

        return result
    } // end fun

    /** Rectifies [inValue] to be an [Int] that is clamped between [bound1] and [bound2].
     * @param inValue: an input value
     * @param bound1: a bound 1
     * @param bound2: a bound 2
     * @return result: the result
     */
    private fun rectifyIntClamped(inValue: Int, bound1: Int, bound2: Int): Int {
        var result = inValue
        val lower = min(bound1, bound2)
        val upper = max(bound1, bound2)

        if (result < lower) {
            result = lower
        } // end if

        if (result > upper) {
            result = upper
        } // end if

        return result
    } // end fun

    /** Parses generation configuration. */
    private fun parseGenConfig() {
        logln("Started parsing generation configuration")
        val tree = JSONTree(genConfigRoot)

        autoSeedEnabled = tryValue(true) { tree["auto_seed_enabled"].elem.asBoolean }
        logln("Parsed auto seed enabled: $autoSeedEnabled")

        manualSeed = tryValue(0L) { tree["manual_seed"].elem.asLong }
        logln("Parsed manual seed: $manualSeed")

        gpuCount = tryValue(1) { tree["gpu_count"].elem.asInt }
        gpuCount = rectifyIntGE0(gpuCount)
        logln("Parsed GPU count: $gpuCount")

        imageCount = tryValue(256) { tree["image_count"].elem.asInt }
        imageCount = rectifyIntGE0(imageCount)
        logln("Parsed image count: $imageCount")

        imagesPerBatch = tryValue(32) { tree["images_per_batch"].elem.asInt }
        imagesPerBatch = rectifyIntGE1(imagesPerBatch)
        logln("Parsed images per batch: $imagesPerBatch")

        imageQuality = tryValue(95) { tree["image_quality"].elem.asInt }
        imageQuality = rectifyIntClamped(imageQuality, 0, 100)
        logln("Parsed image quality: $imageQuality")

        gridModeEnabled = tryValue(true) { tree["grid_mode", "enabled"].elem.asBoolean }
        logln("Parsed grid mode - enabled: $gridModeEnabled")

        gridModeImagesPerGrid = tryValue(64) { tree["grid_mode", "images_per_grid"].elem.asInt }
        gridModeImagesPerGrid = rectifyIntGE1(gridModeImagesPerGrid)
        logln("Parsed grid mode - images per grid: $gridModeImagesPerGrid")

        gridModePadding = tryValue(2) { tree["grid_mode", "padding"].elem.asInt }
        gridModePadding = rectifyIntGE0(gridModePadding)
        logln("Parsed grid mode - padding: $gridModePadding")

        logln("Completed parsing generation configuration")
    } // end fun

    /** Input noise resolution. */
    private var noiseRes by Delegates.notNull<Int>()

    /** Input noise channel count. */
    private var noiseChannelCount by Delegates.notNull<Int>()

    /** Output image resolution. */
    private var imageRes by Delegates.notNull<Int>()

    /** Output image channel count. */
    private var imageChannelCount by Delegates.notNull<Int>()

    /** Parses generator configuration. */
    private fun parseGeneratorConfig() {
        logln("Started parsing generator configuration")
        val tree = JSONTree(generatorConfigRoot)

        noiseRes = tryValue(2) { tree["noise_resolution"].elem.asInt }
        noiseRes = rectifyIntGE1(noiseRes)
        logln("Parsed input noise resolution: $noiseRes")

        noiseChannelCount = tryValue(32) { tree["noise_channel_count"].elem.asInt }
        noiseChannelCount = rectifyIntGE1(noiseChannelCount)
        logln("Parsed input noise channel count: $noiseChannelCount")

        imageRes = tryValue(64) { tree["image_resolution"].elem.asInt }
        imageRes = rectifyIntGE1(imageRes)
        logln("Parsed output image resolution: $imageRes")

        imageChannelCount = tryValue(3) { tree["image_channel_count"].elem.asInt }
        imageChannelCount = rectifyIntGE1(imageChannelCount)
        logln("Parsed output image channel count: $imageChannelCount")

        logln("Completed parsing generator configuration")
    } // end fun

    /** Random seed to use. */
    private var seed by Delegates.notNull<Int>()

    /** Kotlin random number generator to use. */
    private lateinit var random: Random

    /** Sets-up random. */
    private fun setupRand() {
        logln("Started setting-up random")

        val mode = if (autoSeedEnabled) {
            "Auto"
        } else {
            "Manual"
        } // end val

        if (autoSeedEnabled) {
            val currTimeMS = System.currentTimeMillis()
            val currTimeRandom = Random(currTimeMS)
            seed = currTimeRandom.nextInt()
        } else {
            seed = manualSeed.toInt()
        } // end if

        logln("Seed ($mode): $seed")

        random = Random(seed)
        logln("Done setting Kotlin random number generator")
        DJLDefaults.engine.setRandomSeed(seed)
        logln("Done setting DJL random number generator")

        logln("Completed setting-up random")
    } // end fun

    /** Sets-up hardware. */
    private fun setupHW() {
        logln("Started setting-up hardware")

        DJLDefaults.updateByGPUCount(gpuCount)
        logln("Done setting DJL default device: ${DJLDefaults.device}")

        logln("Completed setting-up hardware")
    } // end fun

    /** Input noise dimension sizes. */
    private lateinit var noiseDimSizes: Array<out Long>

    /** Output image dimension sizes. */
    private lateinit var imageDimSizes: Array<out Long>

    /** Sets-up the input/output dimension sizes. */
    private fun setupDimSizes() {
        noiseDimSizes = arrayOf(noiseChannelCount.toLong(), noiseRes.toLong(), noiseRes.toLong())
        imageDimSizes = arrayOf(imageChannelCount.toLong(), imageRes.toLong(), imageRes.toLong())
        logln("Done setting-up input/output dimension sizes")
    } // end fun

    /** Input noise batches. */
    private lateinit var noiseBatches: ArrayList<NDList>

    /** Batch count. */
    private val batchCount: Int
        get() {
            // Initialize batchCount
            val imageCount = imageCount.toFloat()
            val imagesPerBatch = imagesPerBatch.toFloat()
            val batchCountFloat = imageCount / imagesPerBatch
            val result = ceil(batchCountFloat).toInt()
            return result
        } // end get

    /** Sets-up input noise batches. */
    private fun setupNoiseBatches() {
        logln("Started setting-up input noise batches")

        var imageCountRemain = imageCount
        noiseBatches = arrayListOf()
        var batchIdx = 0

        while (imageCountRemain > 0) {
            val batchImageCount = if (imageCountRemain > imagesPerBatch) {
                imagesPerBatch
            } else {
                imageCountRemain
            } // end if

            imageCountRemain -= batchImageCount
            val batch = ModelOps.genRandNoiseBatch(batchImageCount, noiseDimSizes)
            noiseBatches.add(batch)
            val needsLog = (batchIdx == 0) or ((batchIdx + 1).mod(15) == 0) or (batchIdx == batchCount - 1)

            if (needsLog) {
                logln("Done setting-up noise batch: ${batchIdx + 1} / $batchCount")
            } // end if

            batchIdx += 1
        } // end while

        logln("Completed setting-up input noise batches")
    } // end fun

    /** Internal field of whether the generation process is completed. */
    private var completedField = false

    /** Whether the generation process is completed. */
    val completed: Boolean
        get() {
            val result = completedField
            return result
        } // end get

    /** Start time. */
    private var startTime = Instant.now()

    /** Prepares for the generation. */
    fun prep() {
        logln(
            """
                Started preparation
                App data path: $appDataPath
                Exportation path: $exportPath
            """.trimIndent()
        ) // end logln

        completedField = false
        startTime = Instant.now()
        val genResultsFile = File(genResultsPath)
        genResultsFile.mkdirs()
        logln("Ensured ${Defaults.genResultsName} folder in exportation path")

        genConfigRoot = Utils.loadJson(genConfigLoc)
        logln("Loaded ${Defaults.djlGenProcsDemoName} from app data")

        generatorConfigRoot = Utils.loadJson(generatorConfigLoc)
        logln("Loaded ${Defaults.generatorConfigName} from exportation path")

        parseGenConfig()
        parseGeneratorConfig()

        setupRand()
        setupHW()
        setupDimSizes()
        setupNoiseBatches()

        logln("Completed preparation")
    } // end fun

    /** Generator TorchScript location. */
    private val generatorTSLoc =
        Utils.joinPaths(exportPath, Defaults.modelSavesName, Defaults.generatorStateScriptName)
    // end val

    /** Output image batches. */
    private lateinit var imageBatches: ArrayList<NDList>

    /** Images. */
    private lateinit var images: ArrayList<NDArray>

    /** Generate images. */
    private fun genImages() {
        logln("Started generating images")

        val model = ModelIO.loadTSModel(generatorTSLoc)
        logln("Loaded model")
        imageBatches = arrayListOf()
        var batchIdx = 0

        for (noiseBatch in noiseBatches) {
            val imageBatch = ModelOps.genImageBatch(model, noiseBatch, noiseDimSizes, imageDimSizes)
            imageBatches.add(imageBatch)
            val needsLog = (batchIdx == 0) or ((batchIdx + 1).mod(15) == 0) or (batchIdx == batchCount - 1)

            if (needsLog) {
                logln("Done generating image batch ${batchIdx + 1} / $batchCount")
            } // end if

            batchIdx += 1
        } // end for

        model.close()
        logln("Completed generating images")
    } // end fun

    /** Converts the batches to images.
     *
     * Note: After the conversion is done, the batches will be cleared.
     */
    private fun batchesToImages() {
        images = arrayListOf()

        for (imageBatch in imageBatches) {
            for (imageNDArray in imageBatch) {
                images.add(imageNDArray)
            } // end for
        } // end for

        imageBatches.clear()
        logln("Done converting batches to images")
    } // end fun

    /** Grids. */
    private lateinit var grids: ArrayList<NDArray>

    /** Grid count. */
    private val gridCount: Int
        get() {
            // Initialize gridCount
            val imageCountFloat = imageCount.toFloat()
            val imagesPerGridFloat = gridModeImagesPerGrid.toFloat()
            val gridCountFloat = imageCountFloat / imagesPerGridFloat
            val result = ceil(gridCountFloat).toInt()
            return result
        } // end get

    /** Converts some images to a grid.
     * @param images: some images
     * @param imagesPerGrid: image count per grid
     * @param gridPadding: a padding
     * @return result: the result
     */
    private fun imagesToGrid(images: ArrayList<NDArray>, imagesPerGrid: Int, gridPadding: Int): NDArray {
        val imagesPerGridFloat = imagesPerGrid.toFloat()
        val xImageCount = ceil(imagesPerGridFloat.pow(0.5f)).toInt()
        val yImageCount = xImageCount

        val gridRes = gridPadding + xImageCount * (imageRes + gridPadding)
        val gridShape = Shape(imageChannelCount.toLong(), gridRes.toLong(), gridRes.toLong())
        val gridPaddingValue = -0.65f

        val imageCount = images.size
        var imageIdx = 0
        val result = DJLDefaults.cpuNDManager.full(gridShape, gridPaddingValue, DJLDefaults.dataType)

        for (yImageIdx in 0 until yImageCount) {
            for (xImageIdx in 0 until xImageCount) {
                if (imageIdx < imageCount) {
                    val xStart = gridPadding + xImageIdx * (imageRes + gridPadding)
                    val xEnd = xStart + imageRes

                    val yStart = gridPadding + yImageIdx * (imageRes + gridPadding)
                    val yEnd = yStart + imageRes

                    // Paste the image into the grid in the axis order CHW (color, height, width)
                    val imageGridIndices = NDIndex(":, $yStart:$yEnd, $xStart:$xEnd")
                    val image = images[imageIdx]
                    result.set(imageGridIndices, image.duplicate())

                    imageIdx += 1
                } // end if
            } // end for
        } // end for

        return result
    } // end fun

    /** Converts the images to grids. */
    private fun imagesToGrids() {
        grids = arrayListOf()

        for (gridIdx in 0 until gridCount) {
            val startImageIdx = gridIdx * gridModeImagesPerGrid
            var endImageIdx = (gridIdx + 1) * gridModeImagesPerGrid

            if (endImageIdx >= imageCount) {
                endImageIdx = imageCount
            } // end if

            val imageIdxRange = startImageIdx until endImageIdx
            val imagesSlice = images.slice(imageIdxRange)
            val imagesSliceArrayList = ArrayList(imagesSlice)

            val grid = imagesToGrid(imagesSliceArrayList, gridModeImagesPerGrid, gridModePadding)
            grids.add(grid)
        } // end for

        logln("Done converting images to grids")
    } // end fun

    /** Normalizes an image or grid in-place.
     * @param imageOrGrid: an image or grid
     * @return result: the result
     */
    private fun normalize(imageOrGrid: NDArray): NDArray {
        val mean = -0.75f
        val std = 1.5f

        // normed = (orig - mean) / std
        var result = imageOrGrid
        result = result.sub(mean)
        result = result.div(std)
        result = result.clip(0f, 1f)
        return result
    } // end fun

    /** Converts an ND array to a buffered image.
     * @param ndArray: an ND array
     * @return result: the result
     */
    private fun toImage(ndArray: NDArray): BufferedImage {
        val shape = ndArray.shape
        val dimension = shape.dimension()

        if (dimension != 3) {
            throw UnsupportedOperationException(
                "Unsupported image channel count: $dimension (Supported: 3)"
            ) // end throw
        } // end if

        var converted = ndArray
        converted = converted.mul(255)
        converted = converted.clip(0f, 255f)

        // Assume that the ndArray image has the axis order CHW (color, height, width)
        val width = shape[2].toInt()
        val height = shape[1].toInt()
        val pixelCount = width * height
        val pixels = IntArray(pixelCount)

        val raw = converted.toType(DataType.UINT8, false).toUint8Array()
        val rawRedOffset = 0
        val rawGreenOffset = pixelCount
        val rawBlueOffset = 2 * pixelCount

        for (xIdx in 0 until width) {
            for (yIdx in 0 until height) {
                val idx = yIdx * width + xIdx

                val red = raw[rawRedOffset + idx]
                val green = raw[rawGreenOffset + idx]
                val blue = raw[rawBlueOffset + idx]

                val redShifted = red shl 16
                val greenShifted = green shl 8
                val blueShifted = blue shl 0

                val pixel = redShifted or greenShifted or blueShifted
                pixels[idx] = pixel
            } // end for
        } // end for

        val result = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        result.setRGB(0, 0, width, height, pixels, 0, width)
        return result
    } // end fun

    /** Saves an ND array as an image file.
     * @param prefix: A file name prefix. For example: Image, Grid.
     * @param idx: an image or grid index
     * @param ndArray: an ND array
     */
    private fun saveAsImage(prefix: String, idx: Int, ndArray: NDArray) {
        val normalized = normalize(ndArray)
        val image = toImage(normalized)

        val now = LocalDateTime.now()
        val timestamp = Utils.timestampStringOf(now)

        val name = "$prefix-${idx + 1}-Time-$timestamp.jpg"
        val loc = Utils.joinPaths(genResultsPath, name)
        val file = File(loc)

        val jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next()
        val jpgWriteParam = jpgWriter.defaultWriteParam

        jpgWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        val imageQualityFloat = imageQuality.toFloat()
        val fractionalQuality = imageQualityFloat / 100f
        jpgWriteParam.compressionQuality = fractionalQuality

        val fileStream = file.outputStream()
        val imageStream = MemoryCacheImageOutputStream(fileStream)
        jpgWriter.output = imageStream

        val iioImage = IIOImage(image, null, null)
        jpgWriter.write(null, iioImage, jpgWriteParam)

        fileStream.close()
        jpgWriter.dispose()
    } // end fun

    /** Saves the grids. */
    private fun saveGrids() {
        var idx = 0

        for (gridNDArray in grids) {
            saveAsImage("Grid", idx, gridNDArray)
            idx += 1
        } // end for

        logln("Done saving grids")
    } // end fun

    /** Saves the images. */
    private fun saveImages() {
        var idx = 0

        for (imageNDArray in images) {
            saveAsImage("Image", idx, imageNDArray)
            idx += 1
        } // end for

        logln("Done saving images")
    } // end fun

    /** End time */
    private var endTime = Instant.now()

    /** Execution time. */
    val exeTime: Duration
        get() {
            val result = Duration.between(startTime, endTime)
            return result
        } // end get

    /** Starts the generation. */
    fun start() {
        logstr("")
        logln("Started generation")
        genImages()

        if (gridModeEnabled) {
            batchesToImages()
            imagesToGrids()
            saveGrids()
        } else {
            batchesToImages()
            saveImages()
        } // end if

        endTime = Instant.now()
        completedField = true
        logln("Completed generation")
    } // end fun
} // end class
