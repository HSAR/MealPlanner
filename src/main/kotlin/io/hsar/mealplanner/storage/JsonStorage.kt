package io.hsar.mealplanner.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.mealplanner.cli.OBJECT_MAPPER
import io.hsar.mealplanner.data.PreviousMeals
import java.io.File

/**
 * Stores previous meals in a machine-readable (but human-friendly) map format.
 * Fully responsible for serialisation and deserialisation.
 */
class JsonStorage(val filePath: File, val initialState: PreviousMeals = PreviousMeals(emptyMap()), private val mapper: ObjectMapper = OBJECT_MAPPER) {

    fun read(): PreviousMeals = mapper.readValue(filePath)

    fun write(input: PreviousMeals) = mapper.writeValue(filePath, input)
}