package com.example.neotaskmanager.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neotaskmanager.R
import com.example.neotaskmanager.databinding.FragmentMainPageBinding
import com.example.neotaskmanager.presentation.MainActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainPageFragment : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding: FragmentMainPageBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCurrentMonth()
        binding.btnCalendar.setOnClickListener {
            findNavController().navigate(R.id.calendarFragment)
        }
    }

    private fun setCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM", Locale("ru"))
        val currentDate = Date()
        val currentMonth = dateFormat.format(currentDate)
        binding.btnCalendar.text = currentMonth
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}