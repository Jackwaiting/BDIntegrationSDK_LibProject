package com.chipsguide.app.colorbluetoothlamp.v2.brunton.frags;

import android.content.Context;

import com.chipsguide.app.colorbluetoothlamp.v2.brunton.R;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.adapter.SimpleMusicListAdapter;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.been.Music;
import com.chipsguide.app.colorbluetoothlamp.v2.brunton.media.PlayerManager.PlayType;

public class MyMusicFrag extends SimpleMusicFrag {
	public static final String TAG = "local";
	
	public static MyMusicFrag getInstance(Context context, String tag, SimpleMusicListAdapter adapter, OnItemSelectedListener listener){
		MyMusicFrag frag = new MyMusicFrag();
		frag.setFilterTag(tag);
		frag.setAdapter(adapter);
		frag.setOnItemSelectedListener(listener);
		return frag;
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.frag_my_music;
	}

	@Override
	public PlayType getPlayType() {
		return PlayType.Local;
	}
	
	@Override
	public String getFilter(Music music) {
		return music.getLocalPath();
	}

	@Override
	public void onLoadPlayList() {
		playerManager.loadLocalMusic(this, false);
	}

}