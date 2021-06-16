package org.chis.util;

import org.chis.Main;

public class CANSparkMax {
    public CANSparkMax(int canID, MotorType motortype){

    }
    public void set(double power){
        Main.arm.motor.setPower(power);
    }
    public double getPosition(){
        return Main.arm.motor.getEncoderPosition();
    }

    public enum MotorType{
        kBrushless, kBrushed
    }
}
