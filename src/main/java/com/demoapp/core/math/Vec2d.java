package com.demoapp.core.math;

import java.util.List;

/**
 * A 2D math Vector model class.
 *
 * @author Frédéric Delorme
 * @since 1.0.3
 */
public class Vec2d {
    public double x;
    public double y;

    public Vec2d() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d add(Vec2d v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vec2d add(List<Vec2d> vl) {
        for (Vec2d v : vl) {
            add(v);
        }
        return this;
    }

    public Vec2d normalize() {
        // sets length to 1
        //
        double length = Math.sqrt(x * x + y * y);

        if (length != 0.0) {
            double s = 1.0f / length;
            x = x * s;
            y = y * s;
        }

        return new Vec2d(x, y);
    }

    public Vec2d multiply(double v) {
        return new Vec2d(x * v, y * v);
    }

    public Vec2d minMax(double minVal, double maxVal) {
        this.x = MathUtils.ceilMinMaxValue(this.x, minVal, maxVal);
        this.y = MathUtils.ceilMinMaxValue(this.y, minVal, maxVal);
        return this;
    }

    public Vec2d setX(double x) {
        this.x = x;
        return this;
    }

    public Vec2d setY(double y) {
        this.y = y;
        return this;
    }

    public String toString() {
        return String.format("(%4.3f,%4.3f)", x, y);
    }

    public boolean isZero() {
        return (x == 0.0 && y == 0.0);
    }

    public String toStringAsInt() {
        return String.format("(%4.0f,%4.0f)", x, y);
    }
}
