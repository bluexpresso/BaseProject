package com.ankuradlakha.baseproject.ui.home.landing

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.databinding.FragmentSliderItemBinding
import com.ankuradlakha.baseproject.di.GlideApp
import com.ankuradlakha.baseproject.utils.CONTENT_TYPE_BUTTON
import com.ankuradlakha.baseproject.utils.CONTENT_TYPE_TEXT
import com.ankuradlakha.baseproject.utils.CONTENT_TYPE_VIDEO
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_slider_item.*

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
        if (videoContent != null) {
            GlideApp.with(requireContext()).asGif()
                .load(videoContent.url)
                .into(binding.gifMedia)
            if (!videoContent.elements.isNullOrEmpty()) {
                val buttonContent =
                    videoContent.elements.find { it.type.equals(CONTENT_TYPE_BUTTON, true) }
                val buttonTextContent = videoContent.elements.find {
                    it.type.equals(
                        CONTENT_TYPE_TEXT, true
                    )
                }
                if (buttonContent != null) {
                    binding.btnAction.setBackgroundColor(
                        Color.parseColor(
                            buttonContent.backgroundColor ?: "#FFFFFF"
                        )
                    )
                    binding.btnAction.setTextColor(
                        Color.parseColor(
                            buttonContent.foregroundColor ?: "#000000"
                        )
                    )
                    binding.btnAction.text = buttonContent.content ?: ""
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SliderItemViewModel::class.java)
    }

}