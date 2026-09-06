package com.example.standbyclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StandbyClockView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Locale locale = Locale.TAIWAN;
    private boolean charging = false;
    private boolean running = false;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            invalidate();
            if (running) handler.postDelayed(this, 1000);
        }
    };

    public StandbyClockView(Context context) {
        super(context);
        setBackgroundColor(Color.rgb(4,4,5));
        setKeepScreenOn(false);
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
        invalidate();
    }

    public void start() {
        if (!running) {
            running = true;
            handler.post(ticker);
        }
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(ticker);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float pad = Math.max(22f, Math.min(w,h) * 0.035f);
        float gap = pad * 0.65f;
        float leftW = (w - pad*2 - gap) * 0.57f;
        float rightW = (w - pad*2 - gap) - leftW;

        RectF left = new RectF(pad, pad, pad + leftW, h-pad);
        RectF right = new RectF(left.right + gap, pad, left.right + gap + rightW, h-pad);

        drawPanel(canvas, left);
        drawPanel(canvas, right);
        drawClock(canvas, left);
        drawCalendar(canvas, right);
    }

    private void drawPanel(Canvas c, RectF r) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(20,20,23));
        c.drawRoundRect(r, 38, 38, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.5f);
        paint.setColor(Color.argb(28,255,255,255));
        c.drawRoundRect(r, 38, 38, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawClock(Canvas c, RectF r) {
        Date now = new Date();
        String hm = new SimpleDateFormat("HH:mm", locale).format(now);
        String sec = new SimpleDateFormat("ss", locale).format(now);
        String date = new SimpleDateFormat("M月d日 EEEE", locale).format(now);
        String ymd = new SimpleDateFormat("yyyy年 · M月", locale).format(now);

        float x = r.left + r.width()*0.08f;
        float top = r.top + r.height()*0.09f;

        paint.setTypeface(android.graphics.Typeface.create("sans", android.graphics.Typeface.BOLD));
        paint.setTextSize(r.height()*0.037f);
        paint.setColor(Color.rgb(145,145,150));
        c.drawText(greeting(), x, top, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(charging ? Color.rgb(48,209,88) : Color.rgb(145,145,150));
        c.drawText(charging ? "⚡ 充電中 · 螢幕常亮" : "未充電", r.right-r.width()*0.08f, top, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        float timeY = r.top + r.height()*0.56f;
        paint.setTypeface(android.graphics.Typeface.create("sans", android.graphics.Typeface.BOLD));
        paint.setTextSize(Math.min(r.width()*0.26f, r.height()*0.31f));
        paint.setColor(Color.rgb(245,245,247));
        c.drawText(hm, x, timeY, paint);

        float hmWidth = paint.measureText(hm);
        paint.setTextSize(Math.min(r.width()*0.06f, r.height()*0.085f));
        paint.setColor(Color.rgb(255,69,58));
        c.drawText(sec, x + hmWidth + r.width()*0.025f, timeY-r.height()*0.12f, paint);

        float dateY = r.bottom - r.height()*0.14f;
        paint.setTextSize(r.height()*0.065f);
        paint.setColor(Color.WHITE);
        c.drawText(date, x, dateY, paint);

        paint.setTextSize(r.height()*0.032f);
        paint.setColor(Color.rgb(142,142,147));
        c.drawText(ymd, x, dateY + r.height()*0.055f, paint);
    }

    private String greeting() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h < 5) return "夜深了";
        if (h < 11) return "早安";
        if (h < 14) return "午安";
        if (h < 18) return "下午好";
        return "晚安";
    }

    private void drawCalendar(Canvas c, RectF r) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int today = cal.get(Calendar.DAY_OF_MONTH);

        float x = r.left + r.width()*0.08f;
        float headerY = r.top + r.height()*0.13f;
        paint.setTypeface(android.graphics.Typeface.create("sans", android.graphics.Typeface.BOLD));
        paint.setTextSize(r.height()*0.075f);
        paint.setColor(Color.WHITE);
        c.drawText((month+1) + "月", x, headerY, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(r.height()*0.038f);
        paint.setColor(Color.rgb(142,142,147));
        c.drawText(String.valueOf(year), r.right-r.width()*0.08f, headerY, paint);
        paint.setTextAlign(Paint.Align.CENTER);

        String[] wd = {"日","一","二","三","四","五","六"};
        float gridLeft = r.left + r.width()*0.07f;
        float gridRight = r.right - r.width()*0.07f;
        float gridTop = r.top + r.height()*0.23f;
        float gridBottom = r.bottom - r.height()*0.07f;
        float cw = (gridRight-gridLeft)/7f;
        float ch = (gridBottom-gridTop)/7f;

        paint.setTextSize(r.height()*0.027f);
        paint.setTypeface(android.graphics.Typeface.create("sans", android.graphics.Typeface.BOLD));
        for (int i=0;i<7;i++) {
            paint.setColor(i==0 ? Color.rgb(255,69,58) : Color.rgb(142,142,147));
            c.drawText(wd[i], gridLeft + cw*(i+.5f), gridTop + ch*.65f, paint);
        }

        Calendar first = Calendar.getInstance();
        first.set(year, month, 1);
        int firstDow = first.get(Calendar.DAY_OF_WEEK)-1;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar prev = Calendar.getInstance();
        prev.set(year, month-1, 1);
        int daysPrev = prev.getActualMaximum(Calendar.DAY_OF_MONTH);

        paint.setTextSize(r.height()*0.041f);
        for (int cell=0;cell<42;cell++) {
            int row = cell/7;
            int col = cell%7;
            float cx = gridLeft + cw*(col+.5f);
            float cy = gridTop + ch*(row+1.65f);
            int day;
            boolean other=false;
            if (cell < firstDow) {
                day = daysPrev - firstDow + cell + 1;
                other = true;
            } else if (cell >= firstDow + daysInMonth) {
                day = cell - firstDow - daysInMonth + 1;
                other = true;
            } else {
                day = cell - firstDow + 1;
            }

            boolean isToday = !other && day == today;
            if (isToday) {
                paint.setColor(Color.rgb(255,69,58));
                c.drawCircle(cx, cy-r.height()*0.012f, Math.min(cw,ch)*0.35f, paint);
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(other ? Color.rgb(72,72,78) : Color.rgb(225,225,230));
            }
            c.drawText(String.valueOf(day), cx, cy, paint);
        }
        paint.setTextAlign(Paint.Align.LEFT);
    }
}
