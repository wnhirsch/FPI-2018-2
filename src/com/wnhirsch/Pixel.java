/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wnhirsch;

/**
 *
 * @author Wellington
 */
public class Pixel {
    
    // Class Variable
    public int red;
    public int blue;
    public int green;
    
    public Pixel(int rgb, boolean isColorfull){
        if(isColorfull){
            this.red   = Integer.rotateRight(rgb & 0xFF0000, 16);
            this.green = Integer.rotateRight(rgb & 0xFF00, 8);
            this.blue  = rgb & 0xFF;
        }
        else{
            this.red   = rgb;
            this.green = rgb;
            this.blue  = rgb;
        }
    }
    
    public void sumColors(int rgb){
        this.red += Integer.rotateRight(rgb & 0xFF0000, 16);
        this.green += Integer.rotateRight(rgb & 0xFF00, 8);
        this.blue += rgb & 0xFF;
    }
    
    public void divideColor(int factor){
        this.red /= factor;
        this.green /= factor;
        this.blue /= factor;
    }
   
    public void validateRGB(){
        this.red   = validateColor(this.red);
        this.green = validateColor(this.green);
        this.blue  = validateColor(this.blue);
    }
    
    public int validateColor(int color) {
        if (color > 255){
            return 255;
        }
        else if (color < 0) {
            return 0;
        }
        else {
            return color;
        }
    }
    
    public int getRGB(){
        // Convert each one bytes
        int auxRed   = Integer.rotateLeft(this.red, 16);
        int auxGreen = Integer.rotateLeft(this.green, 8);
        int auxBlue  = this.blue;

        // Sum them and return
        return auxRed + auxGreen + this.blue;
    }   
}
