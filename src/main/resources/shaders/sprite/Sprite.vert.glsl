#version 400

in layout(location = 0) vec3 position;
in layout(location = 1) vec2 uv;
in layout(location = 3) vec4 color;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out struct {
    vec2 uv;
    vec4 color;
} params;

void main() {
    params.uv = uv;
    params.color = color;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
}
