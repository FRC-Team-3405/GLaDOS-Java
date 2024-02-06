package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Launcher extends SubsystemBase {
    public VictorSPX m_flyMotor1;
    public VictorSPX m_flyMotor2;
    public CANSparkMax m_screwMotor;
    public RelativeEncoder screwEncoder;
    public SparkPIDController screwPID;

    public double kP, kI, kD, kMaxOut, kMinOut, maxRPM, maxVel, maxAcc;

    public Launcher(int m_screwMotorId, int m_flyMotor1ID, int m_flyMotor2ID) {
        
        m_screwMotor = new CANSparkMax(m_screwMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
        m_flyMotor1 = new VictorSPX(m_flyMotor1ID);
        m_flyMotor2 = new VictorSPX(m_flyMotor2ID);

        screwEncoder = m_screwMotor.getEncoder();
        screwPID = m_screwMotor.getPIDController();

        kP = Constants.Launcher.screwP;
        kI = Constants.Launcher.screwI;
        kD = Constants.Launcher.screwD;
    
        kMaxOut = Constants.Launcher.screwMaxOut;
        kMinOut = Constants.Launcher.screwMinOut;

        maxRPM = Constants.Launcher.screwMaxRPM;
        maxVel = Constants.Launcher.screwMaxVel;
        maxAcc = Constants.Launcher.screwMaxAcc;

        // set PID coefficients
        screwPID.setP(kP);
        screwPID.setI(kI);
        screwPID.setD(kD);
        screwPID.setOutputRange(kMinOut, kMaxOut);


        
        int smartMotionSlot = 0;
        screwPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        screwPID.setSmartMotionMinOutputVelocity(0, smartMotionSlot);
        screwPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        screwPID.setSmartMotionAllowedClosedLoopError(0, smartMotionSlot);
    }
    
}
