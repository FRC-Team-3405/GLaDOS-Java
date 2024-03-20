package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.LEDS;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Swerve;


public class TargetSwerveAuto extends Command {    
    private Swerve s_Swerve;    
    private Launcher s_Launcher;
    private Intake s_Intake;

    private double swerve_X_speed;
    private double swerve_Y_speed; 

    private boolean shoot;
    private boolean end;
    private boolean launching;
    private Timer timer;
    private Timer timerL;

    private NetworkTable LLtbl;

    private PIDController Xpid;
    private PIDController Ypid;


    private LEDS led;

    /**
     * Default Teleop mode, takes the swerve and conrolor 
     * 
     * @param s_Swerve swerve base
     * @param translationSup forward and back suplier, typicaly a controlor axis
     * @param strafeSup left and right suplier, typicaly a controlor axis
     * @param rotationSup Rotation suplier, typicaly a controlor axis
     * @param robotCentricSup wether to drive relative to the robot.
     */
    public TargetSwerveAuto(Swerve s_Swerve, LEDS led, Launcher s_Launcher, Intake s_Intake) {
        this.s_Swerve = s_Swerve;
        this.s_Launcher = s_Launcher;
        this.s_Intake = s_Intake;
        addRequirements(s_Swerve);
        addRequirements(s_Launcher);
        addRequirements(s_Intake);

        this.led = led;
    }

    
    @Override
    public void initialize() {

        Xpid = new PIDController(Constants.TargetSwerve.P, Constants.TargetSwerve.I, Constants.TargetSwerve.D);
        Ypid = new PIDController(Constants.TargetSwerve.P, Constants.TargetSwerve.I, Constants.TargetSwerve.D);

        Xpid.setTolerance(3);
        Ypid.setTolerance(3);

        Xpid.setSetpoint(0);
        Ypid.setSetpoint(0);

        timer = new Timer();
        timerL = new Timer();
        shoot = false;
        end = false;
        launching = false;

        led.setMode("LT");
        System.out.println("set LT");

        s_Launcher.startLaunch();
        s_Intake.lightPull(false);

        LLtbl = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void execute() {

        // Get Targert X and Y
        double X_from_camera = LLtbl.getValue("tx").getDouble();
        double Y_from_camera = LLtbl.getValue("ty").getDouble();
        // Calculate PID for movement
        swerve_X_speed = MathUtil.clamp(Xpid.calculate(X_from_camera), -1,1);
        swerve_Y_speed = MathUtil.clamp(Ypid.calculate(Y_from_camera), -1,1);

        // If target reached set shoot to true
        shoot = Xpid.atSetpoint() && Ypid.atSetpoint();
        // if target reached zero motion
        if(Xpid.atSetpoint()) swerve_X_speed = 0;
        if(Ypid.atSetpoint()) swerve_Y_speed = 0;

        // set led modes
        if(shoot && !end && !launching) {
            if(led.getMode()!="LK"){
            led.setMode("LK");
            System.out.println("set LK");
            timerL.reset();
            timerL.start();
            }
        } else if (!end && led.getMode()!="LT" && !launching) {
            led.setMode("LT");
            System.out.println("set LT");
        } 

        SmartDashboard.putNumber("SmartLaunch", timerL.get());
        if(timerL.hasElapsed(Constants.TargetSwerve.SmartLaunchTime) && !launching) {
            s_Intake.pushIntake(false);
            end = true;
            timer.start();
            led.setMode("L");
            System.out.println("set L");
            launching = true;
        }

        SmartDashboard.putNumber("Xpid", swerve_X_speed);
        SmartDashboard.putNumber("Ypid", swerve_Y_speed);
        SmartDashboard.putBoolean("Xshoot", Xpid.atSetpoint());
        SmartDashboard.putBoolean("Yshoot", Ypid.atSetpoint());
        SmartDashboard.putBoolean("Shoot", shoot);

        /* Drive */
        s_Swerve.drive(
            new Translation2d(-swerve_Y_speed * 0.5, -swerve_X_speed * 0.5).times(Constants.Swerve.maxSpeed), 
            0 * Constants.Swerve.maxAngularVelocity, 
            false, 
            false
        );
    }
    
    @Override
    public void end(boolean interrupted) {
        s_Intake.pushIntake(true);
        s_Launcher.endLaunch();
            led.setMode("D");
            System.out.println("set D");
    }

    @Override
    public boolean isFinished() {
        return end && timer.get() >= Constants.Launcher.LaunchStopTime;
    }
}