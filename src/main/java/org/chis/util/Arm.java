package org.chis.util;

import org.chis.util.Motor.MotorType;

public class Arm {


    public Motor motor;
    public VerletIntegrator integrator = new VerletIntegrator(0, 0, 0, 0);

    public double angle = 0;
    
    final double I = 0.01;
    final double mg = 1; 

    public void init(){
        motor = new Motor(MotorType.NEO, 1);
    }

    public void update(double dt){
        motor.update(integrator.vel, dt);

        double grav = mg * Math.cos(angle);
        double torque = Util.applyFrictions(motor.torque - grav, integrator.vel, 0.05, 0.04, 0.02, 0.001);
        // double torque = Util.applyFrictions(motor.torque - grav, integrator.vel, 0.0, 0.0, 0.0, 0.0);
        
        integrator.update(torque / I, dt);
        angle = integrator.pos;

    }
}
