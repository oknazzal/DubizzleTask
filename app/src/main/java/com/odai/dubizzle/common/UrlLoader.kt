package com.odai.dubizzle.common

import androidx.annotation.NonNull
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class UrlLoader private constructor(
    urlLoader: ModelLoader<GlideUrl, InputStream>
) : BaseGlideUrlLoader<String>(urlLoader) {

    class Factory : ModelLoaderFactory<String, InputStream> {

        @NonNull
        override fun build(
            multiFactory: MultiModelLoaderFactory
        ): ModelLoader<String, InputStream> {
            return UrlLoader(
                multiFactory.build(GlideUrl::class.java, InputStream::class.java)
            )
        }

        override fun teardown() {}
    }

    override fun handles(@NonNull model: String): Boolean {
        return !model.startsWith("http")
    }

    override fun getUrl(model: String?, width: Int, height: Int, options: Options): String {
        return BASE_IMAGES_URL + model
    }

    companion object {

        private const val BASE_IMAGES_URL = "https://image.tmdb.org/t/p/w500/"
    }
}
