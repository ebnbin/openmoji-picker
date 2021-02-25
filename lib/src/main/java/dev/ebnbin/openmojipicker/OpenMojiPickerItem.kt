package dev.ebnbin.openmojipicker

internal data class OpenMojiPickerItem(
    val viewType: ViewType,
    val group: String? = null,
    val openMoji: OpenMoji? = null,
) {
    enum class ViewType {
        GROUP,
        EMOJI;

        companion object {
            fun of(viewType: Int): ViewType {
                return values()[viewType]
            }
        }
    }
}
