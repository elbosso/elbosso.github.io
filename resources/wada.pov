#include "colors.inc"
#include "textures.inc"

global_settings { 
	max_trace_level 100
	max_intersections 128
}

// Camera specification
camera {
   location <2,-2,2>
   up       y
   right    x*4/3
   angle    20
	look_at   <0,0,0>
}

// Three lights, red, green, blue located opposite four of the three gaps
/*light_source {
   <-3,-3,-3>
   color <1.0,0.0,0.0>
}
light_source {
   <-3,3,3>
   color <0.0,1.0,0.0>
}
light_source {
   <3,3,-3>
   color <0.0,0.0,1.0>
}
*/
// One Light Source positioned behind the camera
light_source {
   <3,-3,3>
   color <1.0,1.0,1.0>
}

// Three lights, differently coloured and all behind the camera but each one
// slightly offset from the two others
/*light_source {
   <3.5,-3,3.5>
   color <1.0,0.0,0.0>
}
light_source {
   <-3.5,-3,3.5>
   color <0.0,1.0,0.0>
}
light_source {
   <3.5,-3,-3.5>
   color <0.0,0.0,1.0>
}
*/
#declare mat0=texture{Silver2}
#declare mat2=texture{Glass}
#declare mat=texture{
      pigment {
         color <1,1,1>
      }
      finish {
         ambient 0.1
         diffuse 0.9
         brilliance 4
         specular 1
         reflection 1
         phong 1
         phong_size 250
	 roughness 0.005
         metallic
      }
   }



#declare kuller=sphere {
   <0,0,0>, 0.70710678118654757
   texture {mat}
}

// The three spheres follow, all highly reflective and grey
object{kuller translate   <-0.5,-0.5,0.5>}
object{kuller translate   <0.5,0.5,0.5>}
object{kuller translate   <-0.5,0.5,-0.5>}
object{kuller translate   <0.5,-0.5,-0.5>}

