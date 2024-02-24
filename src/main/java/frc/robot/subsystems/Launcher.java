package frc.robot.subsystems;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTableInstance.NetworkMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Launcher extends SubsystemBase {

    public CANSparkMax m_launcherRight;
    public SparkPIDController m_RPID;
    public CANSparkMax m_launcherLeft;
    public SparkPIDController m_LPID;

    // public VictorSPX m_launcherLeft;
    // public VictorSPX m_launcherRight;

    public Intake s_Intake;
    
    public Launcher(int lMotorId, int rMotorId) {
        // --- NEO CODE---
        m_launcherLeft = new CANSparkMax(lMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
        m_launcherRight = new CANSparkMax(rMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);

        m_launcherLeft.restoreFactoryDefaults();
        m_launcherRight.restoreFactoryDefaults();

        // m_LPID = m_launcherLeft.getPIDController();
        // m_RPID = m_launcherRight.getPIDController();
        
        // m_LPID.setP(Constants.Launcher.P);
        // m_LPID.setI(Constants.Launcher.I);
        // m_LPID.setD(Constants.Launcher.D);
        // m_LPID.setIZone(0);
        // m_LPID.setFF(Constants.Launcher.FF);
        // m_LPID.setOutputRange(Constants.Launcher.MinOut, Constants.Launcher.MaxOut);

        // m_RPID.setP(Constants.Launcher.P);
        // m_RPID.setI(Constants.Launcher.I);
        // m_RPID.setD(Constants.Launcher.D);
        // m_RPID.setIZone(0);
        // m_RPID.setFF(Constants.Launcher.FF);
        // m_RPID.setOutputRange(Constants.Launcher.MinOut, Constants.Launcher.MaxOut);

        // m_LPID.setReference(0, CANSparkMax.ControlType.kDutyCycle);
        // m_RPID.setReference(0, CANSparkMax.ControlType.kDutyCycle);

        // --- CIM/SPX CODE ---
        // m_launcherLeft = new VictorSPX(lMotorId);
        // m_launcherRight = new VictorSPX(rMotorId);

        // m_launcherLeft.setNeutralMode(NeutralMode.Coast);
        // m_launcherRight.setNeutralMode(NeutralMode.Coast);

        // m_launcherLeft.setInverted(true);

        // m_launcherLeft.set(VictorSPXControlMode.Velocity,0);
        // m_launcherRight.set(VictorSPXControlMode.Velocity,0);
    }


    public void startLaunch() {
        // m_LPID.setReference(Constants.Launcher.LaunchP, CANSparkMax.ControlType.kDutyCycle);
        // m_RPID.setReference(-Constants.Launcher.LaunchP, CANSparkMax.ControlType.kDutyCycle);
        
        System.out.println("start launch");
        m_launcherLeft.set(-Constants.Launcher.LaunchP);
        m_launcherRight.set(Constants.Launcher.LaunchP);

        // m_launcherLeft.set(VictorSPXControlMode.PercentOutput,Constants.Launcher.LaunchP);
        // m_launcherRight.set(VictorSPXControlMode.PercentOutput,Constants.Launcher.LaunchP);
    }

    public void endLaunch() {
        // m_LPID.setReference(0, CANSparkMax.ControlType.kDutyCycle);
        // m_RPID.setReference(0, CANSparkMax.ControlType.kDutyCycle);

        m_launcherLeft.set(0);
        m_launcherRight.set(0);
        
        // m_launcherLeft.set(VictorSPXControlMode.PercentOutput,0);
        // m_launcherRight.set(VictorSPXControlMode.PercentOutput,0);
    }

    
}
