package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LEDS {
    
    private AddressableLED m_led;
    private AddressableLEDBuffer m_ledBuffer;
    private int rainbowValue = 0;

    public LEDS(int PWM, int length) {
        
        m_led = new AddressableLED(9);

        m_ledBuffer = new AddressableLEDBuffer(60);
        m_led.setLength(m_ledBuffer.getLength());

        m_led.setData(m_ledBuffer);
        m_led.start();
    }

    // public int[] HEXtoRGB(String hex){
    //     int R = Integer.valueOf(hex.substring(0,2))
    //     int G = Integer.valueOf(hex.substring(0,2))
    //     int B = Integer.valueOf(hex.substring(0,2))
    // }

    public void SetFull(int R, int G, int B) {
        // For every Pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values
            m_ledBuffer.setRGB(i, R, G, B);
         }
         m_led.setData(m_ledBuffer);
    }

    public void SetFullHSV(int H, int S, int V) {
        // For every pixel
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the HSV values 
            m_ledBuffer.setHSV(i, H, S, V);
         }
         m_led.setData(m_ledBuffer);
    }

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
}
