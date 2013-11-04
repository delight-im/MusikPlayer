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

public class Song {
	
    public static final String[] BEWERTUNG_VALID_VALUES = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private String mInterpret = "";
    private String mSongtitel = "";
    private String mFileName = "";
    private String mGenre = "";
    private String mAlbum = "";
    private String mTanz = "";
    private int mJahr = 0;
    private long mDauerInSekunden = 0;
    private String mBewertung = "0";
    private boolean mSelected = true;
    
    public Song(String interpret, String songtitel, String fileName, String genre, String album, String tanz, int jahr, long dauerInSekunden, String bewertung) {
    	mInterpret = interpret.trim().replace("/", "");
    	mSongtitel = songtitel.trim().replace("/", "");
    	mFileName = fileName.trim().replace("/", "");
    	mGenre = genre.trim().replace("/", "");
    	mAlbum = album.trim().replace("/", "");
    	mTanz = tanz.trim().replace("/", "");
    	if (jahr >= 0 && jahr < 3000) {
    		mJahr = jahr;
    	}
    	if (dauerInSekunden >= 0 && dauerInSekunden < 86400) {
    		mDauerInSekunden = dauerInSekunden;
    	}
    	if (isBewertungValid(bewertung)) {
    		mBewertung = bewertung;
    	}
    }
    
    public static boolean isBewertungValid(final String bewertung) {
    	for (int i = 0; i < BEWERTUNG_VALID_VALUES.length; i++) {
    		if (BEWERTUNG_VALID_VALUES[i].equals(bewertung)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public String getDataCSV() {
    	return mInterpret+"/"+mSongtitel+"/"+mFileName+"/"+String.valueOf(mDauerInSekunden)+"/"+mGenre+"/"+mAlbum+"/"+String.valueOf(mBewertung)+"/"+String.valueOf(mJahr)+"/"+mTanz;
    }
    
    public String getInterpret() {
    	return mInterpret;
    }
    
    public void setInterpret(String interpret) {
    	mInterpret = interpret.trim().replace("/", "");
    }
    
    public String getGenre() {
    	return mGenre;
    }
    
    public void setGenre(String genre) {
    	mGenre = genre.trim().replace("/", "");
    }
    
    public String getAlbum() {
    	return mAlbum;
    }
    
    public void setAlbum(String album) {
    	mAlbum = album.trim().replace("/", "");
    }
    
    public String getSongtitel() {
    	return mSongtitel;
    }
    
    public void setSongtitel(String songtitel) {
    	mSongtitel = songtitel.trim().replace("/", "");
    }
    
    public String getTanz() {
    	return mTanz;
    }
    
    public void setTanz(String tanz) {
    	mTanz = tanz.trim().replace("/", "");
    }
    
    public String getFileName() {
    	return mFileName;
    }
    
    public int getJahr() {
    	return mJahr;
    }
    
    public void setJahr(int jahr) {
    	if (jahr >= 0 && jahr < 3000) {
    		mJahr = jahr;
    	}
    }
    
    public long getDauerInSekunden() {
    	return mDauerInSekunden;
    }
    
    public void setDauerInSekunden(long dauerInSekunden) {
    	if (dauerInSekunden >= 0 && dauerInSekunden < 86400) {
    		mDauerInSekunden = dauerInSekunden;
    	}
    }
    
    public String getBewertung() {
    	return mBewertung;
    }
    
    public void setBewertung(String bewertung) {
    	if (isBewertungValid(bewertung)) {
    		mBewertung = bewertung;
    	}
    }
    
    public boolean getSelected() {
    	return mSelected;
    }

    public void setSelected(boolean selected) {
    	mSelected = selected;
    }

}