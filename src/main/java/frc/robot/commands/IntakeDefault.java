package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.function.BooleanSupplier;
import frc.robot.subsystems.Intake;

public class IntakeDefault extends Command {

    private Intake s_Intake;
    private DigitalInput LIM;
    // private Boolean end;
    private Boolean first;

    /**
     * Run intake untill note is obtained or manualy disabled
     * 
     * @param s_Intake Intake object
     */
    public IntakeDefault(Intake s_Intake, DigitalInput LIM) {
        this.s_Intake = s_Intake;
        this.LIM = LIM;
        
        // this.end = false;
        this.first = false;
        addRequirements(s_Intake);
    }

    @Override
    public void initialize() {
        // tell the intake to extend
        System.out.println("Start IntakeDefault");
        s_Intake.setDeploy(false);
        s_Intake.endIntake();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Start IntakeDefault");
    }

    @Override
    public boolean isFinished() {
        // this should trigger when a note is detected, or manualy triggerd
        // return(s_Intake.checkForNote() || switchButton.getAsBoolean());
        return false;
    }
    
}
