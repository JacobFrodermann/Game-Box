import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class ReadSettings extends Thread{
    @Override
    public void run() {
        
        if (new File("Settings.txt").exists()) {
            List<String> Read = new ArrayList<String>();
            Map<String,String> Settings = new HashMap<String, String>();
            try {
                Read = IOUtils.readLines(new FileInputStream(new File("Settings.txt")),StandardCharsets.UTF_8);
                Read.forEach(x -> {
                    if (x.contains("=")){
                        x = x.replace(" ", "");
                        String[]VK = x.split("=");
                        System.out.println(VK[0]);
                        Settings.put(VK[0],VK[1]);
                    } 
            });
            Main.INSTANCE.Settings = Settings;
            System.out.println("Settings Loaded");
            } catch (IOException e) {e.printStackTrace();try {
                IOUtils.write("FlappyBird:\n    Gravity=0.125\n    RotationFaktor=1\n    JumpHeight=4\nPong:\n    Xamp=1\n    Yamp=1\n    Linespeed=6\nSnake:\n    help=false\n    DeadlyLake=true\n    ticktime=60\nSpace Destroyer:\n    EnemyHP:10\n    Homing:true\n    InstantDeath:false", new FileOutputStream(new File("Settings.txt")), StandardCharsets.UTF_8);
            } catch (IOException e1) {}}
            
        }
    }
    
}
