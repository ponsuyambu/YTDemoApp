package `in`.ponshere.ytdemoapp.utils

import org.junit.Assert

fun assertTrue(value: Boolean?) {
    if (value == null) {
        throw AssertionError()
    }
    Assert.assertTrue(value)
}