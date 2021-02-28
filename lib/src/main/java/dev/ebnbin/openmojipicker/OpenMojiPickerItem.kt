package dev.ebnbin.openmojipicker

internal data class OpenMojiPickerItem(
    val viewType: ViewType,
    val group: Group? = null,
    val subgroup: Subgroup? = null,
    val openMoji: OpenMoji? = null,
) {
    data class Group(
        val openMojiGroup: OpenMojiGroup,
        val indexRange: IntRange,
    )

    data class Subgroup(
        val openMojiSubgroup: OpenMojiSubgroup,
        val indexRange: IntRange,
    )

    enum class ViewType {
        GROUP,
        SUBGROUP,
        OPENMOJI;

        companion object {
            fun of(viewType: Int): ViewType {
                return values()[viewType]
            }
        }
    }
}
