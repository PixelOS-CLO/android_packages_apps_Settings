/*
 * SPDX-FileCopyrightText: 2017-2023 The LineageOS Project
 * SPDX-FileCopyrightText: PixelOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.custom.networktraffic;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.custom.preference.SecureSettingSwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class NetworkTrafficSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener  {

    private static final String REFRESH_INTERVAL = "network_traffic_refresh_interval";
    private static final String AUTOHIDE_THRESHOLD = "network_traffic_autohide_threshold";

    private ListPreference mNetTrafficMode;
    private SecureSettingSwitchPreference mNetTrafficAutohide;
    private ListPreference mNetTrafficInterval;
    private ListPreference mNetTrafficThreshold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.network_traffic_settings);
        getActivity().setTitle(R.string.network_traffic_settings_title);

        final ContentResolver resolver = getActivity().getContentResolver();

        mNetTrafficMode = findPreference(Settings.Secure.NETWORK_TRAFFIC_MODE);
        mNetTrafficMode.setOnPreferenceChangeListener(this);
        int mode = Settings.Secure.getInt(resolver,
                Settings.Secure.NETWORK_TRAFFIC_MODE, 0);
        mNetTrafficMode.setValue(String.valueOf(mode));

        mNetTrafficAutohide = findPreference(Settings.Secure.NETWORK_TRAFFIC_AUTOHIDE);
        mNetTrafficAutohide.setOnPreferenceChangeListener(this);

        mNetTrafficInterval = findPreference(REFRESH_INTERVAL);
        mNetTrafficInterval.setOnPreferenceChangeListener(this);
        mNetTrafficInterval.setValue(String.valueOf(Settings.Secure.getInt(
                resolver, REFRESH_INTERVAL, 1)));
        mNetTrafficThreshold = findPreference(AUTOHIDE_THRESHOLD);
        mNetTrafficThreshold.setOnPreferenceChangeListener(this);
        mNetTrafficThreshold.setValue(String.valueOf(Settings.Secure.getInt(
                resolver, AUTOHIDE_THRESHOLD, 1)));

        updateEnabledStates(mode);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNetTrafficMode) {
            int mode = Integer.parseInt((String) newValue);
            Settings.Secure.putInt(getActivity().getContentResolver(),
                    Settings.Secure.NETWORK_TRAFFIC_MODE, mode);
            updateEnabledStates(mode);
        } else if (preference == mNetTrafficInterval || preference == mNetTrafficThreshold) {
            Settings.Secure.putInt(getActivity().getContentResolver(),
                    preference.getKey(), Integer.parseInt((String) newValue));
        }
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM;
    }

    private void updateEnabledStates(int mode) {
        final boolean enabled = mode != 0;
        mNetTrafficAutohide.setEnabled(enabled);
        mNetTrafficInterval.setEnabled(enabled);
        mNetTrafficThreshold.setEnabled(enabled);
    }
}
