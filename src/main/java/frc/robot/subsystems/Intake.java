package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.function.IntSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;

public class Intake extends SubsystemBase{

    public VictorSPX m_intakeMotor1;
    public VictorSPX m_intakeMotor2;
    public CANSparkMax m_barMotor;
    public RelativeEncoder barEncoder;
    // public SparkPIDController barPID;
    private boolean deployed;

    private XboxController secondaryController;

    public double kP, kI, kD, kMaxOut, kMinOut, maxRPM, maxVel, maxAcc;


    public Intake(int m_barMotorId, int m_intakeMotor1ID, int m_intakeMotor2ID, XboxController secondaryControllerIn) {
        // SmartDashboard.putNumber("test",3);
        m_barMotor = new CANSparkMax(m_barMotorId, com.revrobotics.CANSparkLowLevel.MotorType.kBrushless);
        m_intakeMotor1 = new VictorSPX(m_intakeMotor1ID);
        m_intakeMotor2 = new VictorSPX(m_intakeMotor2ID);

        m_barMotor.restoreFactoryDefaults();
        m_barMotor.setIdleMode(IdleMode.kBrake);

        secondaryController = secondaryControllerIn;

        barEncoder = m_barMotor.getEncoder();
        // barPID = m_barMotor.getPIDController();

        // kP = Constants.Intake.barP;
        // kI = Constants.Intake.barI;
        // kD = Constants.Intake.barD;
    
        // kMaxOut = Constants.Intake.barMaxOut;
        // kMinOut = Constants.Intake.barMinOut;

        // maxRPM = Constants.Intake.barMaxRPM;
        // maxVel = Constants.Intake.barMaxVel;
        // maxAcc = Constants.Intake.barMaxAcc;

        // set PID coefficients
        // barPID.setP(kP);
        // barPID.setI(kI);
        // barPID.setD(kD);
        // barPID.setIZone(0);
        // barPID.setFF(0);
        // barPID.setOutputRange(kMinOut, kMaxOut);


        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    
        m_barMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, Constants.Intake.forwardLim);
        m_barMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, Constants.Intake.reversLim);
        
        // barPID.setReference(-0.3, CANSparkMax.ControlType.kDutyCycle);
    }

    public void startIntake() {
        m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, Constants.Intake.intakeVel);
        m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, Constants.Intake.intakeVel);
        // barPID.setReference(0.3, CANSparkMax.ControlType.kDutyCycle);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
        m_barMotor.set(Constants.Intake.acuateVel);
        
        System.out.println("Start");
    }
    
    public void endIntake() {
        m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, 0);
        m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, 0);
        // barPID.setReference(-0.3, CANSparkMax.ControlType.kDutyCycle);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        m_barMotor.set(-Constants.Intake.acuateVel);
        System.out.println("End");
    }

    public void runIntake() {
        // m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, SmartDashboard.getBoolean("Forward Soft Limit Enabled", true));
        // m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, SmartDashboard.getBoolean("Reverse Soft Limit Enabled", true));
        m_barMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)SmartDashboard.getNumber("Forward Soft Limit", 15));
        m_barMotor.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)SmartDashboard.getNumber("Reverse Soft Limit", 0));
    }

    public void breakIntake(){
        m_intakeMotor1.setNeutralMode(NeutralMode.Brake);
        m_intakeMotor2.setNeutralMode(NeutralMode.Brake);
    }

    public boolean checkForNote() {
        return false;
    }

    public void pushIntake(boolean stop) {
        if(!stop) {
        m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, -Constants.Intake.intakeVel);
        m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, -Constants.Intake.intakeVel*0.5);
        } else {
        m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, 0);
        m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, 0);
        }
    }

    public void toggleDeploy() {
        if(!deployed) {
            deployed = true;
            startIntake();
        } else {
            deployed = false;
            endIntake();
        }
    }

    public void nudge() {
        if (secondaryController.getPOV() != -1) {
            System.out.println(secondaryController.getPOV());
        }
        if (secondaryController.getPOV() == 0) {
            m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, -Constants.Intake.intakeNudge);
            m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, -Constants.Intake.intakeNudge);
        } else if (secondaryController.getPOV() == 180){
            m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, Constants.Intake.intakeNudge);
            m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, Constants.Intake.intakeNudge);
        } else {
            m_intakeMotor1.set(VictorSPXControlMode.PercentOutput, 0);
            m_intakeMotor2.set(VictorSPXControlMode.PercentOutput, 0);
        }
    }

    public void manualAcuation() {
        if (secondaryController.getPOV() != -1) {
            System.out.println(secondaryController.getPOV());
        }
        if (secondaryController.getPOV() == 90) {
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        m_barMotor.set(-Constants.Intake.acuateManual);
        } else if (secondaryController.getPOV() == 270){
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        m_barMotor.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
        m_barMotor.set(Constants.Intake.acuateManual);
        } else {
        m_barMotor.set(0);
        }
    }

    public void setDeploy(boolean mode) {
        if(mode && !deployed) {
            deployed = true;
        } else if(!mode && deployed) {
            deployed = false;
        }
    }

    public boolean getMode() {
        return deployed;
    }

    public void updateData() {
        SmartDashboard.putBoolean("Forward Soft Limit Enabled",
                                  m_barMotor.isSoftLimitEnabled(CANSparkMax.SoftLimitDirection.kForward));
        SmartDashboard.putBoolean("Reverse Soft Limit Enabled",
                                  m_barMotor.isSoftLimitEnabled(CANSparkMax.SoftLimitDirection.kReverse));                          
        SmartDashboard.putNumber("Forward Soft Limit",
                                  m_barMotor.getSoftLimit(CANSparkMax.SoftLimitDirection.kForward));
        SmartDashboard.putNumber("Reverse Soft Limit",
                                  m_barMotor.getSoftLimit(CANSparkMax.SoftLimitDirection.kReverse));
    }
    


}
