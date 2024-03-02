package frc.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.ctre.phoenix6.Orchestra;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathPlannerPath;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import java.util.ArrayList;
import java.util.List;

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

    // FALLOVER JUST IN CASE MAIN CONTROLLER FAILS! PLZ CHECK MY SYNTAX SINCE MY COMPUTER DOES NOT HAVE ALL OF THE NESSESARY THINGS INSTALLED :D
    private boolean main_failed = false;

    private final PowerDistribution PDP = new PowerDistribution();

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
    private final int throttleAxis = 2;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, 2);
    private final JoystickButton robotCentric = new JoystickButton(driver, 7);
    private final JoystickButton Test = new JoystickButton(driver, 3);

    // private final NamedCommands NamedCommands = new NamedCommands();

    private final NetworkTableInstance tableInstance = NetworkTableInstance.getDefault();

    /* Subsystems */
    private final LEDS theLEDs = new LEDS(0,60);

    private final Band theBand = new Band();

    private final Swerve s_Swerve = new Swerve(theLEDs);

    public Intake intake = new Intake(Constants.Intake.ActuatorCAN, Constants.Intake.RollerCAN1, Constants.Intake.RollerCAN2,secondary);

    public Launcher launcher = new Launcher(Constants.Launcher.LeftCAN,Constants.Launcher.RightCAN);
    
    public DigitalInput LIM = new DigitalInput(0);

    // private final Intake s_Intake = new Intake(#, #, #, #);

    // private final Launcher s_Launcher = new Launcher(#, #, #);

    // dashboard selector for autos and music
    private final SendableChooser<Command> autoChooser;
    private final SendableChooser<String> musiChooser;


    private Thread m_visionThread;

    /** The container for the robot. Contains subsystems, OI devices, and commands. 
     *      this is the main class most things stem from. only thing above this is the robot.java that 
     *      connects this to the driverstation
    */
    private double getTranslationAxisMaybe() {
        if (main_failed) {
            return secondary.getRawAxis(sLy);
        }
        return -driver.getRawAxis(translationAxis);
    }

    private double getStrafeAxisMaybe() {
        if (main_failed) {
            return secondary.getRawAxis(sLX);
        }
        return -driver.getRawAxis(strafeAxis);
    }

    private double getRotationAxisMaybe() {
        if (main_failed) {
            return secondary.getRawAxis(sRX);
        }
        return -driver.getRawAxis(rotationAxis);
    }

    private double getThrottleAxisMaybe() {
        if (main_failed) {
            return 1.00;
        }
        return -driver.getRawAxis(throttleAxis);
    }

    public RobotContainer() {
        /**sets the Teleop command, uses TeleopSwever class to connect the swerve drive and controls
         * runs the Swerve.Drive() function periodicly 
         */
        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> getTranslationAxisMaybe(), 
                () -> getStrafeAxisMaybe(), 
                () -> getRotationAxisMaybe(), 
                () -> getThrottleAxisMaybe(), 
                () -> robotCentric.getAsBoolean(),
                theLEDs
            )
        );
        
        intake.setDefaultCommand(new IntakeDefault(intake,LIM));

        NamedCommands.registerCommand("RunIntake", new IntakeRun(intake, LIM, new JoystickButton(secondary, 3), theLEDs));
        NamedCommands.registerCommand("EndIntake", new IntakeDefault(intake, LIM));
        NamedCommands.registerCommand("LaunchASAP", new LaunchASAP(intake,launcher,theLEDs));


        // Configure the button bindings
        configureButtonBindings();

        //Build the auto chooser
        // autoChooser = AutoBuilder.buildAutoChooser();
        autoChooser = AutoBuilder.buildAutoChooser("Btm4");
        SmartDashboard.putData("Auto Chooser", autoChooser);

        musiChooser = theBand.Buildchoser();
        SmartDashboard.putData("Music Choser",musiChooser);

        secondary.getPOV();

        // theLEDs.SetFull(255, 60, 0);
        theLEDs.setMode("D");


        m_visionThread =
        new Thread(
            () -> {
              // Get the UsbCamera from CameraServer
              UsbCamera camera = CameraServer.startAutomaticCapture();
              // Set the resolution
              camera.setResolution(640/2, 480/2);

              // Get a CvSink. This will capture Mats from the camera
              CvSink cvSink = CameraServer.getVideo();
              // Setup a CvSource. This will send images back to the Dashboard
              CvSource outputStream = CameraServer.putVideo("Note", 640/2, 480/2);

              // Mats are very memory expensive. Lets reuse this Mat.
              Mat mat = new Mat();

              // This cannot be 'true'. The program will never exit if it is. This
              // lets the robot stop this thread when restarting robot code or
              // deploying.
              while (!Thread.interrupted()) {
                // Tell the CvSink to grab a frame from the camera and put it
                // in the source mat.  If there is an error notify the output.
                if (cvSink.grabFrame(mat) == 0) {
                  // Send the output the error.
                  outputStream.notifyError(cvSink.getError());
                  // skip the rest of the current iteration
                  continue;
                }
                // Imgproc.rectangle(
                //     mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
                // List<MatOfPoint> list = new ArrayList<MatOfPoint>();
                // list.add( new MatOfPoint (
                //     new Point(208, 71), new Point(421, 161),
                //     new Point(226, 232), new Point(332, 52),
                //     new Point(363, 250)));
                // Imgproc.polylines(mat, list, false, new Scalar(255,255,255), 2);
                // // Give the output stream a new image to display
                outputStream.putFrame(mat);
              }
            });
        m_visionThread.setDaemon(true);
        m_visionThread.start();
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
        // new JoystickButton(secondary, 4).onTrue(new LaunchASAP(intake,launcher,theLEDs));
        new JoystickButton(secondary, 4).onTrue(new LaunchControled(intake,launcher,theLEDs,new JoystickButton(secondary, 4),new JoystickButton(secondary, 6)));
        new JoystickButton(secondary, 2).onTrue(new IntakeRun(intake, LIM, new JoystickButton(secondary, 2),theLEDs));
        new JoystickButton(secondary, 3).onTrue(new IntakeFix(intake, LIM, new JoystickButton(secondary, 3),new JoystickButton(secondary, 6),theLEDs));
        new JoystickButton(secondary, 1).onTrue(new IntakeAmp(intake, LIM, new JoystickButton(secondary, 1), new JoystickButton(secondary, 6),theLEDs));
<<<<<<< Updated upstream

        // NEW STUFF I ADDED! PLZ CHECK BECAUSE I KNOW I DID NOT DO THIS RIGHT! 
        //I DONT WANT TO SHOVE THIS IN A RANDOM COMMAND FOR FEAR THAT IT WILL NO LONGER BE ABLE TO ACCESS THE main_failed VARIABLE
        if (secondary.getLeftBumperPressed() && main_failed) {
            main_failed = false;
        } else {
            main_failed = true;
        }
=======
        new JoystickButton(secondary, 8).onTrue(smartLaunch());
>>>>>>> Stashed changes
    }

    public void updateInfo() {
        SmartDashboard.putBoolean("IntakeDeployed", intake.getMode());
        SmartDashboard.putBoolean("Note", LIM.get());
        SmartDashboard.putBoolean("Launcher Spin", launcher.spin);

        // {"D","IO","IR","L","LS"};
        SmartDashboard.putBoolean("D", theLEDs.getMode() == "D");
        SmartDashboard.putBoolean("IO", theLEDs.getMode() == "IO");
        SmartDashboard.putBoolean("IR", theLEDs.getMode() == "IR");
        SmartDashboard.putBoolean("IF", theLEDs.getMode() == "IF");
        SmartDashboard.putBoolean("IA", theLEDs.getMode() == "IA");
        SmartDashboard.putBoolean("N", theLEDs.getMode() == "N");
        SmartDashboard.putBoolean("LS", theLEDs.getMode() == "LS");
        SmartDashboard.putBoolean("L", theLEDs.getMode() == "L");
        
        SmartDashboard.putData("intake", intake);
        SmartDashboard.putData("Launcher", launcher);
        SmartDashboard.putData("Gyro",s_Swerve.gyro);
        SmartDashboard.putBoolean("ControlorA", new JoystickButton(secondary, 1).getAsBoolean());
        SmartDashboard.putBoolean("ControlorB", new JoystickButton(secondary, 2).getAsBoolean());
        SmartDashboard.putBoolean("ControlorX", new JoystickButton(secondary, 3).getAsBoolean());

        // System.out.println("Radio");
        // System.out.println(PDP.getCurrent(15));
        // System.out.println("RIO");
        // System.out.println(PDP.getCurrent(20));
        // System.out.println("Total");
        // System.out.println(PDP.getTotalCurrent());

        intake.updateData();

        double throtleVal = (driver.getRawAxis(throttleAxis)+1)/2;
        SmartDashboard.putNumber("Throttle", throtleVal);
        
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

    public Command smartLaunch() {
        // Load the path you want to follow using its name in the GUI
        PathPlannerPath path = PathPlannerPath.fromPathFile("Example Path");

        // Create a path following command using AutoBuilder. This will also trigger event markers.
        return AutoBuilder.followPath(path);
    }

    public void teleopInit() {
        theLEDs.SetFull(0, 0, 255);
        theLEDs.setMode("D");
        theLEDs.updateMode();
    }

    public void teleopPeriodic() {
        // theLEDs.rainbow();
        theLEDs.updateMode();
    }

    public void autonomousPeriodic() {
        // theLEDs.rainbow();
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
