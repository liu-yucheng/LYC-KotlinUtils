// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

package lyc.ktutils.libs.gputils.jsonio

import com.google.gson.JsonElement

/** JSON tree.
 * @param elem: a root element
 */
class JSONTree(val elem: JsonElement) {
    // Part of LYC-KotlinUtils
    // Copyright 2022 Yucheng Liu. Apache License Version 2.0.
    // Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

    /** Gets the child tree with the [key].
     * @param key: a key
     * @return result: the result
     */
    operator fun get(key: String): JSONTree {
        val childElem = elem.asJsonObject[key]
        val result = JSONTree(childElem)
        return result
    } // end fun

    /** Gets the innermost child tree with the [keys].
     * @param keys: some keys
     * @return result: the result
     */
    operator fun get(vararg keys: String): JSONTree {
        var childElem = elem

        for (key in keys) {
            try {
                childElem = childElem.asJsonObject[key]
            } catch (exc: java.lang.NullPointerException) {
                throw NullPointerException(
                    """
JSONTree.get::childElem cannot be null
keys: ${keys.asList()}
key: $key
${exc.message}
                    """.trim()
                ) // end throw
            } // end if
        } // end for

        val result = JSONTree(childElem)
        return result
    } // end fun

    /** Sets the child tree with the [key] to the [value].
     * @param key: a key
     * @param value: a value
     */
    operator fun set(key: String, value: JSONTree) {
        elem.asJsonObject.add(key, value.elem)
    } // end fun

    /** Sets the innermost child tree with the [keys] to the [value].
     * @param keys: some keys
     * @param value: a value
     */
    operator fun set(vararg keys: String, value: JSONTree) {
        var childElem = elem

        for (key in keys.slice(0..keys.size - 2)) {
            childElem = childElem.asJsonObject[key]
        } // end for

        val lastKey = keys[keys.size - 1]
        childElem.asJsonObject.add(lastKey, value.elem)
    } // end fun
} // end class
