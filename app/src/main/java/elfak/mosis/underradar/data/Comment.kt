package elfak.mosis.underradar.data

import java.util.UUID

data class Comment(
    var id: String=UUID.randomUUID().toString(),
    var ownerId: String= "",
    var deviceId: String="",
    var username: String="",
    var comment: String="",
)
