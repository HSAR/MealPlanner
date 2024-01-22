package io.hsar.mealplanner.cli

import com.beust.jcommander.Parameter
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.MealPlanner
import io.hsar.mealplanner.storage.PossibleMealStorage
import io.hsar.mealplanner.storage.PastMealStorage
import java.nio.file.Path
import java.time.LocalDate

abstract class Command(val name: String) {
    abstract fun run()
}

class CommandLineInterface : Command("generateMealPlan") {

    @Parameter(
        names = ["--days", "-d"],
        description = "An integer number of days to generate a meal plan for",
        required = true,
    )
    private lateinit var daysInput: String

    @Parameter(
        names = ["--number-of-people", "-n"],
        description = "An integer number of people that need meals per day",
        required = true,
    )
    private lateinit var numPeopleInput: String

    @Parameter(
        names = ["--from-date", "-f"],
        description = "ISO 8601 formatted date for the first day on which a meal is required. Meals generated will be deterministic on this input.",
        required = false,
    )
    private lateinit var fromDateInput: LocalDate

    override fun run() {
        val possibleMeals = PossibleMealStorage(MEAL_OPTIONS_PATH)
        val previousMeals = PastMealStorage(PREVIOUS_MEALS_PATH)
        val fromDate = if (this::fromDateInput.isInitialized) { fromDateInput } else { LocalDate.now() }

        val days = Integer.parseInt(daysInput)
        val numPeople = Integer.parseInt(numPeopleInput)

        val result = MealPlanner(previousMeals, possibleMeals)
            .generate(days, numPeople, fromDate)

        println(result)
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