uniform float vPointSize;
uniform mat4 vMatrix;
attribute vec4 vVertices;

void main() {
  gl_Position = vMatrix * vVertices;
  gl_PointSize = vPointSize;
}