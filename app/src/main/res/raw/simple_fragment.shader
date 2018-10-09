//*****************************************************************************
// simple_fragment.shader
//
// Copyright © 2018 by Sylvain Nieuwlandt
// Released under the MIT License (which can be found in the LICENSE.md file)
//*****************************************************************************

precision mediump float;
uniform vec4 vColor;
void main() {
  gl_FragColor = vColor;
}