package dev.ebnbin.openmojipicker

import androidx.annotation.StringRes

enum class OpenMojiGroup(
    val group: String,
    @StringRes
    val stringId: Int,
) {
    RECENT("_recent", R.string.openmoji_picker_group_recent),
    SMILEYS_EMOTION("smileys-emotion", R.string.openmoji_picker_group_smileys_emotion),
    PEOPLE_BODY("people-body", R.string.openmoji_picker_group_people_body),
    ANIMALS_NATURE("animals-nature", R.string.openmoji_picker_group_animals_nature),
    FOOD_DRINK("food-drink", R.string.openmoji_picker_group_food_drink),
    TRAVEL_PLACES("travel-places", R.string.openmoji_picker_group_travel_places),
    ACTIVITIES("activities", R.string.openmoji_picker_group_activities),
    OBJECTS("objects", R.string.openmoji_picker_group_objects),
    SYMBOLS("symbols", R.string.openmoji_picker_group_symbols),
    FLAGS("flags", R.string.openmoji_picker_group_flags);

    companion object {
        fun of(group: String): OpenMojiGroup {
            return values().single { it.group == group }
        }
    }
}
