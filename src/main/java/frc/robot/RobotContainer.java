package frc.robot;

import com.ctre.phoenix6.Orchestra;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */

    // This needs to be cleaned up to a sendable choser for the smart dashboard to use
    // also once perephials are added, will need to add 2nd note manipulation controler

    private final Joystick driver = new Joystick(0);
    private final XboxController secondary = new XboxController(1);


    // /* XBOX CONTROLOR */
    /* Drive Controls */
    private final int sLy = XboxController.Axis.kLeftY.value;
    private final int sLX = XboxController.Axis.kLeftX.value;
    private final int sRX = XboxController.Axis.kRightX.value;
    private final int sRY = XboxController.Axis.kRightY.value;

    // /* Driver Buttons */
    // private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    // private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);

    /* FLIGHTSTICK CONTROLOR */
    /* Drive Controls */
    private final int translationAxis = Joystick.AxisType.kY.value;
    private final int strafeAxis = Joystick.AxisType.kX.value;
    private final int rotationAxis = Joystick.AxisType.kTwist.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, Joystick.ButtonType.kTrigger.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, Joystick.ButtonType.kTop.value);
    private final JoystickButton Test = new JoystickButton(driver, 3);

    /* Subsystems */
    private final LEDS theLEDs = new LEDS(9,60);

    private final Band theBand = new Band();

    private final Swerve s_Swerve = new Swerve(theLEDs);

    // private final Intake s_Intake = new Intake(#, #, #, #);

    // private final Launcher s_Launcher = new Launcher(#, #, #);

    // dashboard selector for autos and music
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<String> musiChooser;

    /** The container for the robot. Contains subsystems, OI devices, and commands. 
     *      this is the main class most things stem from. only thing above this is the robot.java that 
     *      connects this to the driverstation
    */
    public RobotContainer() {
        /**sets the Teleop command, uses TeleopSwever class to connect the swerve drive and controls
         * runs the Swerve.Drive() function periodicly 
         */
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean(),
                theLEDs
            )
        );

        // Configure the button bindings
        configureButtonBindings();

        //Build the auto chooser
        autoChooser = AutoBuilder.buildAutoChooser();
        // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");
        SmartDashboard.putData("Auto Chooser", autoChooser);

        musiChooser = theBand.Buildchoser();
        SmartDashboard.putData("Music Choser",musiChooser);

        theLEDs.SetFull(255, 60, 0);
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /** Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
        
    }

    public void updateInfo() {
        SmartDashboard.putBoolean("HasNote", false /*s_Intake.checkForNote()*/);
        SmartDashboard.putBoolean("IntakeDeployed", false/*s_Intake.deployed*/);
        SmartDashboard.putNumber("LauncherAngle", 45/*s_Launcher.getAngle()*/);

        // {"D","IO","IL","N","R","K","KP","L","CE","CR","C","CL"};
        SmartDashboard.putBoolean("D", theLEDs.getMode() == "D");
        SmartDashboard.putBoolean("IO", theLEDs.getMode() == "IO");
        SmartDashboard.putBoolean("IL", theLEDs.getMode() == "IL");
        SmartDashboard.putBoolean("N", theLEDs.getMode() == "N");
        SmartDashboard.putBoolean("R", theLEDs.getMode() == "R");
        SmartDashboard.putBoolean("K", theLEDs.getMode() == "K");
        SmartDashboard.putBoolean("KP", theLEDs.getMode() == "KP");
        SmartDashboard.putBoolean("L", theLEDs.getMode() == "L");
        SmartDashboard.putBoolean("CE", theLEDs.getMode() == "CE");
        SmartDashboard.putBoolean("CR", theLEDs.getMode() == "CR");
        SmartDashboard.putBoolean("C", theLEDs.getMode() == "C");
        SmartDashboard.putBoolean("CL", theLEDs.getMode() == "CL");
        
    }   

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    // public Command getAutonomousCommand() {
    //     // An ExampleCommand will run in autonomous
    //     return new exampleAuto(s_Swerve);
    // }

    /**
     * Gets the selected Auto from the {@link SmartDashboard}'s {@link SendableChooser}
     * 
     * @return The comand to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public void teleopPeriodic() {
        // theLEDs.rainbow();
    }

    public void autonomousPeriodic() {
        theLEDs.rainbow();
    }
    
    public void testInit() {
        theBand.play(musiChooser.getSelected());
    }

    public void testPeriodic() {
        theLEDs.rainbow();
    }

    public void disabledInit () {
        theLEDs.SetFull(255, 60, 0);
        theBand.orchestra.stop();
    }

    // public void PlayMusic(int sel) {
    //     orch.loadMusic(songs[sel]);
    //     orch.play();
    // }
    
}
