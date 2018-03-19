# vertex
# version 330 core
layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 uv;
layout(location = 3) in float textureID;
layout(location = 4) in float sdf_width_in;
layout(location = 5) in float sdf_edge_in;

out vec4 color_out;
out vec2 uv_out;
out float texID;
out float sdf_width;
out float sdf_edge;

uniform mat4 projection;

void main() {
    gl_Position = projection * vec4(pos, 1.0);
    color_out   = color;
    uv_out      = uv;
    texID       = textureID;
    sdf_width   = sdf_width_in;
    sdf_edge    = sdf_edge_in;
}

# fragment
# version 330 core
in vec4 color_out;
in vec2 uv_out;
in float texID;
in float sdf_width;
in float sdf_edge;

out vec4 color_out2;

uniform sampler2D textures[32];

const float width = 0.51;
const float edge  = 0.02;

void main() {
    if (texID >= 0.0) {
        color_out2 = texture2D(textures[int(texID)], uv_out);
        float distance = 1.0 - color_out2.a;
        float alpha = 1.0 - smoothstep(sdf_width, sdf_width + sdf_edge, distance);
        color_out2 = vec4(color_out2.rgb, alpha);
    } else {
        color_out2 = color_out;
    }

    if(color_out2.a == 0.0) discard;
}