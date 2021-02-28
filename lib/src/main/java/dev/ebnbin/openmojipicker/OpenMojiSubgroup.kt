package dev.ebnbin.openmojipicker

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OpenMojiSubgroup(
    val group: String,
    val subgroup: String,
    val openMojiCount: Int,
    val openMoji: OpenMoji,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenMojiSubgroup
        if (group != other.group) return false
        if (subgroup != other.subgroup) return false
        return true
    }

    override fun hashCode(): Int {
        var result = group.hashCode()
        result = 31 * result + subgroup.hashCode()
        return result
    }
}
