package io.hsar.mealplanner.storage

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.PastMeal
import io.hsar.mealplanner.data.PossibleMeal
import io.hsar.mealplanner.data.PossibleServings
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.time.LocalDate


class StorageTest {

    @Test
    fun `reads as expected`() {
        // Arrange
        val objectUnderTest = PastMealStorage(filePath = TEST_FILE)

        // Act
        val result = objectUnderTest.read()

        // Assert
        assertThat(result, equalTo(TEST_STATE))
    }

    @Test
    fun `writes as expected`() {
        // Arrange
        val objectUnderTest = PastMealStorage(filePath = TEMP_FILE, mapper = TEST_MAPPER)

        // Act
        objectUnderTest.write(TEST_STATE)

        // Assert
        val resultString = TEMP_FILE.readText()
        assertThat(resultString, equalTo(EXPECTED_RESULT))

        println(TEMP_FILE)
    }

    companion object {
        val TEST_MAPPER = jacksonObjectMapper() // does not indent output
            .registerModule(JavaTimeModule())

        val TEMP_FILE = Files.createTempFile("testfile", ".json").toFile()

        val TEST_FILE = File("src/test/resources/testStorage.json")

        val TEST_STATE = sortedMapOf(
            LocalDate.of(2023, 6, 23) to PastMeal(
                name = "Sausage and Leek Pasta",
                tags = setOf("pasta", "leek", "sausage"),
                servings = 4
            ),
            LocalDate.of(2023, 6, 24) to PastMeal(
                name = "Steak",
                tags = setOf("steak"),
                servings = 2
            )
        )


        val EXPECTED_RESULT =
            "{\"2023-06-23\":{\"name\":\"Sausage and Leek Pasta\",\"tags\":[\"pasta\",\"leek\",\"sausage\"],\"servings\":4,\"description\":null},\"2023-06-24\":{\"name\":\"Steak\",\"tags\":[\"steak\"],\"servings\":2,\"description\":null}}"
    }
}