package com.agrisafe.app.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentSummaryBinding

/**
 * Screen 6 — Final Summary Screen
 * Shows:
 *   • Selected Test Type
 *   • Generated Random Value
 *   • Final Status (SAFE / DANGER)
 *
 * "Back to Home" pops the entire back stack back to the Home fragment
 * so a completely fresh test starts if the user taps "TAP TO START TEST" again.
 */
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (requireActivity() as MainActivity).testViewModel
        val value = viewModel.randomValue
        val testType = viewModel.selectedTestType
        val statusText = if (viewModel.isSafe())
            getString(R.string.status_safe) else getString(R.string.status_danger)

        binding.tvTestType.text = getString(R.string.summary_test_type, testType)
        binding.tvResultVal.text = getString(R.string.summary_result_value, value)
        binding.tvStatus.text = getString(R.string.summary_status, statusText)

        // Colour the status row green or red
        if (viewModel.isSafe()) {
            binding.tvStatus.setTextColor(
                requireContext().getColor(R.color.agri_green)
            )
        } else {
            binding.tvStatus.setTextColor(
                requireContext().getColor(R.color.danger_red)
            )
        }

        // "Back to Home" — pop back stack all the way to homeFragment (inclusive = false keeps home)
        binding.btnBackHome.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
