package com.idslogic.levelshoes.ui.home.landing

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Fade
import androidx.transition.Slide
import com.google.gson.Gson
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.databinding.FragmentSliderItemBinding
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.*

class SliderItemFragment : Fragment() {

    companion object {
        private const val ARG_SLIDER_CONTENT = "arg_slider_content"
        fun newInstance(content: Content?) = SliderItemFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SLIDER_CONTENT, Gson().toJson(content))
            }
        }
    }

    private lateinit var viewModel: SliderItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_slider_item, container, false)
                    as FragmentSliderItemBinding
        initContent(binding)
        return binding.root
    }

    private fun initContent(binding: FragmentSliderItemBinding) {
        val args = arguments
        args ?: return
        val content = Gson().fromJson(
            args.getString(ARG_SLIDER_CONTENT, ""), Content::class.java
        )
        if (content.data.isNullOrEmpty()) return
        val videoContent = content.data!!.find { it.type.equals(CONTENT_TYPE_VIDEO, true) }
        val imageContent = content.data!!.find { it.type.equals(CONTENT_TYPE_IMAGE, true) }
        val videoStatus = videoContent?.status ?: 0
        val imageStatus = imageContent?.status ?: 0
        val activeContent =
            if (videoStatus == imageStatus || imageStatus == 1) imageContent else videoContent
        activeContent?.let {
            val mediaUrl = it.url
            val placeHolderColor = ContextCompat.getColor(requireContext(), R.color.gray_38)
            if (mediaUrl.endsWith(".gif")) {
                GlideApp.with(requireContext()).asGif()
                    .load(mediaUrl)
                    .placeholder(ColorDrawable(placeHolderColor))
                    .into(binding.gifMedia)
            } else {
                GlideApp.with(requireContext())
                    .load(mediaUrl)
                    .placeholder(ColorDrawable(placeHolderColor))
                    .into(binding.gifMedia)
            }
            val buttonContent =
                it.elements.find { cnt -> cnt.type.equals(CONTENT_TYPE_BUTTON, true) }
            val buttonTextContent = it.elements.find { cnt ->
                cnt.type.equals(
                    CONTENT_TYPE_TEXT, true
                )
            }
            val heading = it.elements.find { cnt ->
                cnt.type.equals(CONTENT_TYPE_HEADING, true)
            }
            val subHeading = it.elements.find { cnt ->
                cnt.type.equals(CONTENT_TYPE_SUBHEADING, true)
            }
            heading?.let { hdngElement ->
                binding.heading.visibility = View.VISIBLE
                try {
                    binding.heading.setTextColor(
                        Color.parseColor(hdngElement.foregroundColor ?: "#000000")
                    )
                } catch (e: Exception) {
                }
                binding.heading.text = hdngElement.content

            }
            subHeading?.let { subHdngElement ->
                binding.subHeading.visibility = View.VISIBLE
                try {
                    binding.subHeading.setTextColor(
                        Color.parseColor(
                            subHdngElement.foregroundColor ?: "" +
                            "#000000"
                        )
                    )
                } catch (e: Exception) {
                }
                binding.subHeading.text = subHdngElement.content
            }
            buttonContent?.let { btnContent ->
                binding.btnAction.visibility = View.VISIBLE
                try {
                    binding.btnAction.setBackgroundColor(
                        Color.parseColor(
                            btnContent.backgroundColor ?: "#000000"
                        )
                    )
                    binding.btnAction.setTextColor(
                        Color.parseColor(
                            btnContent.foregroundColor ?: buttonTextContent?.foregroundColor
                            ?: "#000000"
                        )
                    )
                } catch (e: Exception) {
                }
                if (buttonTextContent?.content.isNullOrEmpty() && btnContent.content.isNullOrEmpty()) {
                    binding.btnAction.visibility = View.GONE
                } else {
                    binding.btnAction.visibility = View.VISIBLE
                    binding.btnAction.text = btnContent.content ?: buttonTextContent?.content ?: ""
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SliderItemViewModel::class.java)
    }

}