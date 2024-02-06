package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

public class Intake extends SubsystemBase{

    public VictorSPX m_intakeMotor1;
    public VictorSPX m_intakeMotor2;
    public CANSparkMax m_barMotor;
    public RelativeEncoder barEncoder;
    public SparkPIDController barPID;
    public boolean deployed;
    public AnalogInput ultrasonicSensor;

    public double kP, kI, kD, kMaxOut, kMinOut, maxRPM, maxVel, maxAcc;


    public Intake(int m_barMotorId, int m_intakeMotor1ID, int m_intakeMotor2ID, int ultrasonicSensorID) {
        SmartDashboard.putNumber("test",3);
        m_barMotor = new CANSparkMax(m_barMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
        m_intakeMotor1 = new VictorSPX(m_intakeMotor1ID);
        m_intakeMotor2 = new VictorSPX(m_intakeMotor2ID);
        ultrasonicSensor = new AnalogInput(ultrasonicSensorID);

        barEncoder = m_barMotor.getEncoder();
        barPID = m_barMotor.getPIDController();

        kP = Constants.Intake.barP;
        kI = Constants.Intake.barI;
        kD = Constants.Intake.barD;
    
        kMaxOut = Constants.Intake.barMaxOut;
        kMinOut = Constants.Intake.barMinOut;

        maxRPM = Constants.Intake.barMaxRPM;
        maxVel = Constants.Intake.barMaxVel;
        maxAcc = Constants.Intake.barMaxAcc;

        // set PID coefficients
        barPID.setP(kP);
        barPID.setI(kI);
        barPID.setD(kD);
        barPID.setOutputRange(kMinOut, kMaxOut);


        
        int smartMotionSlot = 0;
        barPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        barPID.setSmartMotionMinOutputVelocity(0, smartMotionSlot);
        barPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        barPID.setSmartMotionAllowedClosedLoopError(0, smartMotionSlot);
        
    }

    public void runIntake() {
        m_intakeMotor1.set(VictorSPXControlMode.Velocity, Constants.Intake.intakeVel);
        m_intakeMotor2.set(VictorSPXControlMode.Velocity, Constants.Intake.intakeVel);
    }

    public void breakIntake(){
        m_intakeMotor1.setNeutralMode(NeutralMode.Brake);
        m_intakeMotor2.setNeutralMode(NeutralMode.Brake);
    }

    public boolean checkForNote() {
        return (ultrasonicSensor.getValue() <= Constants.Intake.noteDetectionDistance);
    }

    public void toggleDeploy() {
        if(!deployed) {
            // refrence this code for precice neo control through the spark max
            // https://github.com/REVrobotics/SPARK-MAX-Examples/blob/master/Java/Smart%20Motion%20Example/src/main/java/frc/robot/Robot.java

            barPID.setReference(Constants.Intake.barOutPoint, CANSparkMax.ControlType.kSmartMotion);
            deployed = true;
        } else {
            barPID.setReference(Constants.Intake.barInPoint, CANSparkMax.ControlType.kSmartMotion);
            deployed = false;
        }
    }

    public void setDeploy(boolean mode) {
        if(mode && !deployed) {
            barPID.setReference(Constants.Intake.barOutPoint, CANSparkMax.ControlType.kSmartMotion);
            deployed = true;
        } else if(!mode && deployed) {
            barPID.setReference(Constants.Intake.barInPoint, CANSparkMax.ControlType.kSmartMotion);
            deployed = false;
        }
    }
    


}
