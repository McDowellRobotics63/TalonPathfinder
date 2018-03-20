package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;

/**
 * Move the elevator to a set position
 */
public class ElevatorPositionCommand extends Command {

    private double position;
    private ElevatorPosition elevatorPosition;

    public ElevatorPositionCommand(ElevatorPosition position) {
        this(position.getPosition());

        this.elevatorPosition = position;
    }

    public ElevatorPositionCommand(double position) {
        this.requires(Robot.elevatorSubsystem);

        this.position = (position / RobotMap.Elevator.PULLEY_DIAMETER / Math.PI) * RobotMap.Robot.ENCODER_TICKS_PER_ROTATION;
        this.setTimeout(6);
        this.setInterruptible(false);
    }

    @Override
    protected void execute() {
        Robot.elevatorSubsystem.set(ControlMode.MotionMagic, this.position);
    }

    @Override
    protected boolean isFinished() {
        if (this.elevatorPosition == ElevatorPosition.TOP && Robot.elevatorSubsystem.getForwardLimitSwitch()) {
            return true;
        }

        return Math.abs(this.position - Robot.elevatorSubsystem.getEncoderPosition()) < RobotMap.Elevator.MM_ALLOWABLE_ERROR;
    }

    @Override
    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    public enum ElevatorPosition {

        BOTTOM(0),

        SWITCH(20),

        SCALE(50),

        TOP(RobotMap.Elevator.PRIMARY_STAGE_TRAVEL + RobotMap.Elevator.SECONDARY_STAGE_TRAVEL + 1);

        private double position;

        private ElevatorPosition(double position) {
            this.position = position;
        }

        public double getPosition() {
            return this.position;
        }

    }

}
