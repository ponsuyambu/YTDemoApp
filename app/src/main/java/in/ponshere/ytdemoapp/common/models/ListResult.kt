package `in`.ponshere.ytdemoapp.common.models

interface ListResult<T> {
    val listModels: List<T>?
    val nextPageToken: String
}