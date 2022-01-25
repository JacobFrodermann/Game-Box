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
            Map<String,Object> Settings = new HashMap<String, Object>();
            List<Character> types = new ArrayList<Character>();
            try {
                Read = IOUtils.readLines(new FileInputStream(new File("Settings.txt")),StandardCharsets.UTF_8);
                Read.forEach(x -> {
                    if (x.contains("=")){
                        x = x.replace(" ", "");
                        Object[]VK = x.split("=");
                        System.out.println(VK[0]);
                        if(VK[1].equals(true) || VK[1].equals(false)) {
                            VK[1]=Boolean.parseBoolean((String) VK[1]);
                            types.add('b');
                        } else {
                            VK[1]=Double.valueOf((String) VK[1]);
                            types.add('d');
                        }

                        Settings.put((String)VK[0],VK[1]);
                    } 
            });
            Main.INSTANCE.Settings = Settings;
            Main.INSTANCE.Types = types.toString();
            System.out.println("Settings Loaded");
            } catch (IOException e) {e.printStackTrace();try {
                IOUtils.write("FlappyBird:\n    Gravity=0.125\n    RotationFaktor=1\n    JumpHeight=4\nPong:\n    Xamp=1\n    Yamp=1\n    Linespeed=6\nSnake:\n    help=false\n    DeadlyLake=true\n    ticktime=60\nSpace Destroyer:\n    EnemyHP:10\n    Homing:true\n    InstantDeath:false", new FileOutputStream(new File("Settings.txt")), StandardCharsets.UTF_8);
            } catch (IOException e1) {}}
            
        }
    }
    
}
