package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.hal.simulation.ConstBufferCallback;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDS;
import frc.robot.subsystems.Launcher;

public class LaunchControled extends Command{
    private Intake s_Intake;
    private Launcher s_Launcher;
    private Timer timer;
    private boolean launch;
    private boolean bHold;
    private LEDS theLEDs;
    private JoystickButton overrideButton;
    private JoystickButton launchButton;


    public LaunchControled(Intake s_Intake, Launcher s_Launcher, LEDS theLEDs, JoystickButton overrideButton, JoystickButton launchButton) {
        this.s_Intake = s_Intake;
        this.s_Launcher = s_Launcher;
        this.timer = new Timer();
        this.launch = false;
        this.theLEDs = theLEDs;
        this.bHold = true;
        this.overrideButton = overrideButton;
        this.launchButton = launchButton;

        addRequirements(s_Intake);
        addRequirements(s_Launcher);
    }

    @Override
    public void initialize() {
        timer = new Timer();
        // timer.start();
        launch = false;
        bHold = true;
        s_Intake.lightPull(false);
        s_Launcher.startLaunch();
        System.out.println("Start LaunchASAP");
        theLEDs.setMode("LS");
        System.out.println("set LS");
        
    }

    @Override
    public void execute() {
        if (bHold) bHold = overrideButton.getAsBoolean();
        if (launchButton.getAsBoolean()) {
            timer.start();
            launch = true;
            theLEDs.setMode("L");
            System.out.println("set L");
            s_Intake.pushIntake(false);
            System.out.println("Push");
        }
    }

    @Override
    public void end(boolean interrupted) {
        s_Launcher.endLaunch();
        s_Intake.pushIntake(true);
        System.out.println("End LaunchControled");
        theLEDs.setMode("D");
        System.out.println("set D");
    }

    @Override
    public boolean isFinished() {
        return (timer.get() >= (Constants.Launcher.LaunchStopTime) && launch) || (overrideButton.getAsBoolean() && !bHold);
    }
}
