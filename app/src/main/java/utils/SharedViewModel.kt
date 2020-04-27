package utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soywiz.klock.DateTime

class SharedViewModel: ViewModel() {
    val selectedDate: MutableLiveData<DateTime> = MutableLiveData()
}