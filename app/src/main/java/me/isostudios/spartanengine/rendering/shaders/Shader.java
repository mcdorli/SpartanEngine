package me.isostudios.spartanengine.rendering.shaders;

import android.util.Log;

import java.util.HashMap;

import me.isostudios.spartanengine.math.Vector2;
import me.isostudios.spartanengine.math.Vector3;

import static android.opengl.GLES20.*;

public abstract class Shader {

    private int program;

    private HashMap<String, Integer> attribs = new HashMap<>();
    private HashMap<String, Integer> uniforms = new HashMap<>();

    public Shader(String vertexSource, String fragmentSource) {
        int vertex = createShader(vertexSource, GL_VERTEX_SHADER);
        int fragment = createShader(fragmentSource, GL_FRAGMENT_SHADER);

        program = glCreateProgram();
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);

        int[] status = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, status, 0);
        if (status[0] != GL_TRUE) {
            Log.e("SpartanEngine", "Failed to link program!");
            Log.e("SpartanEngine", glGetProgramInfoLog(program));
            glDeleteProgram(program);
            program = -1;
        }

        bindAttribs();

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    private int createShader(String src, int type) {
        int shader = glCreateShader(type);
        glShaderSource(shader, src);

        glCompileShader(shader);

        int[] status = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
        if (status[0] == 0) {
            Log.e("SpartanEngine", "Failed to compile shader!");
            Log.e("SpartanEngine", glGetShaderInfoLog(shader));
            glDeleteShader(shader);
            return -1;
        }
        return shader;
    }

    public abstract void bindAttribs();

    public int getUniformLocation(String name) {
        if (!uniforms.containsKey(name))
            uniforms.put(name, glGetUniformLocation(program, name));
        return uniforms.get(name);
    }

    protected void bindAttrib(String name) {
        attribs.put(name, glGetAttribLocation(program, name));
    }

    public void bind() {
        glUseProgram(program);
    }

    public void loadUniform(String name, boolean b) {
        glUniform1f(getUniformLocation(name), b ? 1 : 0);
    }

    public void loadUniform(String name, int i) {
        glUniform1i(getUniformLocation(name), i);
    }

    public void loadUniform(String name, float f) {
        glUniform1f(getUniformLocation(name), f);
    }

    public void loadUniform(String name, Vector2 vec) {
        glUniform2f(getUniformLocation(name), vec.x, vec.y);
    }

    public void loadUniform(String name, Vector3 vec) {
        glUniform3f(getUniformLocation(name), vec.x, vec.y, vec.z);
    }

    public void loadUniform(String name, float[] m) {
        glUniformMatrix4fv(getUniformLocation(name), 1, false, m, 0);
    }

    public void loadVBO(int datasize, String attrib) {
        glEnableVertexAttribArray(attribs.get(attrib));
        glVertexAttribPointer(attribs.get(attrib), datasize, GL_FLOAT, false, 0, 0);
    }

    public void cleanup() {
        glDeleteProgram(program);
    }

}
