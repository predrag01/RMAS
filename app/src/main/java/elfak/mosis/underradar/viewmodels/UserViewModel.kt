package elfak.mosis.underradar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.underradar.data.User

class UserViewModel : ViewModel() {
    private val _user=MutableLiveData<User?>(null)

    fun getMutable()=_user

    var user
        get()=_user.value
        set(value){_user.value=value}
}