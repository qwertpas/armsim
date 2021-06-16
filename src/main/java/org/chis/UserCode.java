package org.chis;

import org.chis.util.CANSparkMax;
import org.chis.util.CANSparkMax.MotorType;

public class UserCode {
    
    CANSparkMax neo;

    public void robotInit(){
        neo = new CANSparkMax(0, MotorType.kBrushless);
    }

    public void teleopPeriodic(){

        if(neo.getPosition() < 0){
            neo.set(1);
        }else{
            neo.set(-1);
        }
        
    }

    
}
