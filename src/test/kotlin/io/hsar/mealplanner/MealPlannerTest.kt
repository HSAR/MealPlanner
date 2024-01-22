package io.hsar.mealplanner

import io.hsar.mealplanner.data.PastMeal
import io.hsar.mealplanner.data.PossibleMeal
import io.hsar.mealplanner.data.PossibleServings
import io.hsar.mealplanner.storage.Storage
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

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

        val POSSIBLE_MEAL_1 = PossibleMeal(
            name = "Sausage and Leek Pasta",
            possibleServings = PossibleServings(min = 2, max = 6),
            tags = setOf("pasta", "leek", "sausage")
        )

        val POSSIBLE_MEAL_2 = PossibleMeal(
            name = "Steak",
            possibleServings = PossibleServings(min = 1, max = 4),
            tags = setOf("steak")
        )

        val EXPECTED_RESULT = listOf(
            POSSIBLE_MEAL_1.toPastMeal(6),
            POSSIBLE_MEAL_2.toPastMeal(2)
        )
    }
}

class TestPreviousMealStorage : Storage<SortedMap<LocalDate, PastMeal>> {
    override fun read(): SortedMap<LocalDate, PastMeal> = sortedMapOf(
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

    override fun write(input: SortedMap<LocalDate, PastMeal>) = TODO("Don't use this")
}

class TestMealOptionStorage : Storage<Set<PossibleMeal>> {
    override fun read(): Set<PossibleMeal> = setOf(MealPlannerTest.POSSIBLE_MEAL_1, MealPlannerTest.POSSIBLE_MEAL_2)

    override fun write(input: Set<PossibleMeal>) = TODO("Don't use this")
}