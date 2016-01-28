package musikplayer;

/*
 * Copyright (c) delight.im <info@delight.im>
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.player.Player;

public class Sequencer {
	
	private InputStream is = null;
	private File f = null;
	private Player p = null;
	
	public Sequencer() {
		// do something
	}
	
	public void stop() {
		if (p != null) {
			p.close();
			p = null;
		}
		try {
			if (is != null) {
				is.close();
				is = null;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		is = null;
		f = null;
	}
	
	public void play(String filePath) {
		f = new File(filePath);
		try {
			if (f.exists()) {
				is = new FileInputStream(f);
			}
		}
		catch (Exception e) {
			is = null;
		}
		try {
			if (is != null) {
				p = new Player(is);
			}
		}
		catch (Exception e) {
			p = null;
		}
		if (p != null) {
			try {
				if (is != null) {
					p.play();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
