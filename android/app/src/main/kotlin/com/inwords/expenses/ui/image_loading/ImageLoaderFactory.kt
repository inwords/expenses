package com.inwords.expenses.ui.image_loading

import android.content.Context
import coil3.ImageLoader

class ImageLoaderFactory {

    fun createImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            // TODO set up ktor here
            .build()
    }
}