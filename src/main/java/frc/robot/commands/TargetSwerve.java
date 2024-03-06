package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.LEDS;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Swerve;


public class TargetSwerve extends Command {    
    private Swerve s_Swerve;    
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private DoubleSupplier throtleSupplier;
    private BooleanSupplier robotCentricSup;

    private JoystickButton overrideButton;
    

    private double swerve_X_speed;
    private double swerve_Y_speed; 
    private boolean shoot_x;
    private boolean shoot_y;
    private boolean shoot;
    private Boolean bHold;


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
    public TargetSwerve(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, DoubleSupplier throtleSupplier, BooleanSupplier robotCentricSup, LEDS led, JoystickButton overrideButton) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
        this.throtleSupplier = throtleSupplier;
        this.overrideButton = overrideButton;
        this.led = led;
    }

    
    @Override
    public void initialize() {
        this.bHold = true;
    }

    @Override
    public void execute() {
        if (bHold) bHold = overrideButton.getAsBoolean();
        /* Get Values, Deadband*/
        double translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.stickDeadband);
        double strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.stickDeadband);
        double rotationVal = MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.stickDeadband);
        double throtleVal = 1-((throtleSupplier.getAsDouble()+1)/2);
        // SmartDashboard.putNumber("Throttle", throtleVal);

        // SmartDashboard.get
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("priorityid").setNumber(7);
        NetworkTable LLtbl = NetworkTableInstance.getDefault().getTable("limelight");
        // int tag_id = LLtbl.getEntry("tid").
        // double X_from_camera = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_robotspace").getDoubleArray(new double[1]);
        // double Y_from_camera = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_robotspace").getDoubleArray(new double[1]);
        // double X_rotation_from_camera = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose_robotspace").getDoubleArray(new double[1]); 

        double X_from_camera = LLtbl.getValue("tx").getDouble();
        double Y_from_camera = LLtbl.getValue("ty").getDouble();
        
        if (X_from_camera <= 0.5) {
            if (X_from_camera >= 2) {
                swerve_X_speed = 1;
            } else {
                swerve_X_speed = 1.5 / X_from_camera + 1;
            }
        } else if (X_from_camera >= -0.5) {
            if (X_from_camera <= -2) {
                swerve_X_speed = -1;
            } else {
                swerve_X_speed = 1.5 / X_from_camera - 1;
            }
        } else {
            shoot_x = true;
        }

        if (Y_from_camera <= 0.5) {
            if (Y_from_camera >= 2) {
                swerve_Y_speed = 1;
            } else {
                swerve_Y_speed = 1.5 / Y_from_camera + 1;
            }
        } else if (Y_from_camera >= -0.5) {
            if (Y_from_camera <= -2) {
                swerve_Y_speed = -1;
            } else {
                swerve_Y_speed = 1.5 / Y_from_camera - 1;
            }
        } else {
            shoot_y = true;
        }

        if (shoot_y && shoot_x) {
            shoot = true;
        }

        /* Drive */
        s_Swerve.drive(
            new Translation2d(swerve_X_speed, swerve_Y_speed).times(Constants.Swerve.maxSpeed), 
            (rotationVal * throtleVal) * Constants.Swerve.maxAngularVelocity, 
            !robotCentricSup.getAsBoolean(), 
            true
        );
    }

    @Override
    public boolean isFinished() {
        return (overrideButton.getAsBoolean() && !bHold);
    }
}