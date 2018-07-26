uniform mat4 vMatrix;
attribute vec4 vPosition;
attribute vec4 vColors;
varying vec4 color;
void main() {
  color = vColors;
  gl_Position = vMatrix * vPosition;
}