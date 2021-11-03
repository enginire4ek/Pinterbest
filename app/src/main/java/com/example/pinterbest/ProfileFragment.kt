package com.example.pinterbest

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.pinterbest.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _backgroundProfile: FrameLayout? = null
    private val backgroundProfile get() = _backgroundProfile!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем фон и сразу делаем его невидимым
        _backgroundProfile = view.findViewById(R.id.main_profile)
        backgroundProfile.foreground = ResourcesCompat
            .getDrawable(resources, R.drawable.drawable_profile_background, null)
        backgroundProfile.foreground.alpha = 0

        binding.apply {
            profileDetails.setOnClickListener {
                onShowPopupWindow(view, R.layout.pop_up_profile)
            }
            buttonAdd.setOnClickListener {
                onShowPopupWindow(view, R.layout.pop_up_create)
            }
        }
    }

    private fun onShowPopupWindow(view: View, layout: Int) {
        val popupView: View = LayoutInflater.from(activity)
            .inflate(layout, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            true
        )

        popupWindow.apply {
            showAtLocation(view, Gravity.BOTTOM, 0, 0)
            animationStyle = R.style.PopUpAnimation
        }
        backgroundProfile.foreground.alpha = DARK_BACKGROUND

        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            backgroundProfile.foreground.alpha = NORMAL_BACKGROUND
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DARK_BACKGROUND = 100
        private const val NORMAL_BACKGROUND = 0
    }
}
