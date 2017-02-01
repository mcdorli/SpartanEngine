package me.isostudios.spartanengine.math;

public class Vector3 {

    public float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(Vector3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vector3 add(Vector3 vec) {
        return new Vector3(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3 sub(Vector3 vec) {
        return new Vector3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3 mul(float n) {
        return new Vector3(x * n, y * n, z * n);
    }

    public Vector3 div(float n) {
        if (n == 0)
            throw new IllegalArgumentException("Can't divide with 0!");

        return new Vector3(x / n, y / n, z / n);
    }

    public float dot(Vector3 vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vector3 cross(Vector3 vec) {
        return new Vector3(
                y * vec.z - z * vec.y,
                z * vec.x - x * vec.z,
                x * vec.y - y * vec.x
        );
    }

}
