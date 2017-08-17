package com.oag.core.ssimout.test;

import java.util.Comparator;

public interface LineComparator extends Comparator {
	int compare(String line1, String line2);
}
