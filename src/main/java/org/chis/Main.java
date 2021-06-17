package org.chis;

import java.awt.Color;
import java.awt.MouseInfo;

import org.chis.util.Arm;
import org.chis.util.GraphicDash;
import org.chis.util.GraphicSim;
import org.chis.util.Util.LooptimeMonitor;

public class Main {
    public static Boolean paused = false;

    public static double startTime;
    public static double elaspedTime;
    
    public static Arm arm;

    public static double target;

    public static void main(String[] args) throws InterruptedException {

        arm = new Arm();
        arm.init();


        new DisplayThread();

        Thread.sleep(2000);

        new InputThread();
        new UserCodeThread();


        startTime = System.nanoTime();

        LooptimeMonitor clock = new LooptimeMonitor();
        while (true) {
            clock.start();
            if(!paused){
                elaspedTime = (System.nanoTime() - startTime) * 1e-9;
                arm.update(Constants.PHYSICS_DT);
            }
            clock.end();
            try {
                double sleeptime = Math.max(0, Constants.PHYSICS_DT - clock.codetime); //in seconds
                Thread.sleep((int) (sleeptime * 1000)); //in milliseconds
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

            LooptimeMonitor clock = new LooptimeMonitor();

            while(!exit) {
                if(!paused){

                    clock.start();
                    angleGraph.putNumber("angle", arm.angle, Color.RED);
                    angleGraph.putNumber("target", Math.toRadians(getTargetDeg()), Color.BLUE);


                    GraphicSim.sim.repaint();
                    GraphicDash.paintAll();
                    clock.end();


                }
                try{
                    double sleeptime = Math.max(0, Constants.DISPLAY_DT - clock.codetime); //in seconds
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

    public static class InputThread implements Runnable{
        private boolean exit;
        Thread t;
        InputThread() {
            t = new Thread(this, "Input");
            System.out.println("New Thread: " + t);
            exit = false;
            t.start();
        }

        public void run(){
            
            LooptimeMonitor clock = new LooptimeMonitor();

            while (!exit) {

                clock.start();

                target = -MouseInfo.getPointerInfo().getLocation().y;

                clock.end();

                try {
                    double sleeptime = Math.max(0, Constants.INPUT_DT - clock.codetime); //in seconds
                    Thread.sleep((int) (sleeptime * 1000)); //in milliseconds
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            exit = true;
        }
    }

    public static double getTargetDeg(){
        return target;
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

            LooptimeMonitor clock = new LooptimeMonitor();

            while (!exit) {

                clock.start();
                userCode.teleopPeriodic();
                clock.end();

                try {
                    double sleeptime = Math.max(0, Constants.USERCODE_DT - clock.codetime); //in seconds
                    Thread.sleep((int) (sleeptime * 1000)); //in milliseconds
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            exit = true;
        }
    }


}
