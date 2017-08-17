package com.oag.core.ssimout.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompositeComparator implements LineComparator {

	List<LineComparator> componentComparators = new ArrayList<LineComparator>(10);
	
	CompositeComparator(Collection<LineComparator> components) {
		super();
		componentComparators.addAll(components);
		
	}
	public int compare(Object o1, Object o2) {
		if (o1 instanceof String && o2 instanceof String) {
			return compare((String)o1, (String)o2);
		}
		return -1;
	}

	public int compare(String line1, String line2) {
		int rc=0;

		for (LineComparator lc: componentComparators) {
			rc=lc.compare(line1, line2);
			if (rc!=0) {
				break;
			}
		}
		return rc;
	}

}
