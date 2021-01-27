package com.example.madesubmission.ui.setting

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.madesubmission.R
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val useCase: GameUseCase by inject()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<Preference>("search_history")?.setOnPreferenceClickListener {
                context?.let {
                    AlertDialog.Builder(it)
                        .setMessage(getString(R.string.clear_history_confirmation))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            lifecycleScope.launch {
                                useCase.clearRecentSearch()
                            }
                            Toast.makeText(it, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton(getString(R.string.no), null)
                        .show()
                }

                true
            }
        }
    }
}