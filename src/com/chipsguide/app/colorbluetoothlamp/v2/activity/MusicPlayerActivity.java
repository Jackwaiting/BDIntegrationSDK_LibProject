package com.chipsguide.app.colorbluetoothlamp.v2.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chipsguide.app.colorbluetoothlamp.v2.R;
import com.chipsguide.app.colorbluetoothlamp.v2.adapter.SimpleMusicListAdapter;
import com.chipsguide.app.colorbluetoothlamp.v2.bean.Music;
import com.chipsguide.app.colorbluetoothlamp.v2.media.PlayListener;
import com.chipsguide.app.colorbluetoothlamp.v2.media.PlayUtil;
import com.chipsguide.app.colorbluetoothlamp.v2.media.PlayerManager;
import com.chipsguide.app.colorbluetoothlamp.v2.media.PlayerManager.PlayType;
import com.chipsguide.app.colorbluetoothlamp.v2.utils.PreferenceUtil;
import com.chipsguide.app.colorbluetoothlamp.v2.view.MusicProgressView;
import com.chipsguide.app.colorbluetoothlamp.v2.view.MusicProgressView.SimpleSeekArcChangeListener;
import com.chipsguide.app.colorbluetoothlamp.v2.view.MusicSpectrumView;
import com.chipsguide.app.colorbluetoothlamp.v2.view.TitleView;
import com.chipsguide.app.colorbluetoothlamp.v2.widget.CirclePageIndicator;
import com.chipsguide.app.colorbluetoothlamp.v2.widget.SeekArc;
import com.chipsguide.app.colorbluetoothlamp.v2.widget.SlidingLayer;
import com.platomix.platomixplayerlib.api.PlaybackMode;

public class MusicPlayerActivity extends BaseActivity {
	private PlayerManager playerManager;
	private int currentModeRes;
	private SimpleMusicListAdapter mAdapter;
	private int currentPosition;
	private boolean userClick, update;
	
	private TitleView titleView;
	private ImageView playBtn,playmodeBtn;
	private SlidingLayer playListLayer;
	private ListView playListLv;
	private MusicProgressView progressLayout;
	private MusicSpectrumView spectrumLayout;
	private TextView musicNameTv,artistTv;
	
	private List<View> views = new ArrayList<View>();
	
	@Override
	public void initBase() {
		playerManager = PlayerManager.getInstance(getApplicationContext());
		int index = PreferenceUtil.getIntance(getApplicationContext())
				.getPlayMode();
		PlayUtil.setCurrentModeIndex(index);
		PlaybackMode mode = PlayUtil.getModeWithIndex(index);
		currentModeRes = PlayUtil.getModeImgRes(mode);

		mAdapter = new SimpleMusicListAdapter(this);
	}

	@Override
	public void initUI() {
		initPlaylistLayer();
		initPagerView();
		titleView = (TitleView) findViewById(R.id.titleView);
		titleView.setOnClickListener(this);
		titleView.setRightBtnVisibility(false);
		musicNameTv = (TextView) findViewById(R.id.tv_music_name);
		artistTv = (TextView) findViewById(R.id.tv_artist);

		playBtn = (ImageView) findViewById(R.id.iv_play_state);
		playmodeBtn = (ImageView) findViewById(R.id.iv_play_mode);
		playmodeBtn.setImageResource(currentModeRes);
		initForType();
		updateUI(true);

	}
	
	private void initPagerView() {
		progressLayout = new MusicProgressView(this);
		progressLayout.setOnSeekArcChangeListener(new SimpleSeekArcChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {
				playerManager
				.seekTo((int) ((float) seekArc.getProgress() / 1000 * 100));
			}
		});
		spectrumLayout = new MusicSpectrumView(this);
		views.add(progressLayout);
		views.add(spectrumLayout);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyPagerAdapter());
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
		indicator.setViewPager(viewPager);
	}

	/**
	 * 播放列表界面
	 */
	private void initPlaylistLayer() {
		playListLayer = (SlidingLayer) findViewById(R.id.playlist_layer);
		playListLv = (ListView) findViewById(R.id.play_list);
		playListLv.setAdapter(mAdapter);
		mAdapter.setMusicList(playerManager.getMusicList());
		playListLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentPosition == position) {
					playerManager.toggle();
					mAdapter.setSelected(position, !playerManager.isPlaying());
				} else {
					userClick = true;
					currentPosition = position;
					mAdapter.setSelected(currentPosition, true);
					playerManager.skipTo(position);
				}
			}
		});
	}

	/**
	 * 根据播放类型初始化界面
	 */
	private void initForType() {
		PlayType type = PlayerManager.getPlayType();
		if (type != null && type == PlayType.Net) {
		} else if (type != null && type == PlayType.Bluz) {
		} else if (type == null) {
			if (playerManager.loadRecentPlay()) {
				initForType();
			}
		}
	}

	/**
	 * 改变播放按钮图片
	 */
	private void changePlaybtn() {
		int resId;
		if (playerManager.isPlaying()) {
			resId = R.drawable.selector_btn_play;
		} else {
			resId = R.drawable.selector_btn_pause;
		}
		playBtn.setImageResource(resId);
	}

	/**
	 * 切换歌曲，开始/暂停时更新ui
	 */
	private void updateUI(boolean force) {
		if (update && !force) {
			return;
		}
		update = true;
		Music currentMusic = playerManager.getCurrentMusic();
		currentPosition = playerManager.getCurrentPosition();
		changePlaybtn();
		if (currentMusic != null) {
			String title = currentMusic.getName();
			if (TextUtils.isEmpty(title)) {
				title = currentMusic.getName_en();
			}
			musicNameTv.setText(title);
			if(!TextUtils.isEmpty(currentMusic.getArtist())){
				artistTv.setText(currentMusic.getArtist());
			}
			titleView.setTitleText(title);
			progressLayout.updateMusicImage(currentMusic.getPicpath_l());
		}
		mAdapter.setSelected(currentPosition, true);
		// 不是用户点击，才滚动ListView
		if (!userClick) {
			playListLv.setSelection(currentPosition);
		}
	}


	@Override
	public void initData() {
	}

	@Override
	public void initListener() {
	}


	/**
	 * 播放器回调。此监听比Activity的生命周期要长，所以声明为静态内部类，防止内存泄露
	 */
	private static class MyPlayListener extends PlayListener {
		private WeakReference<MusicPlayerActivity> ref;

		public MyPlayListener(MusicPlayerActivity act) {
			ref = new WeakReference<MusicPlayerActivity>(act);
		}

		@Override
		public void onMusicStart(String musicSymbol) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
				act.mAdapter.setSelected(
						act.playerManager.getCurrentPosition(),
						true);
				act.playBtn
						.setImageResource(R.drawable.selector_btn_play);
				act.progressLayout.playStateChange(true);
			}
		}

		@Override
		public void onMusicPause(String musicSymbol) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
				act.mAdapter.setSelected(
						act.playerManager.getCurrentPosition(),
						false);
				act.playBtn
						.setImageResource(R.drawable.selector_btn_pause);
				act.progressLayout.playStateChange(false);
			}
		}

		@Override
		public void onMusicProgress(String musicSymbol, long duration,
				long currentDuration, int percent) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
				act.progressLayout.updateProgress(duration, currentDuration, percent);
				if(!act.progressLayout.isRotatingAnim()){
					act.progressLayout.playStateChange(act.playerManager.isPlaying());
				}
				if (!act.update) {
					act.updateUI(false);
				}
			}
		}

		@Override
		public void onMusicChange(String musicSymbol) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
				act.updateUI(true);
			}
		}

		@Override
		public void onMusicBuffering(String musicSymbol, int percent) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
			}
		}

		@Override
		public void onMusicError(String musicSymbol, int what, int extra) {
		}

		@Override
		public void onLoopModeChanged(int mode) {
			MusicPlayerActivity act = ref.get();
			if (act != null) {
				PlaybackMode playmode = PlayUtil.convertMode(mode);
				PlayUtil.setCurrentMode(playmode);
				int imgRes = PlayUtil.getModeImgRes(playmode);
				act.playmodeBtn.setImageResource(imgRes);
			}
		}

	}

	/**
	 * 改变播放模式
	 */
	public void changePlaybackMode() {
		int res = PlayUtil.nextModeRes();
		playmodeBtn.setImageResource(res);
		int textRes = PlayUtil.getCurrentModeTextRes();
		showToast(textRes);
		playerManager.changePlaymode(PlayUtil.getModeWithRes(res));
		PreferenceUtil.getIntance(getApplicationContext()).savePlayMode(
				PlayUtil.getCurrentModeIndex());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.iv_play_state:
			playerManager.toggle();
			break;
		case R.id.iv_next:
			playerManager.next();
			break;
		case R.id.iv_pre:
			playerManager.prev();
			break;
		case R.id.iv_play_mode:
			changePlaybackMode();
			break;
		case R.id.iv_playlist:
			showPlaylist();
			break;
		case R.id.hide_btn:
			playListLayer.closeLayer(true);
			break;
		}
	}

	private void showPlaylist() {
		mAdapter.setSelected(currentPosition, playerManager.isPlaying());
		if (playListLayer.isOpened()) {
			playListLayer.closeLayer(true);
		} else {
			playListLayer.openLayer(true);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_music_player;
	}

	@Override
	public void onBackPressed() {
		if (playListLayer.isOpened()) {
			playListLayer.closeLayer(true);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		update = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		playerManager.setPlayListener(new MyPlayListener(this),
				PlayerManager.getPlayType(), true);
	}
	
	private class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}


}
