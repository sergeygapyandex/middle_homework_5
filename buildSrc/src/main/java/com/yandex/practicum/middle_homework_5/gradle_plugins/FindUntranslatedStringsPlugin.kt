package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class FindUntranslatedStringsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("untranslatedStrings", FindUntranslatedStringsTask::class.java)
    }
}