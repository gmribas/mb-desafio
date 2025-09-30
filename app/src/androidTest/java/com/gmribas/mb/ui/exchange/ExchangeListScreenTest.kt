package com.gmribas.mb.ui.exchange

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gmribas.mb.MainActivity
import com.gmribas.mb.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun exchangeListScreen_showsTitle() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("title")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithTag("title")
            .assertIsDisplayed()
    }

    @Test
    fun exchangeListScreen_displaysListOfExchanges_whenDataIsAvailable() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("ExchangeItem")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("ExchangeItem")[0].assertIsDisplayed()
    }

    @Test
    fun exchangeListScreen_clicksFirstItem_opensDetails() {
        // Wait for items to load
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("ExchangeItem")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onAllNodesWithTag("ExchangeItem")[0].performClick()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.exchange_details_title)
        ).assertExists()
    }

    @Test
    fun exchangeListScreen_scrollToLoadMore_ifApplicable() {
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule
                .onAllNodesWithTag("ExchangeItem")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        val initialItemCount = composeTestRule
            .onAllNodesWithTag("ExchangeItem")
            .fetchSemanticsNodes().size

        composeTestRule.onNodeWithTag("LazyColumn").performScrollToIndex(initialItemCount -1) // Scroll to the last known item

        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule
                .onAllNodesWithTag("ExchangeItem")
                .fetchSemanticsNodes().size > initialItemCount
        }

        // Assert that the item count has increased (or new items are visible)
        val newItemCount = composeTestRule
            .onAllNodesWithTag("ExchangeItem")
            .fetchSemanticsNodes().size

        assert(newItemCount > initialItemCount) {
            "Scrolling did not load more items. Initial: $initialItemCount, New: $newItemCount"
        }
    }
}
