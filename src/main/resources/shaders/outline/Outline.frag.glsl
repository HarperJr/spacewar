#version 400

in struct {
    vec4 color;
} params;

out vec4 color;

void main() {
    color = params.color;
}
