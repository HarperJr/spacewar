#version 400

uniform sampler2D tex;

in struct {
    vec2 uv;
    vec4 color;
} params;

out vec4 color;

void main() {
    color = texture(tex, params.uv) * params.color;
}
