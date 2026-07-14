package com.agrisafe.app.ui.progress

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agrisafe.app.MainActivity
import com.agrisafe.app.R
import com.agrisafe.app.databinding.FragmentProgressBinding

/**
 * Screen 3 — Testing Progress
 * Generates the random value ONCE on entry, shows a 3-second animation, then navigates
 * to the SAFE or DANGER result screen based on the generated value.
 */
class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null
    private val DEMO_DURATION_MS = 3000L   // 3 seconds for demo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (requireActivity() as MainActivity).testViewModel

        // Generate random value ONCE for this session
        viewModel.generateRandomValue()

        // Animate the circular progress bar
        val animator = ObjectAnimator.ofInt(binding.progressCircular, "progress", 0, 100)
        animator.duration = DEMO_DURATION_MS
        animator.interpolator = LinearInterpolator()
        animator.start()

        // Countdown timer updates the display text and navigates on completion
        countDownTimer = object : CountDownTimer(DEMO_DURATION_MS, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000) + 1
                binding.tvTimer.text = "${secondsLeft}s"
            }

            override fun onFinish() {
                binding.tvTimer.text = getString(R.string.done_label)
                // Navigate based on the generated value
                if (isAdded) {
                    if (viewModel.isSafe()) {
                        findNavController().navigate(R.id.action_progress_to_resultSafe)
                    } else {
                        findNavController().navigate(R.id.action_progress_to_resultDanger)
                    }
                }
            }
        }.start()
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
        _binding = null
    }
}
