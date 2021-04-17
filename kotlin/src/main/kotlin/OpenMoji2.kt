data class OpenMoji2(
    val emoji: String,
    val hexcode: String,
    val group: String,
    val subgroups: String,
    val annotation: String,
//    val tags: String,
//    val openmoji_tags: String,
//    val openmoji_author: String,
//    val openmoji_date: String,
//    val skintone: String,
//    val skintone_combination: String,
//    val skintone_base_emoji: String,
//    val skintone_base_hexcode: String,
//    val unicode: String,
//    val order: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OpenMoji2
        if (hexcode != other.hexcode) return false
        return true
    }

    override fun hashCode(): Int {
        return hexcode.hashCode()
    }
}
