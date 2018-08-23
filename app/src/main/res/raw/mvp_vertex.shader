uniform mat4 vMatrix;
attribute vec4 vVertices;
attribute vec4 vColors;
varying vec4 color;
void main() {
  color = vColors;
  gl_Position = vMatrix * vVertices;
}