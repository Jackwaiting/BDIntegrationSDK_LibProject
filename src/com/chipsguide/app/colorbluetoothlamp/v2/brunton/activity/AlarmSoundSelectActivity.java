package com.chipsguide.app.colorbluetoothlamp.v2.brunton.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chipsguide.app.colorbluetoothlamp.v2.brunton.R;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.adapter.SelectMusicListAdapter;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.application.CustomApplication;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.been.Music;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.frags.SelectMusicFrag;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.frags.SimpleMusicFrag;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.frags.TFCardMusicFrag;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.frags.SimpleMusicFrag.OnItemSelectedListener;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.media.PlayerManager.PlayType;

public class AlarmSoundSelectActivity extends BaseActivity implements
		OnPageChangeListener, OnCheckedChangeListener ,OnItemSelectedListener{
	private boolean tf;
	private MyPagerAdapter adapter;
	private ViewPager viewPager;
	private RadioGroup topNavRg;
	private String soundPath;

	@Override
	public int getLayoutId() {
		return R.layout.activity_alarm_sound_select;
	}

	@Override
	public void initBase() {
		CustomApplication.addActivity(this);
		//tf = BluetoothDeviceManagerProxy.getInstance(this).isPlugTFCard();
		soundPath = getIntent().getStringExtra(AlarmSoundActivity.EXTRA_SOUND_PATH);
	}

	@Override
	public void initUI() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		findViewById(R.id.rg_top_nav).setVisibility(
				tf ? View.VISIBLE : View.GONE);
		String type = "";
		String tag = "";
		if(soundPath != null){
			String [] arr = soundPath.split("\\|");
			if(arr != null && arr.length > 1){
				type = arr[0];
				if(type.equals(PlayType.Local.name())){
					tag = arr[2];
				}else{
					tag = arr[1];
				}
			}
		}
		SelectMusicFrag selectMusicFrag = SelectMusicFrag.getInstance(this, tag, this);
		fragments.add(selectMusicFrag);
		if (tf) {
			TFCardMusicFrag cardMusicFrag = TFCardMusicFrag.getInstance(this, tag, new SelectMusicListAdapter(this), this);
			fragments.add(cardMusicFrag);
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		adapter.setList(fragments);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		topNavRg = (RadioGroup) findViewById(R.id.rg_top_nav);
		topNavRg.setOnCheckedChangeListener(this);
		topNavRg.check(R.id.rb_my_music);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initListener() {
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		CustomApplication.addActivity(this);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments = new ArrayList<Fragment>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setList(List<Fragment> list) {
			if (list == null) {
				return;
			}
			fragments = list;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int posi) {
		topNavRg.check(posi == 0 ? R.id.rb_my_music : R.id.rb_tf_card_music);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_my_music:
			viewPager.setCurrentItem(0);
			break;
		case R.id.rb_tf_card_music:
			viewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra(AlarmSoundActivity.EXTRA_SOUND_PATH, soundPath);
		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onItemSelected(SimpleMusicFrag frag, Music music, int position) {
		switch (frag.getPlayType()) {
		case Local:
			soundPath = PlayType.Local.name() + "|" + music.getName() + "|" + music.getLocalPath();
			finish();
			break;
		case Bluz:
			soundPath = PlayType.Bluz.name() + "|" + music.getName() + "|" + position;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void updateConnectState()
	{
	}

	@Override
	public void updateAlarm(int state)
	{
		if(CustomApplication.getActivity() == this)
		{
			if(state == 1)
			{
				createAlarmToast();
			}else if(state == 0)
			{
				dismissAlarmDialog();
			}else
			{
				dismissAlarmDialog();
			}
		}
	}
}