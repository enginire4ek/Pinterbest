package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.adapters.PinFeedHomeAdapter
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentProfileBinding
import com.example.pinterbest.presentation.mappers.MapToViewData
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.viewmodels.HomeViewModel
import com.example.pinterbest.presentation.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _backgroundProfile: FrameLayout? = null
    private val backgroundProfile get() = _backgroundProfile!!

    private lateinit var pinFeedHomeAdapter: PinFeedHomeAdapter

    private val args: ProfileFragmentArgs by navArgs()
    private var actualProfile: Int? = null

    private val viewModel: ProfileViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    private val viewModelPins: HomeViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    var avatarLink: String? = null
    var followers: Int? = null
    var following: Int? = null
    var profileId: Int? = null
    var boardId: Int? = null

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
        actualProfile = args.profileId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfileInfo()

        pinFeedHomeAdapter = PinFeedHomeAdapter()
        binding.rvMyPins.apply {
            adapter = pinFeedHomeAdapter
        }

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
            swipeRefresh.apply {
                setColorSchemeColors(ContextCompat.getColor(context, R.color.pinterest_color))
                setOnRefreshListener {
                    getProfileInfo()
                    isRefreshing = false
                }
            }
        }
    }

    private fun getProfileInfo() {
        if (actualProfile != -1) {
            viewModel.getProfileDetailsById(actualProfile!!)
            binding.mainProfile.visibility = View.VISIBLE
            initObservers()
            hideProfileSettings()
        } else {
            viewModel.getAuthStatus()
            initAuthObservers()
            showProfileSettings()
        }
    }

    private fun hideProfileSettings() {
        binding.apply {
            settings.visibility = View.GONE
            buttonAdd.visibility = View.GONE
        }
    }

    private fun showProfileSettings() {
        binding.apply {
            settings.visibility = View.VISIBLE
            buttonAdd.visibility = View.VISIBLE
        }
    }

    private fun initAuthObservers() {
        viewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            when (loggedIn) {
                true -> {
                    binding.mainProfile.visibility = View.VISIBLE
                    viewModel.getProfileDetails()
                    initObservers()
                }
                false -> {
                    val loginArgs = LoginFragmentArgs.Builder()
                    loginArgs.returnFragmentId = R.id.profileFragment
                    findNavController().navigate(R.id.loginFragment, loginArgs.build().toBundle())
                }
            }
        }
        viewModel.checkAuthError.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(response)
            }
        }
        viewModel.checkAuthState.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun initObservers() {
        viewModel.run {
            profile.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    profileId = response.id
                    showProfile(response)
                    viewModel.getProfileBoards(profileId!!)
                }
            }
            error.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showError(response)
                }
            }
            boards.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    boardId = viewModel.findSavedPinsBoardId(response)
                    viewModelPins.getPinDetailsById(boardId!!)
                }
            }
            loadingState.observe(viewLifecycleOwner) { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        viewModelPins.pins.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showPins(response)
            }
        }
    }

    private fun showPins(response: PinsList) {
        binding.emptyView.visibility = View.GONE
        binding.rvMyPins.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        pinFeedHomeAdapter.updateList(
            MapToViewData.mapToPinsListViewData(
                response
            )
        )
    }

    private fun showError(messageId: Int) {
        binding.emptyView.visibility = View.VISIBLE
        binding.profileView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.rvMyPins.visibility = View.GONE

        binding.errorText.text =
            ResourceProvider(resources).getString(messageId)
        binding.avatarPicture.setImageResource(R.drawable.ic_error)
        binding.usernameText.text = ""
    }

    private fun showProfile(profile: Profile) {
        binding.emptyView.visibility = View.GONE
        binding.profileView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        Glide.with(binding.avatarPicture.context)
            .load(profile.avatarLink)
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

    private fun onShowPopupWindow(view: View, layout: Int) {
        val popupView: View = LayoutInflater.from(activity).inflate(layout, null)
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
                    view.findNavController().popBackStack(R.id.homeFragment, false)
                }
            }
            R.layout.pop_up_create -> {
                popupWindow.contentView.findViewById<TextView>(R.id.pop_up_create_pin)
                    .setOnClickListener {
                        popupWindow.dismiss()
                        view.findNavController().navigate(R.id.pinCreationFragment)
                    }
                popupWindow.contentView.findViewById<TextView>(R.id.pop_up_create_board)
                    .setOnClickListener {
                        popupWindow.dismiss()
                        view.findNavController()
                            .navigate(R.id.boardCreationFragment)
                    }
            }
        }
        popupWindow.apply {
            showAtLocation(view, Gravity.BOTTOM, 0, 0)
            animationStyle = R.style.PopUpAnimation
        }
        backgroundProfile.foreground.alpha = DARK_BACKGROUND
        popupView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    popupWindow.dismiss()
                    backgroundProfile.foreground.alpha = NORMAL_BACKGROUND
                }
                MotionEvent.ACTION_UP -> v.performClick()
                else -> {}
            }
            true
        }
    }

    private fun setDataInPopUp(
        userTextView: TextView,
        followersTextView: TextView,
        imageView: ImageView
    ) {
        userTextView.text = binding.usernameText.text.toString()
        followersTextView.text = getString(R.string.followers_with_following, followers, following)
        Glide.with(imageView.context)
            .load(avatarLink)
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
    }
}
