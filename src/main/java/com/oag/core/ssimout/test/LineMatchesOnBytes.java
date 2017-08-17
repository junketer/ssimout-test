package com.oag.core.ssimout.test;

public class LineMatchesOnBytes implements LineComparator {

	private final int from;
	private final int to;
	
	protected LineMatchesOnBytes(int from, int to) {
		this.from=from;
		this.to=to;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof String && arg1 instanceof String) {
			return compare((String) arg0, (String) arg1);
		}
		return -1;
	}

	@Override
	public int compare(String line1, String line2) {
		if (line1==null || line2==null) {
			return -1;
		}
		String tmp1 = line1;
		String tmp2 = line2;
		if (tmp1.length()> from && tmp1.length()>=to) {
			tmp1 = line1.substring(from, to);
		}
		if (tmp2.length() >= from && tmp2.length()>=to) {
			tmp2 = line2.substring(from, to);
		}
		return tmp1.compareTo(tmp2);
		
	}

}
