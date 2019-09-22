package com.google.android.gms.samples.vision.ocrreader;

public class Rectangle<T> {
    public T left;
    public T top;
    public T right;
    public T bottom;

    public Rectangle(T left, T top, T right, T bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return "Rectangle("+left+"x"+top+"-"+right+"x"+bottom+")";
    }
}
