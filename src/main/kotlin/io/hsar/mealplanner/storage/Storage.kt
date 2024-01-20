package io.hsar.mealplanner.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.mealplanner.cli.OBJECT_MAPPER
import io.hsar.mealplanner.data.Meal
import java.io.File
import java.time.LocalDate

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
class PreviousMealStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<Map<String, LocalDate>>(filePath, mapper) {
    override fun read(): Map<String, LocalDate> = mapper.readValue(filePath)
}

/**
 * Stores meals uniquely by name. If there are multiple meals in the input with the same name, one will be chosen.
 */
class MealOptionStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<Set<Meal>>(filePath, mapper) {
    override fun read(): Set<Meal> = mapper.readValue<Map<String, Meal>>(filePath)
        .values.toSet()

    override fun write(input: Set<Meal>) = input.associateBy { it.name }.let { mapper.writeValue(filePath, it) }
}