// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils.jsonio

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/** JSON tree.
 * @param elem: a root element
 */
class JSONTree(val elem: JsonElement) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Gets the innermost child tree with the [keys].
     * @param keys: some keys
     * @return result: the result
     */
    operator fun get(vararg keys: String): JSONTree {
        var childElem = elem

        for (key in keys) {
            try {
                childElem = childElem.asJsonObject[key]
            } catch (exc: NullPointerException) {
                throw NullPointerException(
                    """
JSONTree.get::childElem cannot be null
keys: ${keys.asList()}
key: $key
${exc.message}
                    """.trim()
                ) // end throw
            } // end try
        } // end for

        val result = JSONTree(childElem)
        return result
    } // end fun

    /** Gets the child tree with the [key].
     * @param key: a key
     * @return result: the result
     */
    operator fun get(key: String): JSONTree {
        val result = get(keys = arrayOf(key))
        return result
    } // end fun

    /** Sets the innermost child tree with the [keys] to the [value].
     * @param keys: some keys
     * @param value: a value
     */
    operator fun set(vararg keys: String, value: JSONTree) {
        var childElem = elem

        for (key in keys.slice(0..keys.size - 2)) {
            try {
                if (!childElem.asJsonObject.has(key)) {
                    val emptyJSONObject = JsonObject()
                    childElem.asJsonObject.add(key, emptyJSONObject)
                } // end if

                childElem = childElem.asJsonObject[key]
            } catch (exc: IllegalStateException) {
                throw IllegalStateException(
                    """
JSONTree.set::childElem must be a JsonObject instance
keys: ${keys.asList()}
key: $key
${exc.message}
                    """.trim()
                ) // end throw
            } // end catch
        } // end for

        val lastKey = keys[keys.size - 1]
        childElem.asJsonObject.add(lastKey, value.elem)
    } // end fun

    /** Sets the child tree with the [key] to the [value].
     * @param key: a key
     * @param value: a value
     */
    operator fun set(key: String, value: JSONTree) {
        set(keys = arrayOf(key), value)
    } // end fun
} // end class
