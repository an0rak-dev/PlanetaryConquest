//*****************************************************************************
// multicolor_fragment.shader
//
// Copyright © 2018 by Sylvain Nieuwlandt
// Released under the MIT License (which can be found in the LICENSE.adoc file)
//*****************************************************************************

precision mediump float;
varying vec4 color;
void main() {
  gl_FragColor = color;
}