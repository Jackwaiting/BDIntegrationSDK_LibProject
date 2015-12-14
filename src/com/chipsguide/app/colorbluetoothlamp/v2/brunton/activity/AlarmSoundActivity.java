package com.chipsguide.app.colorbluetoothlamp.v2.brunton.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chipsguide.app.colorbluetoothlamp.v2.brunton.R;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.application.CustomApplication;

public class AlarmSoundActivity extends BaseActivity {
	public static final String EXTRA_SOUND_PATH = "sound_path";
	private String soundPath;
	private TextView soundNameTv;
	private View songLayout;
	
	@Override
	public int getLayoutId() {
		return R.layout.activity_alarm_sound;
	}

	@Override
	public void initBase() {
		soundPath = getIntent().getStringExtra(EXTRA_SOUND_PATH);
	}

	@Override
	public void initUI() {
		soundNameTv = (TextView) findViewById(R.id.tv_sound_name);
		songLayout = findViewById(R.id.song_layout);
		if(!TextUtils.isEmpty(soundPath)){
			String [] arr = soundPath.split("\\|");
			if(arr != null && arr.length > 1){
				songLayout.setVisibility(View.VISIBLE);
				soundNameTv.setText(arr[1]);
			}
		}
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initListener() {
		findViewById(R.id.text_silent).setOnClickListener(this);
		findViewById(R.id.text_select).setOnClickListener(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		CustomApplication.addActivity(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.text_silent:
			songLayout.setVisibility(View.INVISIBLE);
			soundPath = "";
			finish();
			break;
		case R.id.text_select:
			Intent intent = new Intent(this, AlarmSoundSelectActivity.class);
			intent.putExtra(EXTRA_SOUND_PATH, soundPath);
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode == 1){
				soundPath = data.getStringExtra(EXTRA_SOUND_PATH);
				if(!TextUtils.isEmpty(soundPath)){
					String [] arr = soundPath.split("\\|");
					if(arr != null && arr.length > 1){
						songLayout.setVisibility(View.VISIBLE);
						soundNameTv.setText(arr[1]);
						finish();
					}
				}
			}
		}
	}
	
	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_SOUND_PATH, soundPath);
		setResult(RESULT_OK, intent);
		super.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mSubject.deleteach(this);
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