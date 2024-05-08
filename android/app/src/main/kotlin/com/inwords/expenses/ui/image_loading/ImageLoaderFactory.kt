package com.inwords.expenses.ui.image_loading

import android.content.Context
import coil.ImageLoader
import okhttp3.OkHttpClient

class ImageLoaderFactory {

    fun createImageLoader(context: Context, okHttpClient: Lazy<OkHttpClient>): ImageLoader {
        return ImageLoader.Builder(context)
            // Create the OkHttpClient inside a lambda so it will be initialized lazily on a background thread.
            .okHttpClient { okHttpClient.value }
            .build()
    }
}