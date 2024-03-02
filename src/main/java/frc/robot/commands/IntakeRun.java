package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.function.BooleanSupplier;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDS;

public class IntakeRun extends Command {

    private Intake s_Intake;
    private DigitalInput LIM;
    private JoystickButton overrideButton;
    // private Boolean end;
    private Boolean bHold;
    private Boolean end;
    private Timer timer;
    private LEDS theLEDs;

    /**
     * Run intake untill note is obtained or manualy disabled
     * 
     * @param s_Intake Intake object
     */
    public IntakeRun(Intake s_Intake, DigitalInput LIM, JoystickButton overrideButton, LEDS theLEDs) {
        this.s_Intake = s_Intake;
        this.LIM = LIM;
        this.overrideButton = overrideButton;
        this.theLEDs = theLEDs;
        // this.end = false;
        this.bHold = true;
        this.end = false;
        addRequirements(s_Intake);
    }

    @Override
    public void initialize() {
        // tell the intake to extend
        System.out.println("Start IntakeRun");
        s_Intake.setDeploy(true);
        s_Intake.startIntake();
        this.bHold = true;
        this.end = false;
        timer = new Timer();
        theLEDs.setMode("IO");
        // timer.start();
    }

    @Override
    public void execute() {
        if (bHold) bHold = overrideButton.getAsBoolean();
        if (!LIM.get() && !end) {
            timer.start();
            s_Intake.endIntake();
            end = true;
            System.out.println("End1");
            theLEDs.setMode("IR");
            System.out.println("set IR");
            s_Intake.lightPull(false);
            
        }
        System.out.println(timer.get());
        System.out.println(timer.get() >= Constants.Intake.OverRun);
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("End IntakeRun");
        s_Intake.lightPull(true);
        if (end) {
            theLEDs.setMode("N");
            System.out.println("set N");
        } else {
            theLEDs.setMode("D");
            System.out.println("set D");
        }
    }

    @Override
    public boolean isFinished() {
        // this should trigger when a note is detected, or manualy triggerd
        // return(s_Intake.checkForNote() || switchButton.getAsBoolean());
        if (!LIM.get()) System.out.println("LIM");
        if (overrideButton.getAsBoolean()) System.out.println("OVER");
        return timer.get() >= Constants.Intake.OverRun || (overrideButton.getAsBoolean() && !bHold);
    }
    
}
