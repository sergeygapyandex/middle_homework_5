package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class FindUntranslatedStringsTask : DefaultTask() {
    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, "src/main/res")
        val stringsFile = File(resDir, "values/strings.xml")

        if (!stringsFile.exists()) return

        val defaultStrings = parseStringResources(stringsFile)
        val valuesDirs = resDir.listFiles { file ->
            file.isDirectory && file.name.startsWith("values-") && isLanguageLocale(file.name)
        } ?: emptyArray()

        val missingStrings = mutableMapOf<String, List<String>>()

        valuesDirs.forEach { valuesDir ->
            val localeStringsFile = File(valuesDir, "strings.xml")
            if (localeStringsFile.exists()) {
                val localeStrings = parseStringResources(localeStringsFile)
                val missing = defaultStrings - localeStrings.toSet()
                if (missing.isNotEmpty()) {
                    missingStrings[valuesDir.name] = missing
                }
            } else {
                missingStrings[valuesDir.name] = defaultStrings
            }
        }

        if (missingStrings.isNotEmpty()) {
            val stringBuilderErrorText =
                StringBuilder("Missing translations").append(System.lineSeparator())
            missingStrings.forEach { missing ->
                stringBuilderErrorText
                    .append("=== ${missing.key} ===")
                    .append(System.lineSeparator())
                    .append(missing.value.joinToString(separator = System.lineSeparator()))
                    .append(System.lineSeparator())
            }
            throw GradleException(stringBuilderErrorText.toString())
        }
    }

    private fun isLanguageLocale(dirName: String): Boolean {
        // Проверяем формат values-xx или values-xx-rYY, где xx - код языка (2 буквы), YY - код региона (2 буквы)
        // Исключаем каталоги типа values-night, values-sw600dp и т.д.
        val localePattern = Regex("^values-[a-z]{2}(-r[A-Z]{2})?$")
        return localePattern.matches(dirName)
    }

    private fun parseStringResources(file: File): List<String> {
        val stringsFromXml = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
            .getElementsByTagName("string")

        return stringsFromXml.let { nodeList ->
            (0 until nodeList.length).map { i ->
                val node = nodeList.item(i)
                val name = node.attributes?.getNamedItem("name")?.nodeValue ?: ""
                name
            }
        }
    }
}
