package io.hsar.mealplanner.cli

import com.beust.jcommander.Parameter
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.MealPlanner
import io.hsar.mealplanner.storage.JsonStorage
import java.nio.file.Path

abstract class Command(val name: String) {
    abstract fun run()
}

class CommandLineInterface : Command("generateMealPlan") {

    @Parameter(
        names = ["--days"],
        description = "An integer number of days to generate a meal plan for",
        required = true,
    )
    private lateinit var daysInput: String

    override fun run() {
        val storage = JsonStorage(DEFAULT_FILE_PATH)

        val days = Integer.parseInt(daysInput)
        MealPlanner(storage)
            .generate(days)
    }
}

val OBJECT_MAPPER = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .enable(JsonParser.Feature.ALLOW_COMMENTS)
    .enable(SerializationFeature.INDENT_OUTPUT)!!
    .setSerializationInclusion(NON_NULL)
    .registerModule(JavaTimeModule())

val DEFAULT_FILE_PATH = Path.of(".", "previousMeals.json").toFile()