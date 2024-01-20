package io.hsar.mealplanner.storage

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.PreviousMeals
import io.hsar.mealplanner.data.Servings
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.time.LocalDate


class JsonStorageTest {

    @Test
    fun `reads as expected`() {
        // Arrange
        val objectUnderTest = PreviousMealStorage(filePath = TEST_FILE)

        // Act
        val result = objectUnderTest.read()

        // Assert
        assertThat(result, equalTo(TEST_STATE))
    }

    @Test
    fun `writes as expected`() {
        // Arrange
        val objectUnderTest = PreviousMealStorage(filePath = TEMP_FILE, mapper = TEST_MAPPER)

        // Act
        objectUnderTest.write(TEST_STATE)

        // Assert
        val resultString = TEMP_FILE.readText()
        assertThat(resultString, equalTo(EXPECTED_RESULT))

        println(TEMP_FILE)
    }

    companion object {
        val TEST_MAPPER = jacksonObjectMapper() // does not indent output

        val TEMP_FILE = Files.createTempFile("testfile", ".json").toFile()

        val TEST_FILE = File("src/test/resources/testStorage.json")

        val TEST_STATE = PreviousMeals(
            mapOf(
                LocalDate.of(2023, 6, 23) to Meal(
                    name = "Sausage and Leek Pasta",
                    servings = Servings(min = 2, max = 6),
                    tags = setOf("pasta", "leek", "sausage")
                ),
                LocalDate.of(2023, 6, 24) to Meal(
                    name = "Steak",
                    servings = Servings(min = 1, max = 4),
                    tags = setOf("steak")
                )
            )
        )

        val EXPECTED_RESULT =
            "{\"data\":{\"2023-06-23\":{\"name\":\"Sausage and Leek Pasta\",\"tags\":[\"pasta\",\"leek\",\"sausage\"],\"servings\":{\"min\":2,\"max\":6},\"description\":null},\"2023-06-24\":{\"name\":\"Steak\",\"tags\":[\"steak\"],\"servings\":{\"min\":1,\"max\":4},\"description\":null}}}"
    }
}