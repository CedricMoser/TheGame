package maths;

public class Mat4 {
    private float[] mMat;

    public Mat4() {
        this.mMat = new float[4 * 4];

        for (int i = 0; i < 4 * 4; i++) {
            this.mMat[i] = 0.0f;
        }
    }

    public Mat4(float diagonal) {
        this();

        this.mMat[0 + 0 * 4] = 1.0f;
        this.mMat[1 + 1 * 4] = 1.0f;
        this.mMat[2 + 2 * 4] = 1.0f;
        this.mMat[3 + 3 * 4] = 1.0f;
    }

    public static Mat4 ortho(float left, float right, float top, float bottom, float near, float far) {
        Mat4 result = new Mat4(1.0f);

        result.mMat[0 + 0 * 4] = 2.0f / (right - left);

        result.mMat[1 + 1 * 4] = 2.0f / (top - bottom);

        result.mMat[2 + 2 * 4] = 2.0f / (near - far);

        result.mMat[0 + 3 * 4] = (left + right) / (left - right);
        result.mMat[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.mMat[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Mat4 perspective(float fov, float aspectRatio, float near, float far) {
        return new Mat4();
    }

    public float[] getFloatArray() {
        return this.mMat;
    }
}
