package com.agrisafe.app.ui.summary

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentSummaryBinding

/**
 * Screen 6 — Summary with Farmer View / Expert Mode tabs.
 * Expert Mode shows raw DPV measurement data and a chart rendered via WebView.
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
        val isSafe = viewModel.isSafe()
        val statusText = if (isSafe) getString(R.string.status_safe) else getString(R.string.status_danger)

        // Populate Farmer View
        binding.tvTestType.text = getString(R.string.summary_test_type, testType)
        binding.tvResultVal.text = getString(R.string.summary_result_value, value)
        binding.tvStatus.text = getString(R.string.summary_status, statusText)
        binding.tvStatus.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isSafe) R.color.agri_green else R.color.danger_red
            )
        )

        // Load the DPV chart into WebView
        val chartHtml = buildChartHtml()
        binding.chartWebView.settings.javaScriptEnabled = false
        binding.chartWebView.setBackgroundColor(Color.TRANSPARENT)
        binding.chartWebView.loadDataWithBaseURL(null, chartHtml, "text/html", "utf-8", null)

        // Tab switching
        binding.tabFarmer.setOnClickListener { showFarmerView() }
        binding.tabExpert.setOnClickListener { showExpertView() }

        // Default: Farmer view active
        showFarmerView()

        // Back to Home — pop back stack to homeFragment
        binding.btnBackHome.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun showFarmerView() {
        binding.panelFarmer.visibility = View.VISIBLE
        binding.panelExpert.visibility = View.GONE

        binding.tabFarmer.setTextColor(ContextCompat.getColor(requireContext(), R.color.agri_green))
        binding.tabFarmer.setTypeface(null, android.graphics.Typeface.BOLD)
        binding.tabFarmer.setBackgroundResource(R.drawable.bg_tab_active)

        binding.tabExpert.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        binding.tabExpert.setTypeface(null, android.graphics.Typeface.NORMAL)
        binding.tabExpert.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    private fun showExpertView() {
        binding.panelFarmer.visibility = View.GONE
        binding.panelExpert.visibility = View.VISIBLE

        binding.tabExpert.setTextColor(ContextCompat.getColor(requireContext(), R.color.agri_green))
        binding.tabExpert.setTypeface(null, android.graphics.Typeface.BOLD)
        binding.tabExpert.setBackgroundResource(R.drawable.bg_tab_active)

        binding.tabFarmer.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        binding.tabFarmer.setTypeface(null, android.graphics.Typeface.NORMAL)
        binding.tabFarmer.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    /** Builds a self-contained HTML page with the SVG DPV curve chart. */
    private fun buildChartHtml(): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
              body { margin:0; padding:0; background:transparent; }
              svg { width:100%; height:110px; display:block; }
            </style>
            </head>
            <body>
            <svg viewBox="0 0 320 100" preserveAspectRatio="none" xmlns="http://www.w3.org/2000/svg">
              <!-- Axes -->
              <line x1="20" y1="5" x2="20" y2="88" stroke="#ccc" stroke-width="1"/>
              <line x1="20" y1="88" x2="315" y2="88" stroke="#ccc" stroke-width="1"/>
              <!-- DPV curve -->
              <polyline
                points="20,82 55,80 85,76 110,68 130,50 148,22 158,14 168,22 185,55 205,74 235,79 265,81 295,82 315,82"
                fill="none" stroke="#2D7A3A" stroke-width="2.5" stroke-linejoin="round" stroke-linecap="round"/>
              <!-- Peak marker (dashed red line) -->
              <line x1="158" y1="5" x2="158" y2="88" stroke="#C0392B" stroke-width="1.5" stroke-dasharray="4,3"/>
              <!-- Peak label -->
              <text x="162" y="20" font-size="11" fill="#C0392B" font-family="sans-serif">peak</text>
              <!-- Y axis label -->
              <text x="2" y="50" font-size="9" fill="#999" font-family="sans-serif" writing-mode="tb">µA</text>
              <!-- X axis label -->
              <text x="270" y="98" font-size="9" fill="#999" font-family="sans-serif">V</text>
            </svg>
            </body>
            </html>
        """.trimIndent()
    }

    override fun onDestroyView() {
        binding.chartWebView.destroy()
        super.onDestroyView()
        _binding = null
    }
}
