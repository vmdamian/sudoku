package com.example.sudoku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentSettingsBinding

class Settings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater, R.layout.fragment_settings, container, false)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.switchDarkMode.isChecked = true
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked -> if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.buttonBackSettings.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_mainMenu)
        }

        binding.volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, volume : Int, b: Boolean) {
                binding.volumeText.text = "$volume"
                // Max volume is 100 (set in the component), media player accepts 0 to 1.
                (activity as MainActivity).player?.setVolume(volume.toFloat() / 100, volume.toFloat() / 100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        return binding.root
    }
}