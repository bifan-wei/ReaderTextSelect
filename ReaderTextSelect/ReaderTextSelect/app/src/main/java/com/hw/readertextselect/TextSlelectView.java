package com.hw.readertextselect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TextSlelectView extends View {

	String TextData = "jEh话说天下大势，分久必合，合久必分。周末七国分争，并入于秦。及秦灭之后，楚、汉分争，又并入于汉。汉朝自高祖斩白蛇而起义，一统天下，后来光武中兴，传至献帝，遂分为三国。推其致乱之由，殆始于桓、灵二帝。桓帝禁锢善类，崇信宦官。及桓帝崩，灵帝即位，大将军窦武、太傅陈蕃共相辅佐。时有宦官曹节等弄权，窦武、陈蕃谋诛之，机事不密，反为所害，中涓自此愈横"
			+

	"建宁二年四月望日，帝御温德殿。方升座，殿角狂风骤起。只见一条大青蛇，从梁上飞将下来，蟠于椅上。帝惊倒，左右急救入宫，百官俱奔避。须臾，蛇不见了。忽然大雷大雨，加以冰雹，落到半夜方止，坏却房屋无数。建宁四年二月，洛阳地震；又海水泛溢，沿海居民，尽被大浪卷入海中。光和元年，雌鸡化雄。六月朔，黑气十余丈，飞入温德殿中。秋七月，有虹现于玉堂；五原山岸，尽皆崩裂。种种不祥，非止一端。帝下诏问群臣以灾异之由，议郎蔡邕上疏，以为堕鸡化，乃妇寺干政之所致，言颇切直。帝览奏叹息，因起更衣。曹节在后窃视，悉宣告左右；遂以他事陷邕于罪，放归田里。后张让、赵忠、封、段、曹节、侯览、蹇硕、程旷、夏恽、郭胜十人朋比为奸，号为“十常侍”。帝尊信张让，呼为“阿父”。朝政日非，以致天下人心思乱，盗贼蜂起。";

	public TextSlelectView(Context context) {
		super(context);
		init();

	}

	public TextSlelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextSlelectView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private Paint mPaint = null;
	private Paint mTextSelectPaint = null;
	private Paint mBorderPointPaint = null;
	private int TextSelectColor = Color.parseColor("#77fadb08");
	private int BorderPointColor = Color.RED;

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(39);

		mTextSelectPaint = new Paint();
		mTextSelectPaint.setAntiAlias(true);
		mTextSelectPaint.setTextSize(19);
		mTextSelectPaint.setColor(TextSelectColor);

		mBorderPointPaint = new Paint();
		mBorderPointPaint.setAntiAlias(true);
		mBorderPointPaint.setTextSize(19);
		mBorderPointPaint.setColor(BorderPointColor);

		FontMetrics fontMetrics = mPaint.getFontMetrics();
		TextHeight = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent);

		setOnLongClickListener(mLongClickListener);

	}

	private OnLongClickListener mLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {

			if (mCurrentMode == Mode.Normal) {
				if (Down_X > 0 && Down_Y > 0) {// 说明还没释放，是长按事件
					mCurrentMode = Mode.PressSelectText;
					postInvalidate();
				}
			}
			return false;
		}
	};

	private float Tounch_X = 0, Tounch_Y = 0;
	private float Down_X = -1, Down_Y = -1;
	private Mode mCurrentMode = Mode.Normal;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Tounch_X = event.getX();
		Tounch_Y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Down_X = Tounch_X;
			Down_Y = Tounch_Y;

			if (mCurrentMode != Mode.Normal) {
				Boolean isTrySelectMove = CheckIfTrySelectMove(Down_X, Down_Y);

				if (!isTrySelectMove) {// 如果不是准备滑动选择文字，转变为正常模式，隐藏选择框
					mCurrentMode = Mode.Normal;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:

			if (mCurrentMode == Mode.SelectMoveForward) {

				if (CanMoveForward(event.getX(), event.getY())) {// 判断是否是向上移动

					Log.e("is CanMoveForward", "CanMoveForward");

					ShowChar firstselectchar = DetectPressShowChar(event.getX(), event.getY());
					if (firstselectchar != null) {
						FirstSelectShowChar = firstselectchar;
						invalidate();
					} else {
						Log.e("firstselectchar", "firstselectchar is null");
					}

				} else {
					Log.e("is CanMoveForward", "CanMoveForward");
				}

			} else if (mCurrentMode == Mode.SelectMoveBack) {

				if (CanMoveBack(event.getX(), event.getY())) {// 判断是否可以向下移动
					Log.e("CanMoveBack", "not CanMoveBack");

					ShowChar lastselectchar = DetectPressShowChar(event.getX(), event.getY());

					if (lastselectchar != null) {
						LastSelectShowChar = lastselectchar;
						invalidate();
					} else {
						Log.e("is lastselectchar", "lastselectchar is null");
					}

				} else {
					Log.e("is CanMoveBack", "not CanMoveBack");
				}
			}

			break;

		case MotionEvent.ACTION_UP:
			Release();

			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	private boolean CanMoveBack(float Tounchx, float Tounchy) {

		Path p = new Path();
		p.moveTo(FirstSelectShowChar.TopLeftPosition.x, FirstSelectShowChar.TopLeftPosition.y);
		p.lineTo(getWidth(), FirstSelectShowChar.TopLeftPosition.y);
		p.lineTo(getWidth(), getHeight());
		p.lineTo(0, getHeight());
		p.lineTo(0, FirstSelectShowChar.BottomLeftPosition.y);
		p.lineTo(FirstSelectShowChar.BottomLeftPosition.x, FirstSelectShowChar.BottomLeftPosition.y);
		p.lineTo(FirstSelectShowChar.TopLeftPosition.x, FirstSelectShowChar.TopLeftPosition.y);

		return computeRegion(p).contains((int) Tounchx, (int) Tounchy);
	}

	private boolean CanMoveForward(float Tounchx, float Tounchy) {

		Path p = new Path();
		p.moveTo(LastSelectShowChar.TopRightPosition.x, LastSelectShowChar.TopRightPosition.y);
		p.lineTo(getWidth(), LastSelectShowChar.TopRightPosition.y);
		p.lineTo(getWidth(), 0);
		p.lineTo(0, 0);
		p.lineTo(0, LastSelectShowChar.BottomRightPosition.y);
		p.lineTo(LastSelectShowChar.BottomRightPosition.x, LastSelectShowChar.BottomRightPosition.y);
		p.lineTo(LastSelectShowChar.TopRightPosition.x, LastSelectShowChar.TopRightPosition.y);

		return computeRegion(p).contains((int) Tounchx, (int) Tounchy);
	}

	private void Release() {
		Down_X = -1;// 释放
		Down_Y = -1;
	}

	private Boolean CheckIfTrySelectMove(float xposition, float yposition) {// 检测是否准备滑动选择文字
		if (FirstSelectShowChar == null || LastSelectShowChar == null) {
			return false;
		}

		float flx, fty, frx, fby;

		float hPadding = FirstSelectShowChar.charWidth;
		hPadding = hPadding < 10 ? 10 : hPadding;

		flx = FirstSelectShowChar.TopLeftPosition.x - hPadding;
		frx = FirstSelectShowChar.TopLeftPosition.x;

		fty = FirstSelectShowChar.TopLeftPosition.y;
		fby = FirstSelectShowChar.BottomLeftPosition.y;

		float llx, lty, lrx, lby;

		llx = LastSelectShowChar.BottomRightPosition.x;
		lrx = LastSelectShowChar.BottomRightPosition.x + hPadding;

		lty = LastSelectShowChar.TopRightPosition.y;
		lby = LastSelectShowChar.BottomRightPosition.y;

		if ((xposition >= flx && xposition <= frx) && (yposition >= fty && yposition <= fby)) {
			mCurrentMode = Mode.SelectMoveForward;
			return true;
		}

		if ((xposition >= llx && xposition <= lrx) && (yposition >= lty && yposition <= lby)) {
			mCurrentMode = Mode.SelectMoveBack;
			return true;
		}

		return false;

	}

	/** 
	 * 通过路径计算区域 
	 *  
	 * @param path 
	 *            路径对象 
	 * @return 路径的Region 
	 */
	private Region computeRegion(Path path) {
		Region region = new Region();
		RectF f = new RectF();
		path.computeBounds(f, true);
		region.setPath(path, new Region((int) f.left, (int) f.top, (int) f.right, (int) f.bottom));
		return region;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int viewwidth = getMeasuredWidth();
		int viewheight = getMeasuredHeight();

		initData(viewwidth, viewheight);
	}

	List<ShowLine> mLinseData = null;

	private void initData(int viewwidth, int viewheight) {
		if (mLinseData == null) {
			mLinseData = BreakText(viewwidth, viewheight);
		}

	}

	private List<ShowLine> BreakText(int viewwidth, int viewheight) {
		List<ShowLine> showLines = new ArrayList<ShowLine>();
		while (TextData.length() > 0) {
			BreakResult breakResult = TextBreakUtil.BreakText(TextData, viewwidth, 0, mPaint);

			if (breakResult != null && breakResult.HasData()) {
				ShowLine showLine = new ShowLine();
				showLine.CharsData = breakResult.showChars;
				showLines.add(showLine);

			} else {
				break;
			}

			TextData = TextData.substring(breakResult.ChartNums);

		}

		int index = 0;
		for (ShowLine l : showLines) {
			for (ShowChar c : l.CharsData) {
				c.Index = index++;
			}
		}
		return showLines;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		LineYPosition = TextHeight + LinePadding;

		for (ShowLine line : mLinseData) {
			DrawLineText(line, canvas);

		}

		if (mCurrentMode != Mode.Normal) {
			DrawSelectText(canvas);
		}
	}

	private Path mSelectTextPath = new Path();
	private ShowChar FirstSelectShowChar = null;
	private ShowChar LastSelectShowChar = null;

	private void DrawSelectText(Canvas canvas) {
		if (mCurrentMode == Mode.PressSelectText) {
			DrawPressSelectText(canvas);
		} else if (mCurrentMode == Mode.SelectMoveForward) {
			DrawMoveSelectText(canvas);
		} else if (mCurrentMode == Mode.SelectMoveBack) {
			DrawMoveSelectText(canvas);
		}
	}

	private List<ShowLine> mSelectLines = new ArrayList<ShowLine>();

	private void DrawMoveSelectText(Canvas canvas) {
		if (FirstSelectShowChar == null || LastSelectShowChar == null)
			return;
		GetSelectData();
		DrawSeletLines(canvas);
		DrawBorderPoint(canvas);
	}

	private void DrawSeletLines(Canvas canvas) {
		// DrawRectangleSeletLinesBg(canvas);
		DrawOaleSeletLinesBg(canvas);
	}

	private void DrawOaleSeletLinesBg(Canvas canvas) {// 绘制椭圆型的选中背景
		for (ShowLine l : mSelectLines) {
			Log.e("selectline", l.getLineData() + "");

			if (l.CharsData != null && l.CharsData.size() > 0) {
				

				ShowChar fistchar = l.CharsData.get(0);
				ShowChar lastchar = l.CharsData.get(l.CharsData.size() - 1);

				float fw = fistchar.charWidth;
				float lw = lastchar.charWidth;

				RectF rect = new RectF(fistchar.TopLeftPosition.x, fistchar.TopLeftPosition.y,
						lastchar.TopRightPosition.x, lastchar.BottomRightPosition.y);
				
				canvas.drawRoundRect(rect, fw / 2,
						 TextHeight / 2, mTextSelectPaint);

			}
		}
	}

	private void DrawRectangleSeletLinesBg(Canvas canvas) {
		for (ShowLine l : mSelectLines) {
			Log.e("selectline", l.getLineData() + "");

			if (l.CharsData != null && l.CharsData.size() > 0) {
				mSelectTextPath.reset();

				ShowChar fistchar = l.CharsData.get(0);
				ShowChar lastchar = l.CharsData.get(l.CharsData.size() - 1);

				mSelectTextPath.moveTo(fistchar.TopLeftPosition.x, fistchar.TopLeftPosition.y);
				mSelectTextPath.lineTo(lastchar.TopRightPosition.x, lastchar.TopRightPosition.y);
				mSelectTextPath.lineTo(lastchar.BottomRightPosition.x, lastchar.BottomRightPosition.y);
				mSelectTextPath.lineTo(fistchar.BottomLeftPosition.x, fistchar.BottomLeftPosition.y);
				mSelectTextPath.lineTo(fistchar.TopLeftPosition.x, fistchar.TopLeftPosition.y);

				canvas.drawPath(mSelectTextPath, mTextSelectPaint);
			}
		}
	}

	private void GetSelectData() {

		Boolean Started = false;
		Boolean Ended = false;

		mSelectLines.clear();

		// 找到选择的字符数据，转化为选择的行，然后将行选择背景画出来
		for (ShowLine l : mLinseData) {

			ShowLine selectline = new ShowLine();
			selectline.CharsData = new ArrayList<ShowChar>();

			for (ShowChar c : l.CharsData) {

				if (!Started) {
					if (c.Index == FirstSelectShowChar.Index) {
						Started = true;
						selectline.CharsData.add(c);
						if (c.Index == LastSelectShowChar.Index) {
							Ended = true;
							break;
						}
					}
				} else {

					if (c.Index == LastSelectShowChar.Index) {
						Ended = true;
						if (!selectline.CharsData.contains(c)) {
							selectline.CharsData.add(c);
						}
						break;
					} else {
						selectline.CharsData.add(c);
					}
				}
			}

			mSelectLines.add(selectline);

			if (Started && Ended) {
				break;
			}
		}
	}

	private void DrawPressSelectText(Canvas canvas) {
		ShowChar p = DetectPressShowChar(Down_X, Down_Y);

		if (p != null) {// 找到了选择的字符

			FirstSelectShowChar = LastSelectShowChar = p;
			mSelectTextPath.reset();
			mSelectTextPath.moveTo(p.TopLeftPosition.x, p.TopLeftPosition.y);
			mSelectTextPath.lineTo(p.TopRightPosition.x, p.TopRightPosition.y);
			mSelectTextPath.lineTo(p.BottomRightPosition.x, p.BottomRightPosition.y);
			mSelectTextPath.lineTo(p.BottomLeftPosition.x, p.BottomLeftPosition.y);
			canvas.drawPath(mSelectTextPath, mTextSelectPaint);

			DrawBorderPoint(canvas);

		}
	}

	private float BorderPointradius = 10;

	/**
	 *@param canvas
	 *--------------------
	 *TODO 绘制按着移动时的边界点
	 *--------------------
	 * author: huangwei
	 * 2017年7月4日下午3:01:41
	 */
	private void DrawBorderPoint(Canvas canvas) {
		if (FirstSelectShowChar != null && LastSelectShowChar != null) {
			 DrawPoint(canvas);
			//DrawRectangle(canvas);
		}

	}

	private Path BorderPath = new Path();

	private void DrawRectangle(Canvas canvas) {
		float Padding = 0;

		canvas.drawLine(FirstSelectShowChar.TopLeftPosition.x - Padding,
				FirstSelectShowChar.TopLeftPosition.y - Padding, FirstSelectShowChar.BottomLeftPosition.x - Padding,
				FirstSelectShowChar.BottomLeftPosition.y, mBorderPointPaint);

		canvas.drawLine(LastSelectShowChar.BottomRightPosition.x + Padding,
				LastSelectShowChar.BottomRightPosition.y + Padding, LastSelectShowChar.TopRightPosition.x + Padding,
				LastSelectShowChar.TopRightPosition.y, mBorderPointPaint);

		// mBorderPointPaint.setColor(Color.parseColor("#ff0000"));

		float hPadding = 25;
		float hPadding1 = 10;

		BorderPath.reset();

		BorderPath.moveTo(FirstSelectShowChar.TopLeftPosition.x - hPadding, FirstSelectShowChar.TopLeftPosition.y);
		BorderPath.lineTo(FirstSelectShowChar.TopLeftPosition.x - hPadding1, FirstSelectShowChar.TopLeftPosition.y);
		BorderPath.lineTo(FirstSelectShowChar.TopLeftPosition.x,
				FirstSelectShowChar.TopLeftPosition.y + TextHeight / 2);
		BorderPath.lineTo(FirstSelectShowChar.TopLeftPosition.x - hPadding1,
				FirstSelectShowChar.TopLeftPosition.y + TextHeight);
		BorderPath.lineTo(FirstSelectShowChar.TopLeftPosition.x - hPadding,
				FirstSelectShowChar.TopLeftPosition.y + TextHeight);
		BorderPath.lineTo(FirstSelectShowChar.TopLeftPosition.x - hPadding, FirstSelectShowChar.TopLeftPosition.y);

		canvas.drawPath(BorderPath, mBorderPointPaint);

		BorderPath.reset();

		BorderPath.moveTo(LastSelectShowChar.TopRightPosition.x + hPadding1, LastSelectShowChar.TopRightPosition.y);
		BorderPath.lineTo(LastSelectShowChar.TopRightPosition.x + hPadding, LastSelectShowChar.TopRightPosition.y);
		BorderPath.lineTo(LastSelectShowChar.BottomRightPosition.x + hPadding,
				LastSelectShowChar.BottomRightPosition.y);
		BorderPath.lineTo(LastSelectShowChar.BottomRightPosition.x + hPadding1,
				LastSelectShowChar.BottomRightPosition.y);
		BorderPath.lineTo(LastSelectShowChar.BottomRightPosition.x,
				LastSelectShowChar.TopRightPosition.y + TextHeight / 2);
		BorderPath.lineTo(LastSelectShowChar.BottomRightPosition.x + hPadding1, LastSelectShowChar.TopRightPosition.y);

		canvas.drawPath(BorderPath, mBorderPointPaint);

		// mBorderPointPaint.setColor(BorderPointColor);
	}

	private void DrawPoint(Canvas canvas) {
		float Padding = 0;

		canvas.drawCircle(FirstSelectShowChar.TopLeftPosition.x - Padding,
				FirstSelectShowChar.TopLeftPosition.y - Padding, BorderPointradius, mBorderPointPaint);

		canvas.drawCircle(LastSelectShowChar.BottomRightPosition.x + Padding,
				LastSelectShowChar.BottomRightPosition.y + Padding, BorderPointradius, mBorderPointPaint);

		canvas.drawLine(FirstSelectShowChar.TopLeftPosition.x - Padding,
				FirstSelectShowChar.TopLeftPosition.y - Padding, FirstSelectShowChar.BottomLeftPosition.x - Padding,
				FirstSelectShowChar.BottomLeftPosition.y, mBorderPointPaint);

		canvas.drawLine(LastSelectShowChar.BottomRightPosition.x + Padding,
				LastSelectShowChar.BottomRightPosition.y + Padding, LastSelectShowChar.TopRightPosition.x + Padding,
				LastSelectShowChar.TopRightPosition.y, mBorderPointPaint);
	}

	/**
	 *@param down_X2
	 *@param down_Y2
	 *@return
	 *--------------------
	 *TODO 检测获取按压坐标所在位置的字符，没有的话返回null
	 *--------------------
	 * author: huangwei
	 * 2017年7月4日上午10:23:19
	 */
	private ShowChar DetectPressShowChar(float down_X2, float down_Y2) {

		for (ShowLine l : mLinseData) {
			for (ShowChar c : l.CharsData) {
				if (down_Y2 > c.BottomLeftPosition.y) {
					break;// 说明是在下一行
				}
				if (down_X2 >= c.BottomLeftPosition.x && down_X2 <= c.BottomRightPosition.x) {
					return c;
				}

			}
		}

		return null;
	}

	private int LinePadding = 30;
	private float LineYPosition = 0;
	private float TextHeight = 0;

	private void DrawLineText(ShowLine line, Canvas canvas) {
		canvas.drawText(line.getLineData(), 0, LineYPosition, mPaint);
		//canvas.drawLine(0f, LineYPosition, 680f, LineYPosition, mTextSelectPaint);

		float leftposition = 0;
		float rightposition = 0;
		float bottomposition = LineYPosition + mPaint.getFontMetrics().descent;

		for (ShowChar c : line.CharsData) {
			rightposition = leftposition + c.charWidth;
			Point tlp = new Point();
			c.TopLeftPosition = tlp;
			tlp.x = (int) leftposition;
			tlp.y = (int) (bottomposition - TextHeight);

			Point blp = new Point();
			c.BottomLeftPosition = blp;
			blp.x = (int) leftposition;
			blp.y = (int) bottomposition;

			Point trp = new Point();
			c.TopRightPosition = trp;
			trp.x = (int) rightposition;
			trp.y = (int) (bottomposition - TextHeight);

			Point brp = new Point();
			c.BottomRightPosition = brp;
			brp.x = (int) rightposition;
			brp.y = (int) bottomposition;

			leftposition = rightposition;

		}
		LineYPosition = LineYPosition + TextHeight + LinePadding;
	}

	private enum Mode {
		Normal, PressSelectText, SelectMoveForward, SelectMoveBack
	}

}
