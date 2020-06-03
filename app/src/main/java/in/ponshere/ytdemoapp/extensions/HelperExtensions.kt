package `in`.ponshere.ytdemoapp.extensions

import com.google.gson.Gson

val gson = Gson()

fun Any.toJson() : String{
    return gson.toJson(this)
}

fun <T> String.toObject(type: Class<T>) : T {
    return gson.fromJson(this, type)
}