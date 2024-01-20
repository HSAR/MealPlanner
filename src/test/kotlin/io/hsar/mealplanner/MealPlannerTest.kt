package io.hsar.mealplanner

import io.hsar.mealplanner.MealPlannerTest.Companion.TEST_DATE2
import io.hsar.mealplanner.MealPlannerTest.Companion.TEST_DATE3
import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.Servings
import io.hsar.mealplanner.storage.Storage
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MealPlannerTest {
    @Test
    fun `works as expected`() {
        // Arrange
        val objectUnderTest = MealPlanner(TestPreviousMealStorage(), TestMealOptionStorage())

        // Act
        val result = objectUnderTest.generate(TEST_PERIOD, TEST_HOUSEHOLD_SIZE, TEST_DATE1)

        // Assert
        assertThat(result, equalTo(EXPECTED_RESULT))
    }

    companion object {
        val TEST_PERIOD = 4
        val TEST_HOUSEHOLD_SIZE = 2
        val TEST_DATE1 = LocalDate.of(2023, 6, 25)
        val TEST_DATE2 = TEST_DATE1.minusDays(1)
        val TEST_DATE3 = TEST_DATE1.minusDays(2)

        val TEST_MEAL1 = Meal(
            name = "Sausage and Leek Pasta",
            servings = Servings(min = 2, max = 6),
            tags = setOf("pasta", "leek", "sausage")
        )

        val TEST_MEAL2 = Meal(
            name = "Steak",
            servings = Servings(min = 1, max = 4),
            tags = setOf("steak")
        )

        val EXPECTED_RESULT = listOf(
            TEST_MEAL1 to 6,
            TEST_MEAL2 to 2
        )


    }
}

class TestPreviousMealStorage : Storage<Map<String, LocalDate>> {
    override fun read(): Map<String, LocalDate> = mapOf(
        "Sausage and Leek Pasta" to TEST_DATE3,
        "Steak" to TEST_DATE2
    )

    override fun write(input: Map<String, LocalDate>) = TODO("Don't use this")
}

class TestMealOptionStorage : Storage<Set<Meal>> {
    override fun read(): Set<Meal> = setOf(MealPlannerTest.TEST_MEAL1, MealPlannerTest.TEST_MEAL2)

    override fun write(input: Set<Meal>) = TODO("Don't use this")
}