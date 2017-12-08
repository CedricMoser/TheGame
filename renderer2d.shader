# vertex
# version 330 core
layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 uv;
layout(location = 3) in float textureID;

out vec4 color_out;
out vec2 uv_out;
out float texID;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = projection * model * vec4(pos, 1.0);
    color_out = color;
    uv_out = uv;
    texID = textureID;
}

# fragment
# version 330 core
in vec4 color_out;
in vec2 uv_out;
in float texID;

out vec4 color_out2;

void main() {
    color_out2 = color_out;
}