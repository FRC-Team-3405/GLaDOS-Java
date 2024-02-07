package frc.robot;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;
import frc.lib.util.COTSTalonFXSwerveConstants;
import frc.lib.util.SwerveModuleConstants;

public final class Constants {
    public static final double stickDeadband = 0.1;

    public static final class Swerve {
        public static final int pigeonID = 20;

        public static final COTSTalonFXSwerveConstants chosenModule =  //TODOx: This must be tuned to specific robot
        COTSTalonFXSwerveConstants.SDS.MK4i.Falcon500(COTSTalonFXSwerveConstants.SDS.MK4i.driveRatios.L2);

        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(24.5); //TODOx: This must be tuned to specific robot
        public static final double wheelBase = Units.inchesToMeters(24.5); //TODOx: This must be tuned to specific robot
        public static final double wheelCircumference = chosenModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenModule.driveGearRatio;
        public static final double angleGearRatio = chosenModule.angleGearRatio;

        /* Motor Inverts */
        public static final InvertedValue angleMotorInvert = chosenModule.angleMotorInvert;
        public static final InvertedValue driveMotorInvert = chosenModule.driveMotorInvert;

        /* Angle Encoder Invert */
        public static final SensorDirectionValue cancoderInvert = chosenModule.cancoderInvert;

        /* Swerve Current Limiting */
        public static final int angleCurrentLimit = 15;
        public static final int angleCurrentThreshold = 30;
        public static final double angleCurrentThresholdTime = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveCurrentLimit = 20;
        public static final int driveCurrentThreshold = 40;
        public static final double driveCurrentThresholdTime = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = chosenModule.angleKP;
        public static final double angleKI = chosenModule.angleKI;
        public static final double angleKD = chosenModule.angleKD;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.0; //TODO: This must be tuned to specific robot
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values From SYSID */
        public static final double driveKS = 0.32; //TODO: This must be tuned to specific robot
        public static final double driveKV = 1.51;
        public static final double driveKA = 0.27;

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 4.5; //TODOx: This must be tuned to specific robot DONE
        /** Radians per Second */
        public static final double maxAngularVelocity = 10.0; //TODOx: This must be tuned to specific robot DONE

        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Coast;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;

        /* Module Specific Constants */
        /* Front Left Module - Module 0 */
        public static final class Mod0 { //TODOx: This must be tuned to specific robot DONE
            public static final int driveMotorID = 3; 
            public static final int angleMotorID = 4; 
            public static final int canCoderID = 18; 
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(11.25);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Front Right Module - Module 1 */
        public static final class Mod1 { //TODOx: This must be tuned to specific robot DONE
            public static final int driveMotorID = 1; 
            public static final int angleMotorID = 2;
            public static final int canCoderID = 17;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-93.25);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
        
        /* Back Left Module - Module 2 */
        public static final class Mod2 { //TODOx: This must be tuned to specific robot DONE
            public static final int driveMotorID = 5;
            public static final int angleMotorID = 6;
            public static final int canCoderID = 16;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(-42.45);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Right Module - Module 3 */
        public static final class Mod3 { //TODOx: This must be tuned to specific robot DONE
            public static final int driveMotorID = 7;
            public static final int angleMotorID = 8;
            public static final int canCoderID = 19;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(55.89);
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
    }

    public static final class Intake {
        public static final double noteDetectionDistance = 30;
        public static final double barP = 5e-5;
        public static final double barI = 1e-6;
        public static final double barD = 0;

        public static final double barMaxOut = 1;
        public static final double barMinOut = -1;

        public static final double barMaxRPM = 5700;
        public static final double barMaxVel = 2000;
        public static final double barMaxAcc = 1500;

        public static final double barOutPoint = 1000;
        public static final double barInPoint = -1000;

        public static final double intakeVel = 2;
    }


    public static final class Launcher {
        public static final double noteDetectionDistance = 30;
        public static final double screwP = 5e-5;
        public static final double screwI = 1e-6;
        public static final double screwD = 0;

        public static final double screwMaxOut = 1;
        public static final double screwMinOut = -1;

        public static final double screwMaxRPM = 3000;
        public static final double screwMaxVel = 750;
        public static final double screwMaxAcc = 500;

        public static final double screwRotPerDeg = 5;

        public static final double launcerVel = 2;
    }

    // holds constants for mode system
    public static final class Mode {
        public static final double blinkTime = 5.0;
        // Mode modifiers, in percentages, controlor input is multiplied by these
        // max forward speed (1) * 40% speed (.40) = .40 output
        public static final class Modifiers {
            public static final class Intake {
                public static final double driveSpeed = 0.4;
                public static final double rotSpeed = 0.4;
            }
            public static final class Lock {
                public static final double driveSpeed = 0.3;
                public static final double rotSpeed = 0.0;
            }
        }

        public static final class Colors {
            public static final Color D = Color.kPurple; // Default Color
            public static final Color I = Color.kRed;    // Intake Color
            public static final Color IO = Color.kDarkRed;    // Intake Out Blink Color
            public static final Color R = Color.kOrange; // Ready/Note Color
            public static final Color RB = Color.kDarkOrange; // Ready Blink Color
            public static final Color KP = Color.kYellow;// Lock Pending color
            public static final Color K = Color.kGreen;  // Lock color
            public static final Color L = Color.kSkyBlue;// Launching color

            public static final Color C = Color.kYellow; // Climber Blink Color
            public static final Color CE = Color.kRed;   // Climber Extenidng other Blink Color
            public static final Color CR = Color.kOrange;   // Climber Ready other Blink Color
            public static final Color CC = Color.kSkyBlue;   // Climber Climbing other Blink Color
            public static final Color CL = Color.kGreen;   // Climber Locked other Blink Color

        }
    }

    public static final class AutoConstants { //TODO: The below constants are used in the example auto, and must be tuned to specific robot
        public static final double kMaxSpeedMetersPerSecond = 2;
        public static final double kMaxAccelerationMetersPerSecondSquared = 2;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = 1;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }

}
