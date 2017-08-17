package com.oag.core.ssimout.test;

import java.io.OutputStream;

public class LineUtil {

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	public static final char T1 = "1".charAt(0);
	public static final char T2 = "2".charAt(0);
	public static final char T3 = "3".charAt(0);
	public static final char T4 = "4".charAt(0);
	public static final char T5 = "5".charAt(0);

	static void compareAndPrint(OutputStream os, String line1, String line2) {
		int line1Len = 0;
		int line2Len = 0;

		if (line1 != null) {
			line1Len = line1.length();
		}
		if (line2 != null) {
			line2Len = line2.length();
		}
		int minLength = line1Len < line2Len ? line1Len : line2Len;
		int[] mask = new int[minLength];

		if (line1Len > 0) {
			for (int i = 0; i < minLength; i++) {

				if (line1.charAt(i) != line2.charAt(i)) {
					mask[i] = 0;
					System.out.print(ANSI_RED);
					System.out.print(line1.charAt(i));
					System.out.print(ANSI_RESET);
				} else {
					mask[i] = 1;
					System.out.print(line1.charAt(i));
				}
			}
		}

		System.out.println();
		if (line2Len > 0) {
			for (int i = 0; i < minLength; i++) {
				if (mask[i] == 0) {
					System.out.print(ANSI_RED);
					System.out.print(line2.charAt(i));
					System.out.print(ANSI_RESET);

				} else {
					System.out.print(line2.charAt(i));
				}
			}
		}
		System.out.println();
	}
}
