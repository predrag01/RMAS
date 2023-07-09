package elfak.mosis.underradar.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String="",
    var name: String="",
    var lastName: String="",
    var userName: String="",
    var email:String="",
    var phoneNumber: String="",
    var rang: Int=0,
    var points: Int=0,
    var imageURL: String=""
)
