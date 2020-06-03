package `in`.ponshere.ytdemoapp.utils

import org.junit.Assert
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

fun assertTrue(value: Boolean?) {
    if (value == null) {
        throw AssertionError()
    } else {
        Assert.assertTrue(value)
    }
}

/**
 * Returns Mockito.eq() as nullable type to avoid java.lang.IllegalStateException when
 * null is returned.
 *
 * Generic T is nullable because implicitly bounded by Any?.
 */
fun <T> eq(obj: T): T = Mockito.eq<T>(obj)

/**
 * Returns Mockito.any() as nullable type to avoid java.lang.IllegalStateException when
 * null is returned.
 */
fun <T> any(): T = Mockito.any<T>()

/**
 * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
 * when null is returned.
 */
fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()