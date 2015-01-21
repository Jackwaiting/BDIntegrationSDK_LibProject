package com.chipsguide.app.colorbluetoothlamp.v2.adapter;

import java.util.List;

import android.widget.BaseAdapter;

import com.chipsguide.app.colorbluetoothlamp.v2.bean.Music;

public abstract class IMusicListAdapter extends BaseAdapter {
	
	public abstract void setMusicList(List<Music> list);
	
	public abstract List<Music> getMusicList();
	
	public abstract void setSelected(int position);
}
