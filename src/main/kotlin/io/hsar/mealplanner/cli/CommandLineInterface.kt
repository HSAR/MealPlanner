package io.hsar.mealplanner.cli

import com.beust.jcommander.Parameter
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.MealPlanner
import io.hsar.mealplanner.storage.MealOptionStorage
import io.hsar.mealplanner.storage.PreviousMealStorage
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
        val mealOptions = MealOptionStorage(MEAL_OPTIONS_PATH)
        val previousMeals = PreviousMealStorage(PREVIOUS_MEALS_PATH)

        val days = Integer.parseInt(daysInput)
        MealPlanner(previousMeals, mealOptions)
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

val MEAL_OPTIONS_PATH = Path.of(".", "mealOptions.json").toFile()
val PREVIOUS_MEALS_PATH = Path.of(".", "previousMeals.json").toFile()