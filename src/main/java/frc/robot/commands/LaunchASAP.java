package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.hal.simulation.ConstBufferCallback;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;

public class LaunchASAP extends Command{
    private Intake s_Intake;
    private Launcher s_Launcher;
    private Timer timer;
    private boolean launch;


    public LaunchASAP(Intake s_Intake, Launcher s_Launcher) {
        this.s_Intake = s_Intake;
        this.s_Launcher = s_Launcher;
        this.timer = new Timer();
        this.launch = false;

        addRequirements(s_Intake);
        addRequirements(s_Launcher);
    }

    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();
        launch = false;
        s_Launcher.startLaunch();
        System.out.println("Start LaunchASAP");
    }

    @Override
    public void execute() {
        
        // System.out.println(timer.get());
        if(timer.get() >= Constants.Launcher.LaunchASAPTime && !launch) {
            launch = true;
            s_Intake.pushIntake(false);
            System.out.println("Push");
        }
    }

    @Override
    public void end(boolean interrupted) {
        s_Launcher.endLaunch();
        s_Intake.pushIntake(true);
        System.out.println("End LaunchASAP");
    }

    @Override
    public boolean isFinished() {
        return (timer.get() >= (Constants.Launcher.LaunchASAPTime+Constants.Launcher.LaunchStopTime) && launch);
    }
}
