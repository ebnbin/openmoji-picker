package dev.ebnbin.openmojipicker

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Keep
@Parcelize
data class OpenMoji(
    val emoji: String,
    val hexcode: String,
    val group: String,
    val subgroups: String,
    val annotation: String,
) : Parcelable {
    @IgnoredOnParcel
    private var drawableIdCache: Int? = null

    val drawableId: Int
        get() = drawableIdCache ?: kotlin.runCatching {
            val name = "openmoji_${hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}"
            R.drawable::class.java.getField(name).getInt(null)
        }.getOrDefault(0).also {
            drawableIdCache = it
        }

    @IgnoredOnParcel
    private var iconDrawableIdCache: Int? = null

    val iconDrawableId: Int
        get() = iconDrawableIdCache ?: kotlin.runCatching {
            val name = "openmoji_icon_${hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}"
            R.drawable::class.java.getField(name).getInt(null)
        }.getOrDefault(0).also {
            iconDrawableIdCache = it
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenMoji
        if (hexcode != other.hexcode) return false
        return true
    }

    override fun hashCode(): Int {
        return hexcode.hashCode()
    }
}
