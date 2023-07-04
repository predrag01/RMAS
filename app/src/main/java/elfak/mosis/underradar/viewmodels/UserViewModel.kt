package elfak.mosis.underradar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import elfak.mosis.underradar.data.User

class UserViewModel : ViewModel() {
    private val _user=MutableLiveData<User?>(null)
    private val _location=MutableLiveData<LatLng?>(null)
    fun getMutable()=_user

    var user
        get()=_user.value
        set(value){_user.value=value}

    fun getMutableLocation()=_location

    var location
        get()=_location.value
        set(value){_location.value=value}
}