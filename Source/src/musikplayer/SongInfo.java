package musikplayer;

/**
 * Copyright (C) 2013 www.delight.im <info@delight.im>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class SongInfo {
	
	private String mArtist = "";
	private String mTitle = "";
	private String mAlbum = "";
	private int mYear = 0;
	private long mDuration = 0;
	
	public SongInfo() { }
	
	public String getArtist() {
		return mArtist;
	}
	
	public void setArtist(String artist) {
		artist = validateText(artist);
		if (artist.length() > mArtist.length()) {
			mArtist = artist;
		}
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		title = validateText(title);
		if (title.length() > mTitle.length()) {
			mTitle = title;
		}
	}
	
	public String getAlbum() {
		return mAlbum;
	}
	
	public void setAlbum(String album) {
		album = validateText(album);
		if (album.length() > mAlbum.length()) {
			mAlbum = album;
		}
	}
	
	public int getYear() {
		return mYear;
	}
	
	public void setYear(String yearStr) {
		int year;
		try {
			year = Integer.parseInt(yearStr);
		}
		catch (Exception e) {
			year = 0;
		}
		year = validateNumber(year);
		if (year > mYear) {
			mYear = year;
		}
	}
	
	public long getDuration() {
		return mDuration;
	}
	
	public void setDuration(long duration) {
		duration = validateNumber(duration);
		if (duration > mDuration) {
			mDuration = duration;
		}
	}
	
	protected String validateText(String text) {
		if (text == null) {
			return "";
		}
		else {
			return text.trim();
		}
	}
	
	protected int validateNumber(int number) {
		if (number < 0) {
			return 0;
		}
		else {
			return number;
		}
	}
	
	protected long validateNumber(long number) {
		if (number < 0) {
			return 0;
		}
		else {
			return number;
		}
	}

	public static SongInfo fromFile(String filePath) {
		final SongInfo out = new SongInfo();
		
		try {
			final Mp3File songData = new Mp3File(filePath);
			out.setDuration(songData.getLengthInSeconds());

			if (songData.hasId3v2Tag()) {
				ID3v2 songTags = songData.getId3v2Tag();
				out.setArtist(songTags.getArtist());
				out.setTitle(songTags.getTitle());
				out.setAlbum(songTags.getAlbum());
				out.setYear(songTags.getYear());
			}

			if (songData.hasId3v1Tag()) {
				ID3v1 songTags = songData.getId3v1Tag();
				out.setArtist(songTags.getArtist());
				out.setTitle(songTags.getTitle());
				out.setAlbum(songTags.getAlbum());
				out.setYear(songTags.getYear());
			}
		}
		catch (Exception e) { }
		
		return out;
	}

}
