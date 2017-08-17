package com.oag.core.ssimout.test;

public class LineMatchesComparator implements LineComparator {

	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof String && arg1 instanceof String) {
			return compare((String) arg0, (String) arg1);
		}
		return -1;
	}

	public int compare(String line1, String line2) {
		return line1.compareTo(line2);
	}

}
