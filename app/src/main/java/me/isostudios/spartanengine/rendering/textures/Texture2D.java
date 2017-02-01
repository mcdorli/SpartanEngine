package me.isostudios.spartanengine.rendering.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import me.isostudios.spartanengine.math.Vector2;

import static android.opengl.GLES20.*;

public class Texture2D extends Texture {

    public Texture2D(int resource, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);

        int[] perm = new int[1];
        glGenTextures(1, perm, 0);
        texture = perm[0];

        size = new Vector2(bitmap.getWidth(), bitmap.getHeight());

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap, 0);
    }

    public void setPixelated(boolean pixelated) {
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, pixelated ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, pixelated ? GL_NEAREST : GL_LINEAR);
    }

    @Override
    public void bind(int n) {
        glActiveTexture(GL_TEXTURE0 + n);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void cleanup() {
        glDeleteTextures(1, new int[]{texture}, 0);
    }

}
