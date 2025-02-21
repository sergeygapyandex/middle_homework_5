package com.yandex.practicum.middle_homework_5.data.data_store

data class SettingContainer (
    val periodic: Long,
    val delayed: Long
){

    companion object{
        const val DEFAULT_REFRESH_PERIOD: Long = 15
        const val FIST_LAUNCH_DELAY: Long = 10
        val initial:SettingContainer = SettingContainer(
            periodic = DEFAULT_REFRESH_PERIOD,
            delayed = FIST_LAUNCH_DELAY
        )
    }
}