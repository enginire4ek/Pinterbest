package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pinterbest.domain.entities.Pin
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.models.PinObjectViewData
// import com.example.pinterbest.data.models.Profile
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentActualPinBinding
import com.example.pinterbest.presentation.mappers.MapToViewData
import com.example.pinterbest.presentation.models.ProfileViewData
import com.example.pinterbest.presentation.viewmodels.ActualPinViewModel
import java.lang.Exception
import kotlin.math.roundToInt

class ActualPinFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentActualPinBinding? = null
    private val binding get() = _binding!!

    private val args: ActualPinFragmentArgs by navArgs()
    private lateinit var actualPin: PinObjectViewData

    private val viewModel: ActualPinViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActualPinBinding.inflate(
            inflater,
            container,
            false
        )

        actualPin = args.pinObjectViewData!!

        viewModel.getProfileDetailsById(actualPin.userID)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    private fun initObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showPin()
                setAuthorInfo(response)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(response)
            }
        }
        viewModel.state.observe(viewLifecycleOwner, { loading ->
                when (loading) {
                    true -> {
                        binding.actualPinScreen.visibility = View.GONE
                        binding.emptyView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        )
    }

    private fun showPin() {
        binding.actualPinScreen.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE

        val ratioVar = actualPin.imageHeight / actualPin.imageWidth.toDouble()
        binding.actualPinImage.layoutParams.height =
            (binding.actualPinImage.layoutParams.width * ratioVar).roundToInt()
        setImageResource(actualPin.imageLink, binding.actualPinImage)

        binding.actualPinTitle.text = actualPin.title
        binding.actualPinDescription.text = actualPin.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setAuthorInfo(profile: Profile) {
        val profileViewData = MapToViewData.mapToProfileViewData(profile)
        binding.actualPinAuthor.text = profileViewData.username
        binding.actualPinFollowers.text = getString(
            R.string.followers_actual_pin,
            profileViewData.followers
        )
        setImageResource(profileViewData.avatarLink, binding.actualPinAvatar, pinImage = false)
    }

    private fun showError(error: String) {
        binding.actualPinScreen.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.errorText.text = error
    }

    fun setImageResource(imageLink: String, view:ImageView, pinImage:Boolean=true) {
        val url = BASE_URL_IMAGES + imageLink
        if (pinImage) {
            Glide.with(view.context)
                .load(url)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .into(view)
        } else {
            Glide.with(view.context)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .into(view)
        }
    }

    companion object {
        const val BASE_URL_IMAGES = "https://pinterbest-bucket.s3.eu-central-1.amazonaws.com/"
    }
}
