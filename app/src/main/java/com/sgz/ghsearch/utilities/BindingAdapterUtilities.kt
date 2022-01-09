package com.sgz.ghsearch.utilities

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sgz.ghsearch.R

object BindingAdapterUtilities {
    /**
     * Adapter for loading an image from a url
     */
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView?, imageUrl: String?) {
        view?.let { it ->
            Glide.with(it.context)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.post(Runnable {
                            view
                                .setImageDrawable(
                                    ContextCompat.getDrawable(
                                        view.context,
                                        R.drawable.ic_github_com_logo
                                    )
                                )
                        })
                        view.scaleType = ImageView.ScaleType.FIT_XY
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?, model: Any?,
                        target: Target<Drawable?>,
                        dataSource: DataSource, isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                )
                .into(view)
        }
    }
}