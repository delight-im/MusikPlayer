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
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5 {
	
	private static final String HASH_ALGORITHM = "MD5";
	private static final int BUFFER_LENGTH = 1024;

	private static byte[] createChecksum(File f) throws Exception {
		final InputStream fis = new FileInputStream(f);
		final byte[] buffer = new byte[BUFFER_LENGTH];
		final MessageDigest complete = MessageDigest.getInstance(HASH_ALGORITHM);

		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		}
		while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	private static String getMD5Checksum(File f) {
		byte[] b;
		try {
			b = createChecksum(f);
		}
		catch (Exception e) {
			e.printStackTrace();
			b = null;
		}
		if (b != null) {
			String result = "";
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}
			return result;
		}
		else {
			return "";
		}
	}

	public static String get(File f) {
		try {
			return getMD5Checksum(f);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
