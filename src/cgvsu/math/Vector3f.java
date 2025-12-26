package cgvsu.math;

public class Vector3f {

    public static final float EPS = 1e-6f;

    public float x, y, z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f subtract(Vector3f other) {
        return new Vector3f(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public Vector3f cross(Vector3f o) {
        return new Vector3f(
                this.y * o.z - this.z * o.y,
                this.z * o.x - this.x * o.z,
                this.x * o.y - this.y * o.x
        );
    }

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        float len = length();
        if (len < EPS) return new Vector3f(0, 0, 0);
        return new Vector3f(x / len, y / len, z / len);
    }

    @Override
    public String toString() {
        return "Vector3f(" + x + ", " + y + ", " + z + ")";
    }
}