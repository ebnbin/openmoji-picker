package dev.ebnbin.openmojipicker

internal data class OpenMojiPickerItem(
    val viewType: ViewType,
    val group: Group? = null,
    val openMoji: OpenMoji? = null,
) {
    data class Group(
        val openMojiGroup: OpenMojiGroup,
        val indexRange: IntRange,
    )

    enum class ViewType {
        GROUP,
        OPENMOJI;

        companion object {
            fun of(viewType: Int): ViewType {
                return values()[viewType]
            }
        }
    }
}
