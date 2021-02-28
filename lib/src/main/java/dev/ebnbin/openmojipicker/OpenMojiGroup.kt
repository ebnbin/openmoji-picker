package dev.ebnbin.openmojipicker

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OpenMojiGroup(
    val group: String,
    val subgroupCount: Int,
    val openMojiCount: Int,
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenMojiGroup
        if (group != other.group) return false
        return true
    }

    override fun hashCode(): Int {
        return group.hashCode()
    }
}
