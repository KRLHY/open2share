package top.linesoft.open2share

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import top.linesoft.open2share.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.settingsMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, object : PreferenceFragmentCompat() {
                override fun onCreatePreferences(
                    savedInstanceState: Bundle?,
                    rootKey: String?
                ) {
                    setPreferencesFromResource(R.xml.root_preferences, rootKey)
                    findPreference<Preference>("guide")?.setOnPreferenceClickListener {
                        startActivity(Intent(activity, GuideActivity::class.java))
                        return@setOnPreferenceClickListener true
                    }
                    findPreference<SwitchPreferenceCompat>("hide_icon")?.setOnPreferenceChangeListener { preference, newValue ->
//                Toast.makeText(getContext(),"当前值为："+newValue,Toast.LENGTH_LONG).show();
                        val hideComponentName =
                            ComponentName(requireContext(), "top.linesoft.open2share.hide_icon")
                        val unhideComponentName =
                            ComponentName(requireContext(), "top.linesoft.open2share.unhide_icon")
                        if (newValue as Boolean) {
                            MaterialAlertDialogBuilder(requireActivity()).apply {
                                setTitle(R.string.warn)
                                setMessage(R.string.hide_tips)
                                setPositiveButton(R.string.yes, { dialog, which ->
                                    packageManager.setComponentEnabledSetting(
                                        hideComponentName,
                                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                        PackageManager.DONT_KILL_APP
                                    )
                                    packageManager.setComponentEnabledSetting(
                                        unhideComponentName,
                                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                        PackageManager.DONT_KILL_APP
                                    )
                                    findPreference<SwitchPreferenceCompat>("hide_icon")?.setChecked(
                                        true
                                    )
                                })
                                setNegativeButton(R.string.no, null)
                                create()
                                show()
                            }
                            return@setOnPreferenceChangeListener false
                        } else {
                            packageManager.setComponentEnabledSetting(
                                hideComponentName,
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            packageManager.setComponentEnabledSetting(
                                unhideComponentName,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP
                            )
                            return@setOnPreferenceChangeListener true
                        }
                    }
                    findPreference<SwitchPreferenceCompat>("use_file_uri")?.setOnPreferenceChangeListener { preference, newValue ->
                        if (newValue as Boolean) {
                            MaterialAlertDialogBuilder(requireActivity()).apply {
                                setTitle(R.string.warn)
                                setMessage(R.string.open_file_uri_msg)
                                setPositiveButton(R.string.yes, { dialog, which ->
                                    findPreference<SwitchPreferenceCompat>("use_file_uri")?.setChecked(
                                        true
                                    )
                                })
                                setNegativeButton(R.string.no, { dialog, which ->
                                    findPreference<SwitchPreferenceCompat>("use_file_uri")?.setChecked(
                                        false
                                    )
                                })
                                create()
                                show()
                            }
                            return@setOnPreferenceChangeListener false
                        } else {
                            return@setOnPreferenceChangeListener true
                        }
                    }
                    findPreference<Preference>("about")?.setOnPreferenceClickListener {
                        MaterialAlertDialogBuilder(requireActivity()).apply {
                            setTitle(R.string.about_dialogue_title)
                            setMessage(R.string.about_dialogue_msg)
                            setPositiveButton(R.string.ok, null)
                            create()
                            show()
                        }
                        return@setOnPreferenceClickListener false
                    }
                }
            })
            .commit()
    }
}