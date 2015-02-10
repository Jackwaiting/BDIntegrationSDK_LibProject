package com.chipsguide.app.colorbluetoothlamp.v2.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chipsguide.app.colorbluetoothlamp.v2.R;
import com.chipsguide.app.colorbluetoothlamp.v2.utils.FormatHelper;
import com.chipsguide.app.colorbluetoothlamp.v2.view.TitleView;

public class SleepAssistantActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private TextView mShowTimeTextView;
	private ProgressBar mProgressBar;
	private TextView mConfirmationTextView;
	private RadioGroup mSleepTimeRadiogroup;
	private RadioButton mButton10ReadioButton;
	private RadioButton mButton20ReadioButton;
	private RadioButton mButton30ReadioButton;
	private RadioButton mButton60ReadioButton;
	private RadioButton mButton90ReadioButton;
	private TitleView mSleepTitleview;

	private AudioManager mAudioManager;
	private int current;//当前音量
	private int max;//最大音量
	private MyCount mCount;
	private int mColorTextDown;
	private int mColorTextNor;
	private int time = 10;
	private int TIME_GAP = 6;//分6段减小音量

	@Override
	public int getLayoutId()
	{
		return R.layout.activity_sleep_assistant;
	}

	@Override
	public void initBase()
	{
		mColorTextDown = getResources().getColor(R.color.color_blue);
		mColorTextNor = getResources().getColor(R.color.white);
		mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void initUI()
	{
		mSleepTitleview = (TitleView)this.findViewById(R.id.titleview_sleep);
		mShowTimeTextView = (TextView) this
				.findViewById(R.id.textview_show_time);
		mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		mConfirmationTextView = (TextView) this
				.findViewById(R.id.textview_confirmation);
		mSleepTimeRadiogroup = (RadioGroup) this
				.findViewById(R.id.radiogroup_slect_sleeptime);
		mButton10ReadioButton = (RadioButton) this
				.findViewById(R.id.readioButton_button_10);
		mButton20ReadioButton = (RadioButton) this
				.findViewById(R.id.readioButton_button_20);
		mButton30ReadioButton = (RadioButton) this
				.findViewById(R.id.readioButton_button_30);
		mButton60ReadioButton = (RadioButton) this
				.findViewById(R.id.readioButton_button_60);
		mButton90ReadioButton = (RadioButton) this
				.findViewById(R.id.readioButton_button_90);

		mSleepTimeRadiogroup.setOnCheckedChangeListener(this);
		// mProgressBar.seton
		mConfirmationTextView.setOnClickListener(this);
	}

	@Override
	public void initData()
	{
		mSleepTitleview.setRightBtnVisibility(false);
	}

	@Override
	public void initListener()
	{
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.textview_confirmation:
			mSleepTitleview.setLiftBtnVisibility(false);
			
			if (mCount != null)
			{
				mCount.cancel();
				mCount = null;
			}
			mCount = new MyCount(60000 * time, 1000);
			mShowTimeTextView.setText(FormatHelper
					.formatLongToTimeMinuteStr((long) time * 60000));
			mCount.start();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		switch (group.getId())
		{
		case R.id.radiogroup_slect_sleeptime:
			selectTime(checkedId);
			break;
		}
	}

	private void selectTime(int checkedId)
	{
		mButton10ReadioButton.setTextColor(mColorTextNor);
		mButton20ReadioButton.setTextColor(mColorTextNor);
		mButton30ReadioButton.setTextColor(mColorTextNor);
		mButton60ReadioButton.setTextColor(mColorTextNor);
		mButton90ReadioButton.setTextColor(mColorTextNor);
		switch (checkedId)
		{
		case R.id.readioButton_button_10:
			time = 10;
			mButton10ReadioButton.setTextColor(mColorTextDown);
			break;
		case R.id.readioButton_button_20:
			time = 20;
			mButton20ReadioButton.setTextColor(mColorTextDown);
			break;
		case R.id.readioButton_button_30:
			time = 30;
			mButton30ReadioButton.setTextColor(mColorTextDown);
			break;
		case R.id.readioButton_button_60:
			time = 60;
			mButton60ReadioButton.setTextColor(mColorTextDown);
			break;
		case R.id.readioButton_button_90:
			time = 90;
			mButton90ReadioButton.setTextColor(mColorTextDown);
			break;
		}
		mProgressBar.setProgress(0);
		mShowTimeTextView.setText(FormatHelper
				.formatLongToTimeMinuteStr((long) time * 60 * 1000));
	}
	
	private void setSleepSound(long millis)
	{
		int cl = (time*60)/TIME_GAP;
		for(int i =1 ;i <=TIME_GAP ; i++)
		{
			if((millis == cl*i))
			{
				flog.d("音量为：" + (current/TIME_GAP)*i + " i: " + i + " current: " + current);
				//如果分段的段数超过了音量最大可以加上这个，最大音量为15
				if(current < TIME_GAP && TIME_GAP < max)
				 {
					 current =  TIME_GAP;
				 }
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (current/TIME_GAP)*i, 0);
			}
		}
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish()
		{
			mCount.cancel();
			mShowTimeTextView.setText("88:88");
			mSleepTitleview.setLiftBtnVisibility(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			long millis = millisUntilFinished / 1000;
			double x = (time*60-(int) millis)/1.0;//预留小数点
			mProgressBar.setProgress((int)((x/(time*60))*100));
			mShowTimeTextView.setText(FormatHelper
					.formatLongToTimeMinuteStr(millisUntilFinished));
			 setSleepSound(millis);
		}

	}

}
