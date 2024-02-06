package frc.robot.subsystems;

import java.util.Arrays;
import java.util.logging.LogManager;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;


public class LEDS {
    
    private AddressableLED m_led;
    private AddressableLEDBuffer m_ledBuffer;
    private int rainbowValue = 0;
    private Timer timer;
    private String mode;
    private String lastMode;
    private double nextBlink;
    private boolean blink;

    /**
     * Master LED Controler. manages the ligts for ease of use. use WS2812B individualy addressable LED strips
     * 
     * @param PWM the PWM port the dataline is connected to
     * @param length the number of lights on the strand
     */
    public LEDS(int PWM, int length) {
        
        m_led = new AddressableLED(PWM);

        m_ledBuffer = new AddressableLEDBuffer(length);
        m_led.setLength(m_ledBuffer.getLength());

        m_led.setData(m_ledBuffer);
        m_led.start();

        timer = new Timer();
        nextBlink = timer.get() + Constants.Mode.blinkTime;
        
        mode = "D";
        lastMode = "N";
    }

    // public int[] HEXtoRGB(String hex){
    //     int R = Integer.valueOf(hex.substring(0,2))
    //     int G = Integer.valueOf(hex.substring(0,2))
    //     int B = Integer.valueOf(hex.substring(0,2))
    // }
    
    /**
     * Set the entire strip to one color using RGB
     * 
     * @param R Red value
     * @param G Green value
     * @param B Blue value
     */
    public void SetFull(int R, int G, int B) {
        // For every Pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values
            m_ledBuffer.setRGB(i, R, G, B);
         }
         m_led.setData(m_ledBuffer);
    }

    /**
     * Set the full strip using a {@link Color}
     * 
     * @param clr {@link Color} to set to
     */
    public void SetFull(Color clr) {
        // For every Pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the Color provided
            m_ledBuffer.setLED(i, clr);
         }
         m_led.setData(m_ledBuffer);
    }

    /**
     * Set the entire strip to one volor using HSV 
     * 
     * @param H Hue value
     * @param S Saturation value
     * @param V Value value (hmm...)
     */
    public void SetFullHSV(int H, int S, int V) {
        // For every pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the HSV values 
            m_ledBuffer.setHSV(i, H, S, V);
         }
         m_led.setData(m_ledBuffer);
    }

    public void updateMode() {
        if (mode != lastMode) {
            switch (mode) {
                case "D":
                    SetFull(Constants.Mode.Colors.D);
                case "IO":
                    SetFull(Constants.Mode.Colors.I);
                case "IL":
                    SetFull(Constants.Mode.Colors.I);
                case "N":
                    SetFull(Constants.Mode.Colors.R);
                case "R":
                    SetFull(Constants.Mode.Colors.R);
                case "K":
                    SetFull(Constants.Mode.Colors.K);
                case "KP":
                    SetFull(Constants.Mode.Colors.KP);
                case "L":
                    SetFull(Constants.Mode.Colors.L);
                case "CE":
                    SetFull(Constants.Mode.Colors.C);
                case "CR":
                    SetFull(Constants.Mode.Colors.C);
                case "C":
                    SetFull(Constants.Mode.Colors.C);
                case "CL":
                    SetFull(Constants.Mode.Colors.C);
                default:
                    break;
            }
        }

        if(timer.get() >= nextBlink) {
            blink = !blink;
            switch (mode) {
                case "IO":
                    if(blink) SetFull(Constants.Mode.Colors.I);
                    else SetFull(Constants.Mode.Colors.IO);
                case "R":
                    if(blink) SetFull(Constants.Mode.Colors.I);
                    else SetFull(Constants.Mode.Colors.IO);
                case "CE":
                    if(blink) SetFull(Constants.Mode.Colors.C);
                    else SetFull(Constants.Mode.Colors.CE);
                case "CR":
                    if(blink) SetFull(Constants.Mode.Colors.C);
                    else SetFull(Constants.Mode.Colors.CR);
                case "C":
                    if(blink) SetFull(Constants.Mode.Colors.C);
                    else SetFull(Constants.Mode.Colors.CC);
                case "CL":
                    if(blink) SetFull(Constants.Mode.Colors.C);
                    else SetFull(Constants.Mode.Colors.CL);
                default:
                    break;
            }

        }



    }

    public void setMode(String modeID) {
        String[] vModes = {"D","IO","IL","N","R","K","KP","L","CE","CR","C","CL"};
        if (Arrays.asList(vModes).contains(modeID)) {
            mode = modeID;
        }
    }

    public String getMode() {
        return mode;
    }

    /*
     * Rainbow function, called perodicly, shifts a rainbow through the strip
     */
    public void rainbow() {
        // For every pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
          // Calculate the hue - hue is easier for rainbows because the color
          // shape is a circle so only one value needs to precess
          final var hue = (rainbowValue + (i * 180 / m_ledBuffer.getLength())) % 180;
          // Set the value
          m_ledBuffer.setHSV(i, hue, 255, 128);
        }

        rainbowValue += 3;

        rainbowValue %= 180;
        
        m_led.setData(m_ledBuffer);
    }


    /**
     * Rainbow function, called perodicly, shifts a rainbow through the strip
     * 
     * @param shift the ammount to shift each step. could also be viewed as speed.
     */
    public void rainbow(int shift) {
        // For every pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
          // Calculate the hue - hue is easier for rainbows because the color
          // shape is a circle so only one value needs to precess
          final var hue = (rainbowValue + (i * 180 / m_ledBuffer.getLength())) % 180;
          // Set the value
          m_ledBuffer.setHSV(i, hue, 255, 128);
        }

        rainbowValue += shift;

        rainbowValue %= 180;
        
        m_led.setData(m_ledBuffer);
    }

    /**
     * Rainbow function, called perodicly, shifts a rainbow through the strip
     * 
     * @param shift the ammount to shift each step. could also be viewed as speed.
     * @param repititions the number of rainbows to have in the entire strip
     */
    public void rainbow(int shift, int repititions) {
        // For every pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
          // Calculate the hue - hue is easier for rainbows because the color
          // shape is a circle so only one value needs to precess
          final var hue = (rainbowValue + (i * 180 / m_ledBuffer.getLength()) / repititions) % 180;
          // Set the value
          m_ledBuffer.setHSV(i, hue, 255, 128);
        }

        rainbowValue += shift;

        rainbowValue %= 180;
        
        m_led.setData(m_ledBuffer);
    }
}
