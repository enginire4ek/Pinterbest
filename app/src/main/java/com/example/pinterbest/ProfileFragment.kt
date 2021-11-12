package com.example.pinterbest

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
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterbest.api.ApiClient
import com.example.pinterbest.api.ApiEndpoints
import com.example.pinterbest.api.ApiService
import com.example.pinterbest.data.models.Profile
import com.example.pinterbest.databinding.FragmentProfileBinding
import com.example.pinterbest.utilities.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var _backgroundProfile: FrameLayout? = null
    private val backgroundProfile get() = _backgroundProfile!!

    var avatarLink: String? = null
    var followers: Int? = null
    var following: Int? = null

    private val apiService = ApiClient().retrofit.create(ApiService::class.java)

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

        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                apiService.getProfile(SessionManager(requireActivity()).fetchCookie()!!)
            }
            if (response.isSuccessful) {
                showProfile(response)
                avatarLink = response.body()?.avatarLink
                followers = response.body()?.followers
                following = response.body()?.following
            } else {
                showError()
            }
        }

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
            settings.setOnClickListener {
                onShowPopupWindow(view, R.layout.pop_up_exit)
            }
        }
    }

    private fun showError() {
        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        binding.avatarPicture.setImageResource(R.drawable.ic_error)
        binding.usernameText.text = ""
    }

    private fun showProfile(response: Response<Profile>) {
        response.body().let {
            Glide.with(binding.avatarPicture.context)
                .load(ApiEndpoints.BASE_URL_IMAGES + it?.avatarLink)
                .placeholder(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .error(R.drawable.ic_error)
                .into(binding.avatarPicture)
            binding.usernameText.text = it?.username
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
                val imageView = popupWindow.contentView.findViewById<ImageView>(
                    R.id.pop_up_avatar_picture
                )
                val userTextView = popupWindow.contentView.findViewById<TextView>(
                    R.id.pop_up_username_text
                )
                val followersTextView = popupWindow.contentView.findViewById<TextView>(
                    R.id.followers_follow
                )
                setDataInPopUp(userTextView, followersTextView, imageView)
            }
            R.layout.pop_up_exit -> {
                val exitButton = popupWindow.contentView.findViewById<TextView>(
                    R.id.pop_up_exit_profile
                )
                exitButton.setOnClickListener {
                    SessionManager(requireActivity()).saveUserData(
                        cookie = "",
                        username = "",
                        password = "",
                        status = false
                    )
                    popupWindow.dismiss()
                    view.findNavController().navigate(R.id.loginFragment)
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
            .load(ApiEndpoints.BASE_URL_IMAGES + avatarLink)
            .placeholder(R.drawable.ic_loading)
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
