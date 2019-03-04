package com.dm.util;

import java.util.regex.Pattern;

public class PatternSearch {
	public static final Pattern SEARCH_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
}
