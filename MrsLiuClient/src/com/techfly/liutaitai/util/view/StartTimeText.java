package com.techfly.liutaitai.util.view;

import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class StartTimeText extends TextView {
	private final int MSG_UPDATE_TIME = 123;
	private long MSG_TOTAL_TIME;
	public Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_TIME:
				setTime();
				break;

			default:
				break;
			}
		}

	};

	private void setTime() {
		if (MSG_TOTAL_TIME < 0) {
			MSG_TOTAL_TIME = 0;
		}
		Date date = new Date(MSG_TOTAL_TIME);
		long min = MSG_TOTAL_TIME / 1000 / 60;
		long hour = min / 60;
		if (min > 60) {
			this.setText(String.format("%1$d时%2$02d分%3$02d秒", hour,
					date.getMinutes(), date.getSeconds()));
		} else {
			this.setText(String.format("%1$02d分%2$02d秒", date.getMinutes(),
					date.getSeconds()));
		}
		MSG_TOTAL_TIME += 1000;
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					Message message = new Message();
					message.what = MSG_UPDATE_TIME;
					timeHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public StartTimeText(Context context) {
		super(context);
	}

	public StartTimeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		new Thread(runnable).start();

	}

	public void toStart(long time) {
		MSG_TOTAL_TIME = time;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void toFinishHandler() {
		Thread.currentThread().interrupt();
		timeHandler.removeCallbacks(runnable);
	}

}
