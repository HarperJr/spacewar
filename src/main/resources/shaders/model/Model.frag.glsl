# version 400

struct Light {
    vec3 direction;
    vec4 color;
};

uniform mat4 modelMatrix;
uniform sampler2D texAmbient;
uniform sampler2D texDiffuse;
uniform sampler2D texSpecular;
uniform sampler2D texEmissive;

in struct {
    vec2 uv;
    vec3 normal;
} params;

out vec4 color;

vec4 lambertLighting(in vec4 ambient, in vec4 diffuse, in Light light, in vec3 normal) {
    vec3 encodedNormal = normal * 0.5 + 0.5;
    float ndotl = max(dot(encodedNormal, -light.direction), 0.0);
    return diffuse * ambient * ndotl * light.color;
}

Light light = Light(vec3(0.0, -1.0, -1.0), vec4(1.0));
vec4 diffuseDefault = vec4(1.0, 1.0, 1.0, 1.0);

void main() {
    vec4 modelNormal = modelMatrix * vec4(params.normal, 0.0);
    color = lambertLighting(texture(texAmbient, params.uv), diffuseDefault, light, modelNormal.xyz);
}