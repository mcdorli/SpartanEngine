package me.isostudios.spartanengine.rendering.textures;

import me.isostudios.spartanengine.math.Vector2;

public abstract class Texture {

    public Vector2 size;
    protected int texture;

    public abstract void bind(int n);

}
