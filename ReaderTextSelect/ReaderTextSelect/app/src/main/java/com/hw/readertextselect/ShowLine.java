package com.hw.readertextselect;

import java.util.List;

public class ShowLine {
	public List<ShowChar> CharsData = null;

	@Override
	public String toString() {
		return "ShowLine [Linedata=" + getLineData() + "]";
	}
    
	public String getLineData(){
		String linedata = "";	
		if(CharsData==null||CharsData.size()==0) return linedata;
		for(ShowChar c:CharsData){
			linedata = linedata+c.chardata;
		}
		return linedata;
	}
}
