//*****************************************************************************
// mvp_vertex.shader
//
// Copyright © 2018 by Sylvain Nieuwlandt
// Released under the MIT License (which can be found in the LICENSE.adoc file)
//*****************************************************************************

uniform mat4 vMatrix;
attribute vec4 vVertices;
attribute vec4 vColors;
varying vec4 color;
void main() {
  color = vColors;
  gl_Position = vMatrix * vVertices;
}