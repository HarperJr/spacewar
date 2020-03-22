# version 400

struct Light {
    vec3 direction;
    vec4 color;
};

uniform mat4 modelMatrix;

uniform sampler2D ambientTex;
uniform sampler2D lightmapTex;

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

//void phongLighting(in Material mat, in Light light, in vec3 normal, in vec3 look) {
//    vec3 reflect = normalize(reflect(-light.direction, normal));
//    mat.specular = vec4(mat.specular.rgb * pow(max(0.0, dot(reflect, -look)), mat.specular.w), 1.0);
//}

Light light = Light(vec3(0.0, -1.0, -1.0), vec4(1.0));
vec4 ambientDefault = vec4(0.5, 0.5, 0.5, 1.0);
vec4 diffuseDefault = vec4(0.5, 0.5, 0.5, 1.0);

void main() {
    vec4 worldNormal = modelMatrix * vec4(params.normal, 0.0);
    color = lambertLighting(ambientDefault, diffuseDefault, light, worldNormal.xyz);
}