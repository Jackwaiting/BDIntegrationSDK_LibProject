package com.chipsguide.app.colorbluetoothlamp.v2.brunton.utils;


import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.chipsguide.app.colorbluetoothlamp.v2.brunton.R;

public class PreferenceUtil {

	private static String PLAY_MODE = "play_mode";
	private static String FM_FREQUENCY = "fm_frequency";
	private static String PHONE_MUSIC_POSITION = "phone_music_position";
	private static String PHONE_MUSIC_CURRENT_DURATION = "phone_music_current_duration";
	private static String SHAKE_OPTION = "shake_option";
	private static String FIRST_TIME_ENTER_ALARM = "first_time_enter_alarm";
	private static String FIRST_LAUNCH = "first_launch";
	private static String COLORS = "colors";
	private Set<String> set = new HashSet<String>();
	
	private static SharedPreferences sp;
	private static PreferenceUtil settingPrefences;

	private PreferenceUtil(Context context) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public static PreferenceUtil getIntance(Context context) {
		if (null == settingPrefences) {
			settingPrefences = new PreferenceUtil(context);
		}
		return settingPrefences;
	}

	public void savePlayMode(int mode) {
		sp.edit().putInt(PLAY_MODE, mode).commit();
	}
	
	public int getPlayMode() {
		return sp.getInt(PLAY_MODE,0);
	}
	
	public void saveFmFrequency(float frequency){
		sp.edit().putFloat(FM_FREQUENCY, frequency).commit();
	}
	
	public float getFmFrequency(){
		return sp.getFloat(FM_FREQUENCY,87.5f);
	}
	/**
	 * 手机音乐播放的位置
	 * @param position
	 */
	public void savePhoneMusicPosition(int position){
		sp.edit().putInt(PHONE_MUSIC_POSITION, Math.max(0, position)).commit();
	}
	/*
	 * 获取上次播放位置
	 */
	public int getPhoneMusicPosition() {
		return sp.getInt(PHONE_MUSIC_POSITION, -1);
	}
	/**
	 * 保存歌曲的播放时间
	 * @param duration
	 */
	public void savePhoneMusicCurrentDuration(int duration){
		sp.edit().putInt(PHONE_MUSIC_CURRENT_DURATION, Math.max(0, duration)).commit();
	}
	
	public int getPhoneMusicCurrentDuration() {
		return sp.getInt(PHONE_MUSIC_CURRENT_DURATION, 0);
	}
	
	public void saveShakeOption(int option) {
		sp.edit().putInt(SHAKE_OPTION, option).commit();
	}
	
	public int getShakeOption() {
		return sp.getInt(SHAKE_OPTION, R.id.rb_random_color);
	}
	
	public void setFirstEnterAlarm(boolean first) {
		sp.edit().putBoolean(FIRST_TIME_ENTER_ALARM, first).commit();
	}
	
	public boolean isFirstEnterAlarm(){
		return sp.getBoolean(FIRST_TIME_ENTER_ALARM, true);
	}
	
	public boolean isFirstLaunch() {
		return sp.getBoolean(FIRST_LAUNCH, true);
	}
	
	public void setFirstLaunch(boolean firstLaunch) {
		sp.edit().putBoolean(FIRST_LAUNCH, firstLaunch).commit();
	}
	
	public void setColor(Set<String> set)
	{
		sp.edit().putStringSet(COLORS, set).commit();
	}
	
	public Set<String> getColor()
	{
		return sp.getStringSet(COLORS, set);
	}
	
}