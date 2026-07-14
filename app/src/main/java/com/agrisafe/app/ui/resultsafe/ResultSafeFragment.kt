package com.agrisafe.app.ui.resultsafe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentResultSafeBinding

/**
 * Screen 4 — Result: SAFE
 * Shown when the generated random value is <= 75.
 * Displays the result value, a percentage bar, and a recommendation.
 */
class ResultSafeFragment : Fragment() {

    private var _binding: FragmentResultSafeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultSafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (requireActivity() as MainActivity).testViewModel
        val value = viewModel.randomValue

        // "Result Value : X" display
        binding.tvResultValue.text = getString(R.string.result_value_label, value)

        // Show percentage of the 100 ppb limit (value is 0-75, limit is 100)
        val percentage = value
        binding.tvPctBadge.text = getString(R.string.pct_safe_label, percentage)

        // Set progress bar — bar represents value as % of 100
        binding.progressBar.progress = percentage

        // Populate meta — use selected test type
        binding.tvMeta.text = getString(R.string.meta_label, viewModel.selectedTestType, value)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_resultSafe_to_summary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
