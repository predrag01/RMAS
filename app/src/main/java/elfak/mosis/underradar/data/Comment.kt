package elfak.mosis.underradar.data

import java.util.UUID

data class Comment(
    var id: String= UUID.randomUUID().toString(),
    var comment: String="",
    var CommenterId: String=""
)
