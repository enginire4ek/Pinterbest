package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentProfileBinding
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _backgroundProfile: FrameLayout? = null
    private val backgroundProfile get() = _backgroundProfile!!

    private val viewModel: ProfileViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    var avatarLink: String? = null
    var followers: Int? = null
    var following: Int? = null

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
        appComponent.inject(this)
        viewModel.getProfileDetails()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        _backgroundProfile = view.findViewById(R.id.profileView)
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
            settings.setOnClickListener {
                onShowPopupWindow(view, R.layout.pop_up_exit)
            }
        }
    }

    private fun initObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showProfile(response)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(response)
            }
        }
        viewModel.state.observe(
            viewLifecycleOwner,
            { loading ->
                when (loading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> binding.progressBar.visibility = View.GONE
                }
            }
        )
    }

    private fun showError(messageId: Int) {
        if (binding.emptyView.visibility != View.VISIBLE) {
            binding.emptyView.visibility = View.VISIBLE
        }
        if (binding.profileView.visibility != View.GONE) {
            binding.profileView.visibility = View.GONE
        }
        if (binding.progressBar.visibility != View.GONE) {
            binding.progressBar.visibility = View.GONE
        }
        binding.errorText.text = ResourceProvider(resources).getString(messageId)
        binding.avatarPicture.setImageResource(R.drawable.ic_error)
        binding.usernameText.text = ""
    }

    private fun showProfile(profile: Profile) {
        hideEmptyView()
        Glide.with(binding.avatarPicture.context)
            .load(BASE_URL_IMAGES + profile.avatarLink)
            .placeholder(R.drawable.progress_animation)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.ic_error)
            .into(binding.avatarPicture)
        binding.usernameText.text = profile.username

        avatarLink = profile.avatarLink
        followers = profile.followers
        following = profile.following
    }

    private fun hideEmptyView() {
        if (binding.emptyView.visibility != View.GONE) {
            binding.emptyView.visibility = View.GONE
        }
        if (binding.profileView.visibility != View.VISIBLE) {
            binding.profileView.visibility = View.VISIBLE
        }
        if (binding.progressBar.visibility != View.GONE) {
            binding.progressBar.visibility = View.GONE
        }
        if (binding.rvMyPins.visibility != View.VISIBLE) {
            binding.rvMyPins.visibility = View.VISIBLE
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

        when (layout) {
            R.layout.pop_up_profile -> {
                val imageView = popupWindow.contentView
                    .findViewById<ImageView>(R.id.pop_up_avatar_picture)
                val userTextView = popupWindow.contentView
                    .findViewById<TextView>(R.id.pop_up_username_text)
                val followersTextView = popupWindow.contentView
                    .findViewById<TextView>(R.id.followers_follow)
                setDataInPopUp(userTextView, followersTextView, imageView)
            }
            R.layout.pop_up_exit -> {
                val exitButton = popupWindow.contentView
                    .findViewById<TextView>(R.id.pop_up_exit_profile)
                exitButton.setOnClickListener {
                    viewModel.saveCookie()
                    popupWindow.dismiss()
                    view.findNavController().navigate(R.id.loginFragment)
                }
            }
            R.layout.pop_up_create -> {
                val createPin = popupWindow.contentView
                    .findViewById<TextView>(R.id.pop_up_create_pin)
                val createBoard = popupWindow.contentView
                    .findViewById<TextView>(R.id.pop_up_create_board)
                createPin.setOnClickListener {
                    popupWindow.dismiss()
                    view.findNavController().navigate(R.id.pinCreationFragment)
                }
                createBoard.setOnClickListener {
                    popupWindow.dismiss()
                    view.findNavController().navigate(R.id.boardCreationFragment)
                }
            }
        }

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

    private fun setDataInPopUp(
        userTextView: TextView,
        followersTextView: TextView,
        imageView: ImageView
    ) {
        userTextView.text = binding.usernameText.text.toString()
        getString(R.string.followers)
        followersTextView.text = "$followers " + getString(R.string.followers) +
            "$following " + getString(R.string.following)
        Glide.with(imageView.context)
            .load(BASE_URL_IMAGES + avatarLink)
            .placeholder(R.drawable.progress_animation)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.ic_error)
            .into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DARK_BACKGROUND = 100
        private const val NORMAL_BACKGROUND = 0
        const val BASE_URL_IMAGES = "https://pinterbest-bucket.s3.eu-central-1.amazonaws.com/"
    }
}
