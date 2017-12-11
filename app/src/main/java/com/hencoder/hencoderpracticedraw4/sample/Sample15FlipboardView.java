package com.hencoder.hencoderpracticedraw4.sample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Sample15FlipboardView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    int fixedDegreeY = 0;
    int degreeY = 0;
    int degreeZ = 0;
    AnimatorSet animatorSet = new AnimatorSet();
    ObjectAnimator animatorFixedY = ObjectAnimator.ofInt(this, "fixedDegreeY", 0, 30);
    ObjectAnimator animatorY = ObjectAnimator.ofInt(this, "degreeY", 0, 45);
    ObjectAnimator animatorZ = ObjectAnimator.ofInt(this, "degreeZ", 0, 270);

    public Sample15FlipboardView(Context context) {
        super(context);
    }

    public Sample15FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample15FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        animatorFixedY.setDuration(1000);
        animatorY.setDuration(1000);
        animatorZ.setDuration(1500);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        start();
    }

    public void reset() {
        this.fixedDegreeY = 0;
        this.degreeY = 0;
        this.degreeZ = 0;
        invalidate();
    }

    public void setFixedDegreeY(int degree) {
        this.fixedDegreeY = degree;
        invalidate();
    }

    public void setDegreeY(int degree) {
        this.degreeY = degree;
        invalidate();
    }

    public void setDegreeZ(int degree) {
        this.degreeZ = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        // 第一步绘制：不动的部分
        //移动画布(注意移动顺序与编码顺序相反),让图片中心点与camera中心点相同,将画布图片状态通过camera保存然后截取,最后再移回画布
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degreeZ);
        camera.rotateY(fixedDegreeY);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        canvas.rotate(degreeZ);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        camera.restore();
        canvas.restore();

        // 绘制变化的部分
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degreeZ);
        camera.rotateY(-degreeY);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -centerY, centerX, centerY);
        canvas.rotate(degreeZ);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        camera.restore();
        canvas.restore();
    }

    public void start() {
        reset();
        animatorSet.cancel();
        animatorSet.playSequentially(animatorY, animatorZ, animatorFixedY);
        animatorSet.start();
    }
}
