package com.agrisafe.app.viewmodel

import androidx.lifecycle.ViewModel

/**
 * Shared ViewModel scoped to the Activity.
 * Holds all state that needs to persist across fragments during a single test session.
 * A new test session starts when the user returns to Home and begins again.
 */
class TestViewModel : ViewModel() {

    // The test type selected by the user on Screen 2
    var selectedTestType: String = ""

    // The random value generated ONCE when entering the Progress screen (Screen 3).
    // -1 means not yet generated.
    private var _randomValue: Int = -1

    val randomValue: Int
        get() = _randomValue

    /**
     * Generates the random value for this test session.
     * This must be called only once per test. If already generated, does nothing.
     */
    fun generateRandomValue() {
        if (_randomValue == -1) {
            _randomValue = (0..150).random()
        }
    }

    /**
     * Determines if the result is SAFE (random value <= 75).
     */
    fun isSafe(): Boolean = _randomValue <= 75

    /**
     * Resets the session so a completely new test can begin.
     * Called when the user navigates back to Home.
     */
    fun resetSession() {
        selectedTestType = ""
        _randomValue = -1
    }
}
