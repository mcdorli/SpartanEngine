package me.isostudios.spartanengine.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import me.isostudios.spartanengine.utils.BufferUtil;

import static android.opengl.GLES20.*;

public class Model {

    private HashMap<String, Integer> vbos = new HashMap<>();
    private int ibo = -1;

    private int vertexCount = 0;

    public void addVBO(String name, float[] data) {
        FloatBuffer buffer = BufferUtil.createFromData(data);

        int[] perm = new int[1];
        glGenBuffers(1, perm, 0);
        int vbo = perm[0];

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GL_STATIC_DRAW);

        vbos.put(name, vbo);
    }

    public void setIBO(int[] data) {
        IntBuffer buffer = BufferUtil.createFromData(data);

        int[] perm = new int[1];
        glGenBuffers(1, perm, 0);
        ibo = perm[0];

        vertexCount = data.length;
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GL_STATIC_DRAW);
    }

    public void bindVBO(String name) {
        if (vbos.containsKey(name)) {
            glBindBuffer(GL_ARRAY_BUFFER, vbos.get(name));
        }
    }

    public void bindIBO() {
        if (ibo == -1)
            throw new IllegalStateException("No ibo found!");
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, vertexCount, vertexCount > 255 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT, 0);
    }

    public void cleanup() {
        for (int i : vbos.values()) {
            glDeleteBuffers(1, new int[]{i}, 0);
        }
        glDeleteBuffers(1, new int[]{ibo}, 0);
    }

}
