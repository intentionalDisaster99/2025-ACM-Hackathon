package com.hacksolotls.tracker

import com.josiwhitlock.estresso.Ester
import com.josiwhitlock.estresso.Estresso.e2multidose3C
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class `Estresso Test` {
    @Test
    fun e2multidose3CTest() {
        val expected = 491 // expected result by estrannai.se

        val result = e2multidose3C(
            t = 20.0,
            doses = listOf(6.0, 6.0, 6.0, 6.0, 6.0),
            times = listOf(0.0, 5.0, 10.0, 15.0, 20.0),
            models = listOf(Ester.ENANTHATE, Ester.ENANTHATE, Ester.ENANTHATE, Ester.ENANTHATE, Ester.ENANTHATE),
        )

        assertEquals(expected, result.toInt())
    }
}