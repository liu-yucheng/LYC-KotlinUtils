// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.djlutils

import ai.djl.Model
import kotlin.io.path.Path

/** Model input/output. */
class ModelIO private constructor() {
    companion object {
        /** Loads a TorchScript model.
         * @param fromLoc: a location
         * @return result: the resulting model
         */
        fun loadTSModel(fromLoc: String): Model {
            val name = "TorchScript model at $fromLoc"
            val device = DJLDefaults.device
            val engineName = DJLDefaults.engine.engineName
            val result = Model.newInstance(name, device, engineName)
            val path = Path(fromLoc)
            result.load(path)
            return result
        } // end fun
    } // end companion
} // end class
