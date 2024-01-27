package frc.robot.subsystems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Band {

    private Orchestra _orchestra;
    private List<String> songs;

    TalonFX[] _fxes = {
        new TalonFX(1),
        new TalonFX(2),
        new TalonFX(3),
        new TalonFX(4),
        new TalonFX(5),
        new TalonFX(6),
        new TalonFX(7),
        new TalonFX(8)
    };

    public Band() {
        songs = new ArrayList<String>();

        _orchestra = new Orchestra();

        for (TalonFX motor : _fxes) {
        _orchestra.addInstrument(motor);
        }

        File[] musicFiles = new File(Filesystem.getDeployDirectory(), "music").listFiles();

        for (File fileEntry : musicFiles) {
            if (!fileEntry.isDirectory()) {
                songs.add(fileEntry.getName());
            }
        }
    }

    public SendableChooser<String> Buildchoser(){
        SendableChooser<String> chooser = new SendableChooser<String>();
        for (String file : songs) {
            chooser.addOption(file, file);
        }
        return chooser;
    }

    public void play(String filePath){
        _orchestra.loadMusic("music/"+filePath);
        _orchestra.play();
    }
}
