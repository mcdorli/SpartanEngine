package me.isostudios.spartanengine.rendering.spritebatch;

import android.content.Context;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.HashMap;

import me.isostudios.spartanengine.math.Vector2;
import me.isostudios.spartanengine.rendering.Model;
import me.isostudios.spartanengine.rendering.shaders.SpriteShader;
import me.isostudios.spartanengine.rendering.textures.Texture;

import static android.opengl.GLES20.*;

public class SpriteBatch {

    public float maxDepth;
    private Model model;
    private SpriteShader shader;
    private HashMap<Texture, ArrayList<Call>> batch = new HashMap<>();
    private ArrayList<float[]> matrixStack = new ArrayList<>();
    private float[] projectionM = new float[16];

    public SpriteBatch(int width, int height, float maxDepth, Context context) {
        this.maxDepth = maxDepth;

        model = new Model();
        model.addVBO("vertex", new float[]{
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0
        });
        model.addVBO("textureCoord", new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0
        });
        model.setIBO(new int[]{
                0, 1, 3,
                3, 1, 2
        });

        shader = new SpriteShader(context);

        adjust(width, height);

        glClearColor(0, 0, 0, 1);
        glEnable(GL_DEPTH_TEST);
    }

    public void adjust(int width, int height) {

        Matrix.setIdentityM(projectionM, 0);
        Matrix.orthoM(projectionM, 0, -width / 2, width / 2, -height / 2, height / 2, 0, maxDepth);
    }

    public void setIdentity() {
        matrixStack.clear();
    }

    public void rotate(float angle) {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.rotateM(m, 0, angle, 0, 0, 1);
        pushMatrix(m);
    }

    public void moveCamera(Vector2 v) {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.translateM(m, 0, -v.x, -v.y, 0);
        pushMatrix(m);
    }

    public void pushMatrix(float[] m) {
        matrixStack.add(m);
    }

    public float[] popMatrix() {
        return matrixStack.remove(matrixStack.size() - 1);
    }

    public void begin() {
        shader.bind();
        model.bindVBO("vertex");
        shader.loadVBO(3, "vertex");
        model.bindVBO("textureCoord");
        shader.loadVBO(2, "textureCoord");
        model.bindIBO();
    }

    public void draw(Texture texture, Vector2 pos, Vector2 size, float angle, float layer) {
        ArrayList<Call> calls = batch.get(texture);
        if (calls == null) {
            calls = new ArrayList<>();
            batch.put(texture, calls);
        }
        calls.add(new Call(texture, pos, size, -layer, angle));
    }

    public void draw(Texture texture, Vector2 pos) {
        draw(texture, pos, texture.size, 0, 0);
    }

    public void draw(Texture texture, Vector2 pos, Vector2 size) {
        draw(texture, pos, size, 0, 0);
    }

    public void draw(Texture texture, Vector2 pos, Vector2 size, float angle) {
        draw(texture, pos, size, angle, 0);
    }

    public void end() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float[] viewM = new float[16];
        Matrix.setIdentityM(viewM, 0);

        for (float[] m : matrixStack) {
            Matrix.multiplyMM(viewM, 0, m, 0, viewM, 0);
        }

        shader.loadUniform("projection", projectionM);
        shader.loadUniform("view", viewM);

        for (Texture texture : batch.keySet()) {
            texture.bind(0);
            shader.loadUniform("sampler", 0);

            for (Call call : batch.get(texture)) {
                float[] modelM = new float[16];
                Matrix.setIdentityM(modelM, 0);
                Matrix.rotateM(modelM, 0, call.angle, 0, 0, 1);
                Matrix.translateM(modelM, 0, call.pos.x, call.pos.y, 0);
                Matrix.scaleM(modelM, 0, call.size.x, call.size.y, 1);

                shader.loadUniform("model", modelM);
                shader.loadUniform("layer", call.layer);

                model.draw();
            }
        }

        batch.clear();
    }

    private class Call {

        public Texture texture;
        public Vector2 pos;
        public Vector2 size;
        public float layer;
        public float angle;

        public Call(Texture texture, Vector2 pos, Vector2 size, float layer, float angle) {
            this.texture = texture;
            this.pos = pos;
            this.size = size;
            this.layer = layer;
            this.angle = angle;
        }
    }

}
