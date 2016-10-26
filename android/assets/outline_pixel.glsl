#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 step_size;
uniform float current_alpha;

void main()
{
    vec4 tex_color = texture2D(u_texture, v_texCoords);

    /*
    float alpha = 4 * tex_color.w;
    alpha -= texture2D(u_texture, v_texCoords + vec2( step_size.x, 0.0f ) ).w;
    alpha -= texture2D(u_texture, v_texCoords + vec2( -step_size.x, 0.0f ) ).w;
    alpha -= texture2D(u_texture, v_texCoords + vec2( 0.0f, step_size.y ) ).w;
    alpha -= texture2D(u_texture, v_texCoords + vec2( 0.0f, -step_size.y ) ).w;
    gl_FragColor = v_color * vec4(.443, .91, 1, alpha * .95);
    */
    if (tex_color.w == 0){
        float a1 = texture2D(u_texture, v_texCoords + vec2( step_size.x, 0.0f ) ).w;
        float a2 = texture2D(u_texture, v_texCoords + vec2( -step_size.x, 0.0f ) ).w;
        float a3 = texture2D(u_texture, v_texCoords + vec2( 0.0f, step_size.y ) ).w;
        float a4 = texture2D(u_texture, v_texCoords + vec2( 0.0f, -step_size.y ) ).w;
        //float a5 = texture2D(u_texture, v_texCoords + vec2( step_size.x, step_size.y ) ).w;
        //float a6 = texture2D(u_texture, v_texCoords + vec2( step_size.x, -step_size.y ) ).w;
        //float a7 = texture2D(u_texture, v_texCoords + vec2( -step_size.x, step_size.y ) ).w;
        //float a8 = texture2D(u_texture, v_texCoords + vec2( -step_size.x, -step_size.y ) ).w;
        if (a1 != 0 ||  a2 != 0 || a3 != 0 || a4 != 0){ //|| a5 != 0 || a6 != 0 || a7 != 0 || a8 != 0){
            gl_FragColor = v_color * vec4(1, 0, 0, 1);
        }
        else
         gl_FragColor = v_color * vec4(.443, .91, 1, 0);
    }
    else
     gl_FragColor = v_color * vec4(.443, .91, 1, 0);
}
