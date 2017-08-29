package com.hw.readertextselect;

import java.util.List;

public class BreakResult {

	public int ChartNums = 0;
	public Boolean IsFullLine = false;
	public List<ShowChar> showChars = null;

	public Boolean HasData() {
		return showChars != null && showChars.size() > 0;
	}
}
