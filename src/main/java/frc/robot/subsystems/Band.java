package frc.robot.subsystems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Band {

    public Orchestra orchestra;
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
    
    /**
     * Create a Orchestra for the robot. makes 8 motors and adds them to the orchestra. also gets all the music files and saves them to a list to be used in a {@link SendableChooser}
     */
    public Band() {
        songs = new ArrayList<String>();

        orchestra = new Orchestra();

        for (TalonFX motor : _fxes) {
        orchestra.addInstrument(motor);
        }

        File[] musicFiles = new File(Filesystem.getDeployDirectory(), "music").listFiles();

        for (File fileEntry : musicFiles) {
            if (!fileEntry.isDirectory()) {
                songs.add(fileEntry.getName());
            }
        }
    }

    /**
     * Build the {@link SendableChooser} to be used in the {@link SmartDashboard}. 
     * @return returns a new {@link SendableChooser} obeject that can be passed into the {@link SmartDashboard} through .putData()
     */
    public SendableChooser<String> Buildchoser(){
        SendableChooser<String> chooser = new SendableChooser<String>();
        for (String file : songs) {
            chooser.addOption(file, file);
        }
        return chooser;
    }
    /**
     * Plays the provided song. use with the {@link SendableChooser} to get correct song names.
     * @param songName song file name with .chrp extention.
     */
    public void play(String songName){
        orchestra.loadMusic("music/"+songName);
        orchestra.play();
    }
}
