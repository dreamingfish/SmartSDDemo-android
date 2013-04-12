package com.aisino.smartsd;


public class Utils {
	public static String To_Hex(char cData[], int offset, int nlen) {
		// To_Hex
		int nSize = (nlen - offset);
		char[] finalhash = new char[nSize * 2];
		char[] hexval = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };

		for (int j = 0; j < nSize; j++) {
			finalhash[j * 2] = hexval[(int) ((cData[j + offset] >> 4) & 0xF)];
			finalhash[(j * 2) + 1] = hexval[(int) (cData[j + offset]) & 0x0F];
		}

		return String.valueOf(finalhash);
	}

	public static char[] subarray(char[] array, int offset, int len) {
		if (array.length < offset + len) {
			return null;
		}

		char[] a = new char[len];

		for (int i = 0; i < len; i++) {
			a[i] = array[offset + i];
		}

		return a;
	}

	public static boolean memcpy(char[] dst, int doff, char[] src, int soff,
			int len) {
		if (doff < 0 || soff < 0 || dst.length < len + doff
				|| src.length < len + soff) {
			return false;
		}

		for (int i = 0; i < len; i++) {
			dst[i + doff] = src[i + soff];
		}

		return true;
	}

	public static int swapWord(int a) {
		return ((a >> 24) | ((a >> 8) & 0x0000ff00) | ((a << 8) & 0x00ff0000) | ((a << 24) & 0xff000000));
	}

	public static char[] swapWordNew(char[] src) {
		return swapWordNew(src, 0, src.length);
	}

	public static char[] swapWordNew(char[] src, int offset, int len) {
		char[] ret = new char[len];
		for (int i = offset; i < offset + len; i += 4) {
			ret[i] = src[i + 3];
			ret[i + 1] = src[i + 2];
			ret[i + 2] = src[i + 1];
			ret[i + 3] = src[i];
		}
		return ret;
	}

	public static void swapWord(char[] src) {
		swapWord(src, 0, src.length);
	}

	public static void swapWord(char[] src, int offset, int len) {
		char tmp = 0;
		for (int i = offset; i < offset + len; i += 4) {
			tmp = src[i];
			src[i] = src[i + 3];
			src[i + 3] = tmp;
			tmp = src[i + 1];
			src[i + 1] = src[i + 2];
			src[i + 2] = tmp;
		}
	}
}
