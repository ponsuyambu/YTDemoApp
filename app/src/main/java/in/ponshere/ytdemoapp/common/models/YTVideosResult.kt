package `in`.ponshere.ytdemoapp.common.models

class YTVideosResult(
    override val listModels: List<YTVideo>,
    override val nextPageToken: String
) : ListResult<YTVideo>