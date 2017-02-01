attribute vec3 vertex;
attribute vec2 textureCoord;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform float layer;

varying vec2 texCoord;

void main() {
	gl_Position = projection * view * model * vec4(vertex.xy, layer, 1);
	texCoord = textureCoord;
}
