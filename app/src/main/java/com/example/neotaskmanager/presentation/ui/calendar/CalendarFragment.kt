package com.example.neotaskmanager.presentation.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neotaskmanager.R
import com.example.neotaskmanager.databinding.FragmentCalendarBinding
import com.example.neotaskmanager.presentation.MainActivity
import java.text.SimpleDateFormat

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding
        get() = _binding!!
    private lateinit var calendar: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        calendar = binding.calendar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-0${month + 1}-$dayOfMonth"
            binding.btnBack.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("selectedDate", selectedDate)
                findNavController().navigate(R.id.mainPageFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
