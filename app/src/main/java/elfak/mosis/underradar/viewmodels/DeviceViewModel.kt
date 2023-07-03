package elfak.mosis.underradar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.underradar.data.Device

class DeviceViewModel : ViewModel() {

    private val _device=MutableLiveData<Device?>(null)

    fun getMutable() = _device

    var device
        get() = _device.value
        set(value) { _device.value=value}
}