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
    val tags: String,
    val openmoji_tags: String,
    val openmoji_author: String,
    val openmoji_date: String,
    val skintone: String,
    val skintone_combination: String,
    val skintone_base_emoji: String,
    val skintone_base_hexcode: String,
    val unicode: String,
    val order: String,
) : Parcelable
