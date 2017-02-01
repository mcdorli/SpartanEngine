package me.isostudios.spartanengine.rendering.spritebatch.rendering.textures;

import me.isostudios.spartanengine.rendering.spritebatch.math.Vector2;

public abstract class Texture {

    public Vector2 size;
    protected int texture;

    public abstract void bind(int n);

}
