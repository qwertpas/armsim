package org.chis;

import java.awt.Color;

import org.chis.util.Arm;
import org.chis.util.GraphicDash;
import org.chis.util.GraphicSim;

public class Main {
    public static Boolean paused = false;

    public static double startTime;
    public static double elaspedTime;
    
    public static Arm arm;

    public static final double PHYSICS_DT = 0.001;
    public static final double DISPLAY_DT = 0.020;
    public static final double USERCODE_DT = 0.020;

    public static void main(String[] args) throws InterruptedException {

        arm = new Arm();
        arm.init();


        new DisplayThread();
        new UserCodeThread();

        Thread.sleep(2000);


        startTime = System.nanoTime();
        while (true) {

            if(!paused){
                elaspedTime = (System.nanoTime() - startTime) * 1e-9;
                arm.update(PHYSICS_DT);
            }

            try {
                Thread.sleep((int) (1000 * PHYSICS_DT));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DisplayThread implements Runnable{
        private boolean exit;
        Thread t;
        DisplayThread() {
            t = new Thread(this, "Display");
            System.out.println("New Thread: " + t);
            exit = false;
            t.start();
        }

        GraphicDash angleGraph = new GraphicDash("angle", 500, true);

        public void run(){
            GraphicSim.init();



            while(!exit) {
                if(!paused){

                    angleGraph.putNumber("angle", arm.angle, Color.RED);
                    angleGraph.putNumber("target", Math.toRadians(120), Color.BLUE);


                    GraphicSim.sim.repaint();
                    GraphicDash.paintAll();



                }
                try{
                    double sleeptime = Math.max(0, DISPLAY_DT); //in seconds
                    Thread.sleep((int) (sleeptime * 1000)); //in milliseconds
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            exit = true;
        }
    }

    public static class UserCodeThread implements Runnable{
        private boolean exit;
        Thread t;
        UserCodeThread() {
            t = new Thread(this, "UserCode");
            System.out.println("New Thread: " + t);
            exit = false;
            t.start();
        }

        public void run(){
            UserCode userCode = new UserCode();

            userCode.robotInit();


            while (!exit) {

                userCode.teleopPeriodic();

                try {
                    Thread.sleep(20); // in milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            exit = true;
        }
    }


}
