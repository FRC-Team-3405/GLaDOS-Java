package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeRun extends Command {

    private Intake s_Intake;

    /**
     * Run intake untill note is obtained or manualy disabled
     * 
     * @param s_Intake Intake object
     */
    public IntakeRun(Intake s_Intake) {
        this.s_Intake = s_Intake;
    }

    @Override
    public void initialize() {
        // tell the intake to extend
        s_Intake.setDeploy(true);
    }

    @Override
    public void execute() {
        // spin the intake 
        s_Intake.runIntake();
    }

    @Override
    public void end(boolean interrupted) {
        // tell the intake to retratct
        s_Intake.setDeploy(false);
    }

    @Override
    public boolean isFinished() {
        // this should trigger when a note is detected, or manualy triggerd
        return(s_Intake.checkForNote() || false/*<--- insert manual overide check here*/);
    }
    
}
