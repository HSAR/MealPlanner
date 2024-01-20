package io.hsar.mealplanner.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.mealplanner.cli.OBJECT_MAPPER
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.PreviousMeals
import java.io.File

/**
 * Stores previous meals in a machine-readable (but human-friendly) map format.
 * Fully responsible for serialisation and deserialisation.
 */
abstract class JsonStorage<T>(val filePath: File, protected val mapper: ObjectMapper = OBJECT_MAPPER) {

    abstract fun read(): T

    open fun write(input: T) = mapper.writeValue(filePath, input)
}

class PreviousMealStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<PreviousMeals>(filePath, mapper) {
    override fun read(): PreviousMeals = mapper.readValue(filePath)
}

/**
 * Stores meals uniquely by name. If there are multiple meals in the input with the same name, one will be chosen.
 */
class MealOptionStorage(filePath: File, mapper: ObjectMapper = OBJECT_MAPPER) : JsonStorage<Set<Meal>>(filePath, mapper) {
    override fun read(): Set<Meal> = mapper.readValue<Map<String, Meal>>(filePath)
        .values.toSet()

    override fun write(input: Set<Meal>) = input.associateBy { it.name }.let { mapper.writeValue(filePath, it) }
}