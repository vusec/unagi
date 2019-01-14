/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.inputmethoddl.latin;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.text.AutoText;
import android.util.Log;

@SuppressLint("NewApi")
public class LatinIMESettings extends PreferenceActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String QUICK_FIXES_KEY = "quick_fixes";
    private static final String SHOW_SUGGESTIONS_KEY = "show_suggestions";
    private static final String PREDICTION_SETTINGS_KEY = "prediction_settings";
    private static final String CLEAR_LOG_KEY = "clear_log";
    private static final String SEND_LOG_KEY = "send_log";
    private static final String SCRAMBLE_OUTPUT = "scramble_output";
    
    private CheckBoxPreference mQuickFixes;
    private CheckBoxPreference mShowSuggestions;
    private Preference mClearLog;
    private Preference mSendLog;
    private CheckBoxPreference mScrambleOutput;
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs);
        mQuickFixes = (CheckBoxPreference) findPreference(QUICK_FIXES_KEY);
        mShowSuggestions = (CheckBoxPreference) findPreference(SHOW_SUGGESTIONS_KEY);
        mClearLog = findPreference(CLEAR_LOG_KEY);
        mSendLog = findPreference(SEND_LOG_KEY);
        mScrambleOutput = (CheckBoxPreference) findPreference(SCRAMBLE_OUTPUT);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
        
        mClearLog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        	// http://www.kaloer.com/android-preferences
        	@Override
			public boolean onPreferenceClick(Preference preference)
        	{
        		// TODO Majdan
        		Log.v("MAJDAN", "mClearLog.setOnPreferenceClickListener");
        		return true;
        	}
        });
        
        mSendLog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Majdan
				Log.v("MAJDAN", "mSendLog.setOnPreferenceClickListener");
				return true;
			}
		});
    }

    @Override
    protected void onResume() {
        super.onResume();
        int autoTextSize = AutoText.getSize(getListView());
        if (autoTextSize < 1) {
            ((PreferenceGroup) findPreference(PREDICTION_SETTINGS_KEY))
                .removePreference(mQuickFixes);
        } else {
            mShowSuggestions.setDependency(QUICK_FIXES_KEY);
        }
    }

    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onDestroy();
    }

    @Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
    }
}
