uniform mat4 vMatrix;
attribute vec4 vPosition;
attribute vec4 vColor;
varying vec4 color;
void main() {
  color = vColor;
  gl_Position = vMatrix * vPosition;
}