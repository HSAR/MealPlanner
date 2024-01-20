package io.hsar.mealplanner.storage

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.Servings
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
            .registerModule(JavaTimeModule())

        val TEMP_FILE = Files.createTempFile("testfile", ".json").toFile()

        val TEST_FILE = File("src/test/resources/testStorage.json")

        val TEST_STATE = mapOf(
            "Sausage and Leek Pasta" to LocalDate.of(2023, 6, 23),
            "Steak" to LocalDate.of(2023, 6, 24)
        )

        val EXPECTED_RESULT =
            "{\"Sausage and Leek Pasta\":[2023,6,23],\"Steak\":[2023,6,24]}"
    }
}