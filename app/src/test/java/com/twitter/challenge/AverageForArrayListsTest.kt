package com.twitter.challenge

import com.twitter.challenge.utilities.Average.avg
import com.twitter.challenge.utilities.TemperatureConverter.celsiusToFahrenheit
import junit.framework.Assert.assertTrue
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class AverageForArrayListsTest {
    @Test
    fun testAverage() {
//        val precision = Java6Assertions.within(0.01f)
        assertTrue(avg(arrayListOf(1f, 2f, 3f)).equals(2f))
        assertTrue(avg(arrayListOf(0f, 0f, 0f)).equals(0f))
        assertTrue(avg(arrayListOf(-1f, -2f, -3f)).equals(-2f))
    }
}