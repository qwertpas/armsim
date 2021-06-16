package org.chis;

import org.chis.util.CANSparkMax;
import org.chis.util.CANSparkMax.MotorType;

public class UserCode {
    
    CANSparkMax neo;
    double prevDegrees;

    public void robotInit(){
        neo = new CANSparkMax(0, MotorType.kBrushless);
    }

    public void teleopPeriodic() {

        double degrees = convertToDegrees(neo.getPosition());
        System.out.println("Deg:" + degrees);

        int targetDeg = 120;

        // Mass is in kg
        double armMass = 1;

        // distance from 120 determines voltage
        double distance = Math.abs(degrees-targetDeg);
        double scale = 0.012;

        double derivativeConst = 0.003;
        double kv = derivativeConst*(-(degrees-prevDegrees)/0.02);

        double gravConst = 0.03;
        double gv = armMass*9.8*gravConst*Math.cos(Math.toRadians(degrees));

        if (degrees > 120)
            neo.set(-distance*scale + kv + gv);
        else
            neo.set(distance*scale + kv + gv);


        prevDegrees = degrees;
        
    }

    public double convertToDegrees(double ticks) {
        double ticksPerRev = 360/42.0;
        int degrees = (int)(ticks*ticksPerRev) % 360;
        return degrees;
    }

    
}
