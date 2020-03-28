#version 400

in layout(location = 0) vec3 position;
in layout(location = 1) vec2 uv;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out struct {
    vec2 uv;
} params;

void main() {
    params.uv = uv;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
}
