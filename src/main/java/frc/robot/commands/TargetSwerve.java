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


public class TargetSwerve extends Command {    
    private Swerve s_Swerve;    
    private Launcher s_Launcher;
    private Intake s_Intake;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private DoubleSupplier throtleSupplier;
    private BooleanSupplier robotCentricSup;

    private JoystickButton overrideButton;
    private JoystickButton launchButton;
    

    private double swerve_X_speed;
    private double swerve_Y_speed; 
    private double swerve_X_speedt;
    private double swerve_Y_speedt; 
    private boolean shoot_x;
    private boolean shoot_y;

    private boolean shoot;
    private boolean spin;
    private boolean end;
    private Timer timer;

    

    private Boolean bHold;
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
    public TargetSwerve(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, DoubleSupplier throtleSupplier, BooleanSupplier robotCentricSup, LEDS led, Launcher s_Launcher, Intake s_Intake, JoystickButton overrideButton, JoystickButton launchButton) {
        this.s_Swerve = s_Swerve;
        this.s_Launcher = s_Launcher;
        this.s_Intake = s_Intake;
        addRequirements(s_Swerve);
        addRequirements(s_Launcher);
        addRequirements(s_Intake);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
        this.throtleSupplier = throtleSupplier;
        this.overrideButton = overrideButton;
        this.launchButton = launchButton;
        this.led = led;
    }

    
    @Override
    public void initialize() {
        this.bHold = true;

        Xpid = new PIDController(Constants.TargetSwerve.P, Constants.TargetSwerve.I, Constants.TargetSwerve.D);
        Ypid = new PIDController(Constants.TargetSwerve.P, Constants.TargetSwerve.I, Constants.TargetSwerve.D);

        Xpid.setTolerance(3);
        Ypid.setTolerance(3);

        Xpid.setSetpoint(0);
        Ypid.setSetpoint(0);

        timer = new Timer();
        shoot = false;
        spin = false;
        end = false;

        led.setMode("LT");
        System.out.println("set LT");

        s_Launcher.startLaunch();
        s_Intake.lightPull(false);

        LLtbl = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void execute() {
        if (bHold) bHold = overrideButton.getAsBoolean();
        /* Get Values, Deadband*/
        double translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.stickDeadband);
        double strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.stickDeadband);
        double rotationVal = MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.stickDeadband);
        double throtleVal = 1-((throtleSupplier.getAsDouble()+1)/2);

        double X_from_camera = LLtbl.getValue("tx").getDouble();
        double Y_from_camera = LLtbl.getValue("ty").getDouble();
        

        swerve_X_speed = MathUtil.clamp(Xpid.calculate(X_from_camera), -1,1);
        swerve_Y_speed = MathUtil.clamp(Ypid.calculate(Y_from_camera), -1,1);

        shoot = Xpid.atSetpoint() && Ypid.atSetpoint();
        if(Xpid.atSetpoint()) swerve_X_speed = 0;
        if(Ypid.atSetpoint()) swerve_Y_speed = 0;

        if(shoot && !end && led.getMode()!="LK") {
            led.setMode("LK");
            System.out.println("set LK");
        } else if (!end && led.getMode()!="LT") {
            led.setMode("LT");
            System.out.println("set LT");
        } 

        if(launchButton.getAsBoolean() && !end) {
            s_Intake.pushIntake(false);
            end = true;
            timer.start();
            led.setMode("L");
            System.out.println("set L");
        }

        SmartDashboard.putNumber("Xpid", swerve_X_speed);
        SmartDashboard.putNumber("Ypid", swerve_Y_speed);
        SmartDashboard.putBoolean("Xshoot", Xpid.atSetpoint());
        SmartDashboard.putBoolean("Yshoot", Ypid.atSetpoint());
        SmartDashboard.putBoolean("Shoot", shoot);

        /* Drive */
        s_Swerve.drive(
            new Translation2d(-swerve_Y_speed * throtleVal, -swerve_X_speed * throtleVal).times(Constants.Swerve.maxSpeed), 
            (rotationVal * throtleVal) * Constants.Swerve.maxAngularVelocity, 
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
        
        return (overrideButton.getAsBoolean() && !bHold) || (end && timer.get() >= Constants.Launcher.LaunchStopTime);
    }
}