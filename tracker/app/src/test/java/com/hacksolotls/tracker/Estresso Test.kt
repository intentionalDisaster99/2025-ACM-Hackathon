package com.hacksolotls.tracker

import com.josiwhitlock.estresso.Ester
import com.josiwhitlock.estresso.Estresso.e2multidose3C
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class `Estresso Test` {
    @Test
    fun e2multidose3CTest() {
        val expected = 61 // expected result by estrannai.se

        val result = e2multidose3C(
            t = 2.0,
            doses = listOf(1.0),
            times = listOf(0.0),
            models = listOf(Ester.VALERATE),
        )

        assertEquals(expected, result.toInt())
    }
}