package elfak.mosis.underradar.data

import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class Device(
    var id: String= UUID.randomUUID().toString(),
    var title: String="",
    var type: String="",
    var description: String="",
    var ownerId: String="",
    var comments: List<Comment>,
    var location: LatLng= LatLng(0.0,0.0),
    var like: Int=0,
    var dislike: Int=0,
)
