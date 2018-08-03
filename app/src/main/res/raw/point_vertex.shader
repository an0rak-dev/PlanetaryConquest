uniform float vPointSize;
attribute vec4 vPosition;

void main() {
  gl_Position = vPosition;
  gl_PointSize = vPointSize;
}