package frc.robot.commands;

import edu.wpi.first.networktables.PubSub;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.function.BooleanSupplier;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDS;

public class SmartShoot extends Command {


    /**
     * Run intake untill note is obtained or manualy disabled
     * 
     * @param s_Intake Intake object
     */
    public SmartShoot() {
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
