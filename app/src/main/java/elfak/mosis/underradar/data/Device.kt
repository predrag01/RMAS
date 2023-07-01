package elfak.mosis.underradar.data


import java.util.UUID
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Device(
    var id: String= UUID.randomUUID().toString(),
    var title: String="",
    var type: String="",
    var description: String="",
    var ownerId: String="",
    var comments: List<Comment>,
    var date: Timestamp,
    var location: GeoPoint=GeoPoint(.0,.0),
    var like: Int=0,
    var dislike: Int=0,
)
