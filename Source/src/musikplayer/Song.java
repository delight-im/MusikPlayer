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
	
    private String mInterpret = "";
    private String mSongtitel = "";
    private String mFileName = "";
    private String mGenre = "";
    private String mAlbum = "";
    private String mTanz = "";
    private int mJahr = 0;
    private int mDauerInSekunden = 0;
    private int mBewertung = 0;
    private boolean mSelected = true;
    
    public Song(String interpret, String songtitel, String fileName, String genre, String album, String tanz, int jahr, int dauerInSekunden, int bewertung) {
    	mInterpret = interpret.trim().replace("/", "");
    	mSongtitel = songtitel.trim().replace("/", "");
    	mFileName = fileName.trim().replace("/", "");
    	mGenre = genre.trim().replace("/", "");
    	mAlbum = album.trim().replace("/", "");
    	mTanz = tanz.trim().replace("/", "");
    	if (jahr > 0 && jahr < 3000) {
    		mJahr = jahr;
    	}
    	if (dauerInSekunden > 0 && dauerInSekunden < 86400) {
    		mDauerInSekunden = dauerInSekunden;
    	}
    	if (bewertung >= 1 && bewertung <= 6) {
    		mBewertung = bewertung;
    	}
    }
    
    public String getDataCSV() {
    	return mInterpret+"/"+mSongtitel+"/"+mFileName+"/"+"0"+"/"+mGenre+"/"+mAlbum+"/"+String.valueOf(mBewertung)+"/"+String.valueOf(mJahr)+"/"+mTanz;
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
    	if (jahr > 0 && jahr < 3000) {
    		mJahr = jahr;
    	}
    }
    
    public int getDauerInSekunden() {
    	return mDauerInSekunden;
    }
    
    public void setDauerInSekunden(int dauerInSekunden) {
    	if (dauerInSekunden > 0 && dauerInSekunden < 86400) {
    		mJahr = dauerInSekunden;
    	}
    }
    
    public int getBewertung() {
    	return mBewertung;
    }
    
    public void setBewertung(int bewertung) {
    	if (bewertung >= 1 && bewertung <= 6) {
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