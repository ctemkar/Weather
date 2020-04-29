package utils

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soywiz.klock.DateTime

class SharedViewModel: ViewModel() {
    val selectedDate: MutableLiveData<DateTime> = MutableLiveData()
    var currentLocation : MutableLiveData<Location> = MutableLiveData()
    fun setLocation(loc : Location) {currentLocation.value = loc}
}
