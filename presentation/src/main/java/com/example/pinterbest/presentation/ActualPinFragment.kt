package com.example.pinterbest.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentActualPinBinding
import com.example.pinterbest.presentation.mappers.MapToViewData
import com.example.pinterbest.presentation.models.PinObjectViewData
import com.example.pinterbest.presentation.viewmodels.ActualPinViewModel

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

        binding.profileLink.setOnClickListener {
            val direction = ActualPinFragmentDirections
                .actionActualPinFragmentToProfileFragment().setProfileId(actualPin.userID)
            it.findNavController().navigate(direction)
        }

        sharePinListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun sharePinListener() {
        binding.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                val message = getString(
                    R.string.message_text,
                    "pinterbest.ru/pin/${actualPin.id}"
                )
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
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
        viewModel.loadingState.observe(
            viewLifecycleOwner
        ) { loading ->
            if (loading) {
                binding.actualPinScreen.visibility = View.GONE
                binding.emptyView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.actualPinScreen.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }
    }

    private fun showPin() {
        val displayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels
        binding.actualPinImage.layoutParams.width = dpWidth

        setImageResource(actualPin.imageLink, binding.actualPinImage)

        binding.actualPinTitle.text = actualPin.title
        binding.actualPinDescription.text = actualPin.description
    }

    private fun showError(error: String) {
        binding.actualPinScreen.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.errorText.text = error
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

    fun setImageResource(imageLink: String, view: ImageView, pinImage: Boolean = true) {
        if (pinImage) {
            Glide.with(view.context)
                .load(imageLink)
                .fitCenter()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .into(view)
        } else {
            Glide.with(view.context)
                .load(imageLink)
                .circleCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .into(view)
        }
    }
}
