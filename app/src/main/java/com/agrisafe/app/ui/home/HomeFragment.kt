package com.agrisafe.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentHomeBinding

/**
 * Screen 1 — Home Screen
 * Shows the AgriSafe branding, device-connected pill, and TAP TO START TEST button.
 * Also resets any previous test session when this screen appears.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reset the test session whenever we land on Home
        val viewModel = (requireActivity() as MainActivity).testViewModel
        viewModel.resetSession()

        // Navigate to Select Test screen when tapped
        binding.btnStartTest.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_selectTest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
