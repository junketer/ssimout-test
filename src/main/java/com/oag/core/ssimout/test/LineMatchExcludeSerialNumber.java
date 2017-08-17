package com.oag.core.ssimout.test;

public class LineMatchExcludeSerialNumber implements LineComparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof String && arg1 instanceof String) {
			return compare((String) arg0, (String) arg1);
		}
		return -1;
	}

	@Override
	public int compare(String line1, String line2) {
		String tmp1 = line1;
		String tmp2 = line2;
		if (tmp1.length()>=195) {
			tmp1 = line1.substring(0, 195);
		}
		if (tmp2.length()>=195) {
			tmp2 = line2.substring(0, 195);
		}
		return tmp1.compareTo(tmp2);
		
	}

}
