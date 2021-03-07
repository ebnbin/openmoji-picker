package dev.ebnbin.openmojipicker

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OpenMoji(
    val emoji: String,
    val hexcode: String,
    val group: String,
    val subgroups: String,
    val annotation: String,
) : Parcelable {
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
