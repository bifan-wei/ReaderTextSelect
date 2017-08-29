package com.hw.readertextselect;

import java.util.ArrayList;

import android.graphics.Paint;
import android.text.TextUtils;

public class TextBreakUtil {

	/**
	 *@param cs 
	 *@param medsurewidth
	 *@param textpadding
	 *@param paint
	 *@return 如果cs为空或者长度为0，返回null
	 *--------------------
	 *TODO
	 *--------------------
	 * author: huangwei
	 * 2017年5月4日下午3:31:03
	 */
	public static BreakResult BreakText(char[] cs, float medsurewidth, float textpadding, Paint paint) {	
		if(cs==null||cs.length==0){return null;}
		BreakResult breakResult = new BreakResult();		
		breakResult.showChars = new ArrayList<ShowChar>();
		float width = 0;

		for (int i = 0, size = cs.length; i < size; i++) {
			String mesasrustr = String.valueOf(cs[i]);
			float charwidth = paint.measureText(mesasrustr);
			
			if (width <= medsurewidth && (width + textpadding + charwidth) > medsurewidth) {
				breakResult.ChartNums = i;
				breakResult.IsFullLine = true;
				return breakResult;
			}

			ShowChar showChar = new ShowChar();
			showChar.chardata = cs[i];
			showChar.charWidth = charwidth;			
			breakResult.showChars.add(showChar);
			width += charwidth + textpadding;
		}

		breakResult.ChartNums = cs.length;
		return breakResult;
	}

	/**
	 *@param text
	 *@param medsurewidth
	 *@param textpadding
	 *@param paint
	 *@return 如果text为空，返回null
	 *--------------------
	 *TODO
	 *--------------------
	 * author: huangwei
	 * 2017年7月3日下午3:12:12
	 */
	public static BreakResult BreakText(String text, float medsurewidth, float textpadding, Paint paint) {
		if (TextUtils.isEmpty(text)) {
			int[] is = new int[2];
			is[0] = 0;
			is[1] = 0;
			return null;
		}
		return BreakText(text.toCharArray(), medsurewidth, textpadding, paint);

	}

	public static float MeasureText(String text, float textpadding, Paint paint) {
		if (TextUtils.isEmpty(text))
			return 0;
		char[] cs = text.toCharArray();
		float width = 0;
		for (int i = 0, size = cs.length; i < size; i++) {
			String mesasrustr = String.valueOf(cs[i]);
			float charwidth = paint.measureText(mesasrustr);
			width += charwidth + textpadding;
		}

		return width;
	}

	
}
