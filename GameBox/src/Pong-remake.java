import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Pong_remake {

    int Wall1pos;
    int Wall2pos;
    Dimension Ballpos;

    void Innit() {
        Wall1pos = 100;
        Wall2pos = 100;
        Ballpos = new Dimension(180,280);
    }

    void loop() {
        if (/*a gedrückt*/true) {
            Wall1pos -= 1;
        }
        if (/*d gedrückt*/true) {
            Wall1pos += 1;
        }
        if (/*Pfeil rechts gedrückt*/true) {
            Wall2pos -= 1;
        }
        if (/*Pfeil links gedrückt*/true) {
            Wall2pos += 1;
        }
    }
    
}
