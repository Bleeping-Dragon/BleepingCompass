package com.bleepingdragon.compass

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.bleepingdragon.compass.databinding.FragmentSettingsBinding
import com.bleepingdragon.compass.modules.SharedPreferences


class SettingsFragment : Fragment() {

    //Fragment binding
    private var _binding: FragmentSettingsBinding? = null

    //This property is only valid between onCreateView and onDestroyView.
    //https://developer.android.com/topic/libraries/view-binding?hl=es-419
    private val binding get() = _binding!!


    //When creating the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }


    //When creating the fragment ui
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    
    //When the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Bindings Text
        binding.screenOnView.settingTitle.text = getString(R.string.keep_screen_on)
        binding.screenOnView.viewSwitch.text = getString(R.string.keep_the_screen_on)

        binding.detailsOnView.settingTitle.text = getString(R.string.show_details)
        binding.detailsOnView.viewSwitch.text = getString(R.string.can_show_details)

        binding.githubLinkView.settingTitle.text = getString(R.string.check_the_code_on_github)
        binding.bleepingLinkView.settingTitle.text = getString(R.string.discover_more_bleeping_dragon_projects)
        binding.privacyLinkView.settingTitle.text = getString(R.string.privacy_policy)

        //Clear the screen always on (only used in compass fragment)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //Bind the actions
        binding.screenOnView.settingToggleView.setOnClickListener {
            val previousValue = SharedPreferences.GetBoolPreference("keepScreenOn", requireActivity())
            SharedPreferences.SetBoolPreference("keepScreenOn", !previousValue , this.requireActivity())
            binding.screenOnView.viewSwitch.isChecked = !previousValue
        }

        binding.detailsOnView.settingToggleView.setOnClickListener {
            val previousValue = SharedPreferences.GetBoolPreference("isDetailsActive", requireActivity())
            SharedPreferences.SetBoolPreference("isDetailsActive", !previousValue , this.requireActivity())
            binding.detailsOnView.viewSwitch.isChecked = !previousValue
        }

        binding.githubLinkView.settingTapView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Bleeping-Dragon/BleepingCompass"))
            startActivity(browserIntent)
        }

        binding.bleepingLinkView.settingTapView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bleepingdragon.com"))
            startActivity(browserIntent)
        }

        binding.privacyLinkView.settingTapView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bleepingdragon.com/markdown/privacy-policies/BleepingCompass/"))
            startActivity(browserIntent)
        }
    }


    //Restore the checks
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val keepScreenOn = SharedPreferences.GetBoolPreference("keepScreenOn", this.requireActivity())
        binding.screenOnView.viewSwitch.isChecked = keepScreenOn
        binding.screenOnView.viewSwitch.jumpDrawablesToCurrentState() //Omit the transition

        val isDetailsActive = SharedPreferences.GetBoolPreference("isDetailsActive", this.requireActivity())
        binding.detailsOnView.viewSwitch.isChecked = isDetailsActive
        binding.detailsOnView.viewSwitch.jumpDrawablesToCurrentState()
    }



    //Delete the bindings
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}