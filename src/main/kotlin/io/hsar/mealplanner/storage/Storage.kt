package io.hsar.mealplanner.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.mealplanner.cli.OBJECT_MAPPER
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.PastMeal
import io.hsar.mealplanner.data.PossibleMeal
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Stores previous meals in a machine-readable (but human-friendly) map format.
 * Fully responsible for serialisation and deserialisation.
 */
interface Storage<T> {
    fun read(): T

    fun write(input: T)
}

abstract class JsonStorage<T>(val filePath: File, protected val mapper: ObjectMapper = OBJECT_MAPPER): Storage<T> {

    override fun write(input: T) = mapper.writeValue(filePath, input)
}

/**
 * Stores a map of meal names to the date they were last cooked.
 */
class PastMealStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<SortedMap<LocalDate, PastMeal>>(filePath, mapper) {
    override fun read(): SortedMap<LocalDate, PastMeal> = mapper.readValue(filePath)
}

/**
 * Stores meals uniquely by name. If there are multiple meals in the input with the same name, one will be chosen.
 */
class PossibleMealStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<Set<PossibleMeal>>(filePath, mapper) {
    override fun read(): Set<PossibleMeal> = mapper.readValue<Map<String, PossibleMeal>>(filePath)
        .values.toSet()

    override fun write(input: Set<PossibleMeal>) = input.associateBy { it.name }.let { mapper.writeValue(filePath, it) }
}