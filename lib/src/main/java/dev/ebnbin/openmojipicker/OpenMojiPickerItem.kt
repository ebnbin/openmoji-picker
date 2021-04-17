package dev.ebnbin.openmojipicker

internal data class OpenMojiPickerItem(
    val viewType: ViewType,
    val group: OpenMojiGroup? = null,
    val openMoji: OpenMoji? = null,
) {
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
