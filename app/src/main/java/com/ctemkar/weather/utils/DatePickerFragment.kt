package com.ctemkar.weather.utils

import com.ctemkar.weather.ViewModels.SharedViewModel
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.soywiz.klock.DateFormat
import com.soywiz.klock.parseUtc
import timber.log.Timber
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR) -1 // start with previous year
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        Timber.d("Date selected: $month-$day-$year")
        val viewModel = ViewModelProviders.of(requireActivity())[SharedViewModel::class.java]
        val dateFormat = DateFormat("yyyy-MM-dd")
        val mm = String.format("%02d", month+1)
        val dd = String.format("%02d", day)
        Timber.d("date: $year-$mm-$dd")
        viewModel.selectedDate.value = dateFormat.parseUtc("$year-$mm-$dd")
    }
}