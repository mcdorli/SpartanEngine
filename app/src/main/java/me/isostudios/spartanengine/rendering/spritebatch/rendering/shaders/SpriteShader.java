package me.isostudios.spartanengine.rendering.spritebatch.rendering.shaders;

import android.content.Context;

import me.isostudios.spartanengine.rendering.spritebatch.utils.FileUtil;

public class SpriteShader extends Shader {
    public SpriteShader(Context context) {
        super(FileUtil.readAsString("shaders/sprite.vert", context), FileUtil.readAsString("shaders/sprite.frag", context));
    }

    @Override
    public void bindAttribs() {
        bindAttrib("vertex");
        bindAttrib("textureCoord");
    }
}
