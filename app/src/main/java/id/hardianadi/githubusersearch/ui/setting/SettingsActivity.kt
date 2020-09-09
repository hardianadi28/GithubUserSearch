package id.hardianadi.githubusersearch.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.service.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
        }

        private fun init() {
            val languageSetting = resources.getString(R.string.key_language)
            val languagePref = findPreference<Preference>(languageSetting)
            languagePref?.setOnPreferenceClickListener {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }

            val notifSetting = resources.getString(R.string.key_notification)
            val notifPref = findPreference<SwitchPreferenceCompat>(notifSetting)
            notifPref?.setOnPreferenceChangeListener { preference, newValue ->
                val alarmReceiver = AlarmReceiver()
                alarmReceiver.setAlarm(this@SettingsFragment.requireContext(), newValue as Boolean)
                true
            }
        }
    }
}