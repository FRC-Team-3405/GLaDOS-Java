package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.LEDS;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;


public class TeleopSwerve extends Command {    
    private Swerve s_Swerve;    
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private DoubleSupplier throtleSupplier;
    private BooleanSupplier robotCentricSup;
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
    public TeleopSwerve(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, DoubleSupplier throtleSupplier, BooleanSupplier robotCentricSup, LEDS led) {
        this.s_Swerve = s_Swerve;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
        this.throtleSupplier = throtleSupplier;
        this.led = led;
    }

    @Override
    public void execute() {
        /* Get Values, Deadband*/
        double translationVal = MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.stickDeadband);
        double strafeVal = MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.stickDeadband);
        double rotationVal = MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.stickDeadband);
        double throtleVal = 1-((throtleSupplier.getAsDouble()+1)/2);
        // SmartDashboard.putNumber("Throttle", throtleVal);

        /* Drive */
        s_Swerve.drive(
            new Translation2d(translationVal*throtleVal, strafeVal*throtleVal).times(Constants.Swerve.maxSpeed), 
            (rotationVal * throtleVal) * Constants.Swerve.maxAngularVelocity, 
            !robotCentricSup.getAsBoolean(), 
            true
        );
    }
}