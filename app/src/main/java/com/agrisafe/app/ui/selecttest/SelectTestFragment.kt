package com.agrisafe.app.ui.selecttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentSelectTestBinding

/**
 * Screen 2 — Select Test Screen
 * User selects exactly one test type. Continue button is enabled only after a selection.
 */
class SelectTestFragment : Fragment() {

    private var _binding: FragmentSelectTestBinding? = null
    private val binding get() = _binding!!

    // Track which card is selected
    private var selectedType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Continue button starts disabled
        binding.btnContinue.isEnabled = false
        binding.btnContinue.alpha = 0.5f

        // Card click listeners — selecting a card deselects all others
        binding.cardCrop.setOnClickListener { selectCard(getString(R.string.test_crop)) }
        binding.cardWater.setOnClickListener { selectCard(getString(R.string.test_water)) }
        binding.cardSoil.setOnClickListener { selectCard(getString(R.string.test_soil)) }

        binding.btnContinue.setOnClickListener {
            if (selectedType.isNotEmpty()) {
                val viewModel = (requireActivity() as MainActivity).testViewModel
                viewModel.selectedTestType = selectedType
                findNavController().navigate(R.id.action_selectTest_to_progress)
            }
        }
    }

    private fun selectCard(type: String) {
        selectedType = type
        updateCardUI()
        binding.btnContinue.isEnabled = true
        binding.btnContinue.alpha = 1.0f
    }

    private fun updateCardUI() {
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.agri_green)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.card_border)
        val selectedBg = ContextCompat.getColor(requireContext(), R.color.card_selected_bg)
        val defaultBg = ContextCompat.getColor(requireContext(), R.color.white)

        val cropSelected = selectedType == getString(R.string.test_crop)
        val waterSelected = selectedType == getString(R.string.test_water)
        val soilSelected = selectedType == getString(R.string.test_soil)

        applyCardStyle(binding.cardCrop, cropSelected, selectedColor, defaultColor, selectedBg, defaultBg)
        applyCardStyle(binding.cardWater, waterSelected, selectedColor, defaultColor, selectedBg, defaultBg)
        applyCardStyle(binding.cardSoil, soilSelected, selectedColor, defaultColor, selectedBg, defaultBg)
    }

    private fun applyCardStyle(
        card: View, selected: Boolean,
        selectedStroke: Int, defaultStroke: Int,
        selectedBg: Int, defaultBg: Int
    ) {
        val bg = card.background
        if (bg is android.graphics.drawable.GradientDrawable) {
            bg.setStroke(
                if (selected) 4 else 2,
                if (selected) selectedStroke else defaultStroke
            )
            bg.setColor(if (selected) selectedBg else defaultBg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
