package com.discdogs.app.core.navigation.base


open class Destination(
    val host: String, vararg val params: String
) {

    var customPath: String? = null

    val route: String
        get() = customPath ?: buildString {
            append(host)
            params.forEach {
                append("/{$it}")
            }
        }


    fun setPath(vararg path: Any): Destination {

        val builder = StringBuilder(host)
        path.forEach {
            builder.append("/$it")
        }

        this.customPath = builder.toString()
        return this
    }


}
