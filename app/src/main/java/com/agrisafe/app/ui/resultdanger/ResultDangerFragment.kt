package com.agrisafe.app.ui.resultdanger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentResultDangerBinding

/**
 * Screen 5 — Result: DANGER
 * Shown when the generated random value is > 75.
 * Displays the result value, an over-limit bar, and a danger recommendation.
 */
class ResultDangerFragment : Fragment() {

    private var _binding: FragmentResultDangerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultDangerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (requireActivity() as MainActivity).testViewModel
        val value = viewModel.randomValue

        // "Result Value : X" display
        binding.tvResultValue.text = getString(R.string.result_value_label, value)

        // Percentage of the 100 ppb limit — value is 76-150
        val percentage = value
        binding.tvPctBadge.text = getString(R.string.pct_danger_label, percentage)

        // Progress bar capped at 100 visually but value text shows actual
        binding.progressBar.progress = 100

        // Populate meta
        binding.tvMeta.text = getString(R.string.meta_label, viewModel.selectedTestType, value)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_resultDanger_to_summary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
