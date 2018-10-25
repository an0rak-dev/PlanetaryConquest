//*****************************************************************************
// point_vertex.shader
//
// Copyright © 2018 by Sylvain Nieuwlandt
// Released under the MIT License (which can be found in the LICENSE.adoc file)
//*****************************************************************************

uniform float vPointSize;
uniform mat4 vMatrix;
attribute vec4 vVertices;

void main() {
  gl_Position = vMatrix * vVertices;
  gl_PointSize = vPointSize;
}