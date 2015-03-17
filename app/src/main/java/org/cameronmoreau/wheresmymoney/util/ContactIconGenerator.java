package org.cameronmoreau.wheresmymoney.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Cameron on 11/20/2014.
 */
public class ContactIconGenerator extends BitmapDrawable {

    public static int DEFAULT = 0;
    public static int LARGE = 1;

    private Context context;
    private String text;
    private int bgColor = 0;
    private Bitmap contactBitmap;

    private int width = 175;
    private int height = 175;
    private float textSize = 100f;

    public ContactIconGenerator(Context context, String text, String bgColor) {
        this.context = context;
        this.text = text;
        this.bgColor = Color.parseColor(bgColor);
    }

    public ContactIconGenerator(Context context, Uri uri) {
        this.context = context;
        this.contactBitmap =  getBitmapFromUri(context, uri);
    }

    public void setColor(int color) { this.bgColor = color; }

    public BitmapDrawable generateIcon() {
        //Initialize Paint
        Paint paint = initPaint();

        //Generate Icon based on constructor type
        if(contactBitmap != null)
            return new BitmapDrawable(context.getResources(), roundedBitmap(paint,contactBitmap));
        else
            return new BitmapDrawable(context.getResources(), createFromPaint(paint, bgColor));
    }

    public BitmapDrawable generateIcon(int type) {
        if(type == LARGE) {
            this.width = 350;
            this.height = 350;
            this.textSize = 200f;
        }
        return generateIcon();
    }

    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

        return paint;
    }

    private Bitmap createFromPaint(Paint paint, int color) {
        float baseline = (height / 2) - ((paint.descent() + paint.ascent()) / 2);
        float middle = (width / 2) - (paint.measureText(text) / 2);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        RectF bg = new RectF(0, 0, width, height);

        paint.setColor(color);
        canvas.drawOval(bg, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(text, middle, baseline, paint);

        return image;
    }

    private Bitmap roundedBitmap(Paint paint, Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        output = Bitmap.createScaledBitmap(output, width, height, false);

        return output;
    }

    private static Bitmap getBitmapFromUri(Context context, Uri uri) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }
}
