package com.hw.readertextselect;

import android.graphics.Point;

public class ShowChar {
	
	public char chardata ;
	
	public Boolean Selected =false;
	
	public Point TopLeftPosition = null;
	public Point TopRightPosition = null;
	public Point BottomLeftPosition = null;
	public Point BottomRightPosition = null;
	
	public float charWidth = 0;
	public int Index = 0;
	
	@Override
	public String toString() {
		return "ShowChar [chardata=" + chardata + ", Selected=" + Selected + ", TopLeftPosition=" + TopLeftPosition
				+ ", TopRightPosition=" + TopRightPosition + ", BottomLeftPosition=" + BottomLeftPosition
				+ ", BottomRightPosition=" + BottomRightPosition + ", charWidth=" + charWidth + ", Index=" + Index
				+ "]";
	}

	

	
	
	

}
