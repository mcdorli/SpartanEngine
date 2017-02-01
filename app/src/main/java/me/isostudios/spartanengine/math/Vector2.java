package me.isostudios.spartanengine.math;

public class Vector2 {

    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public Vector2(Vector2 vec) {
        this(vec.x, vec.y);
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(x + vec.x, y + vec.y);
    }

    public Vector2 sub(Vector2 vec) {
        return new Vector2(x - vec.x, y - vec.y);
    }

    public Vector2 mul(float n) {
        return new Vector2(x * n, y * n);
    }

    public Vector2 div(float n) {
        if (n == 0)
            throw new IllegalArgumentException("Can't divide with 0!");

        return new Vector2(x / n, y / n);
    }

    public float dot(Vector2 vec) {
        return x * vec.x + y * vec.y;
    }

}
