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

    public Intake s_Intake;
    
    public Launcher(int lMotorId, int rMotorId) {
        m_launcherLeft = new CANSparkMax(lMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
        m_launcherRight = new CANSparkMax(rMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);

        m_launcherLeft.restoreFactoryDefaults();
        m_launcherRight.restoreFactoryDefaults();
    }


    public void startLaunch() {
        System.out.println("start launch");
        m_launcherLeft.set(-Constants.Launcher.LaunchP);
        m_launcherRight.set(Constants.Launcher.LaunchP);
    }

    public void endLaunch() {
        m_launcherLeft.set(0);
        m_launcherRight.set(0);
    }

    
}
