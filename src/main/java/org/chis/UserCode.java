package org.chis;


import org.chis.util.CANSparkMax;
import org.chis.util.CANSparkMax.MotorType;


public class UserCode {
    
    CANSparkMax neo;

    double targetDeg, currentDeg, prevDeg = 0;

    double kP = 0.02;
    double kI = 0.4;
    double kD = 0.0007;
    double kFg = 0.03 * 9.8;
    double dt = 0.02;

    double error, prevError = targetDeg;
    double P, I, D, Fg = 0;

    public void robotInit(){
        neo = new CANSparkMax(0, MotorType.kBrushless);
    }

    public void teleopPeriodic() {
        targetDeg = Main.getTargetDeg();
        currentDeg = convertToDegrees(neo.getPosition());

        error = targetDeg - currentDeg;

        P = kP * error;

        

        D = kD * -(currentDeg - prevDeg) / dt;

        if(Math.abs(error) < 5){
            I += kI * error * dt;
        }
        if(Math.signum(error) != Math.signum(prevError)){
            I = 0;
        }

        Fg = kFg * Math.cos(Math.toRadians(currentDeg));

        double power = P  + I + D + Fg;

        neo.set(power);

        System.out.println("error:" + error);

        prevDeg = currentDeg;
        prevError = error;
    }

    public double convertToDegrees(double ticks) {
        double ticksPerRev = 42.0;
        double degrees = ticks / ticksPerRev * 360.0;
        return degrees;
    }

    
}
