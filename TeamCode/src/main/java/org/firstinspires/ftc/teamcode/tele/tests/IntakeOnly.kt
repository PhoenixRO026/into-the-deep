package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.robot.Intake

class IntakeOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val intake = Intake(hardwareMap, config.intake, values.intake, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        intake.intakeTiltCurrentPos = 0.5

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val rightStickY = -gamepad1.right_stick_y.toDouble()

            if (gamepad1.b) {
                intake.resetExtendoPosition()   
            }

            intake.extendoPower = leftStickY
            intake.sweeperPower = rightStickY

            intake.boxTiltCurrentPos += timeKeep.deltaTime / values.intake.boxTiltMaxTravelDuration * when {
                gamepad1.dpad_up -> 1.0
                gamepad1.dpad_down -> -1.0
                else -> 0.0
            }

            intake.intakeTiltCurrentPos += timeKeep.deltaTime / values.intake.intakeTiltMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)
            telemetry.addData("extendo pos", intake.extendoPosition)
            telemetry.addData("extendo power", intake.extendoPower)
            telemetry.addData("sweeper power", intake.sweeperPower)
            telemetry.addData("box tilt pos", intake.boxTiltCurrentPos)
            telemetry.addData("abs box tilt pos", intake.boxTiltCurrentPos.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("intake tilt pos", intake.intakeTiltCurrentPos)
            telemetry.addData("abs intake tilt pos", intake.intakeTiltCurrentPos.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addLine("Press B to reset extendo position")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.addData("right stick Y", rightStickY)
            telemetry.update()
        }
    }
}
/*
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

    /*
     * Change the pattern every 10 seconds in AUTO mode.
     */
    private final static int LED_PERIOD = 10;

    /*
     * Rate limit gamepad button presses to every 500ms.
     */
    private final static int GAMEPAD_LOCKOUT = 500;

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    Telemetry.Item patternName;
    Telemetry.Item display;
    DisplayKind displayKind;
    Deadline ledCycleDeadline;
    Deadline gamepadRateLimit;

    protected enum DisplayKind {
        MANUAL,
        AUTO
    }

    @Override
    public void init()
    {
        displayKind = DisplayKind.AUTO;

        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
        blinkinLedDriver.setPattern(pattern);

        display = telemetry.addData("Display Kind: ", displayKind.toString());
        patternName = telemetry.addData("Pattern: ", pattern.toString());

        ledCycleDeadline = new Deadline(LED_PERIOD, TimeUnit.SECONDS);
        gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void loop()
    {
        handleGamepad();

        if (displayKind == DisplayKind.AUTO) {
            doAutoDisplay();
        } else {
            /*
             * MANUAL mode: Nothing to do, setting the pattern as a result of a gamepad event.
             */
        }
    }

    /*
     * handleGamepad
     *
     * Responds to a gamepad button press.  Demonstrates rate limiting for
     * button presses.  If loop() is called every 10ms and and you don't rate
     * limit, then any given button press may register as multiple button presses,
     * which in this application is problematic.
     *
     * A: Manual mode, Right bumper displays the next pattern, left bumper displays the previous pattern.
     * B: Auto mode, pattern cycles, changing every LED_PERIOD seconds.
     */
    protected void handleGamepad()
    {
        if (!gamepadRateLimit.hasExpired()) {
            return;
        }

        if (gamepad1.a) {
            setDisplayKind(DisplayKind.MANUAL);
            gamepadRateLimit.reset();
        } else if (gamepad1.b) {
            setDisplayKind(DisplayKind.AUTO);
            gamepadRateLimit.reset();
        } else if ((displayKind == DisplayKind.MANUAL) && (gamepad1.left_bumper)) {
            pattern = pattern.previous();
            displayPattern();
            gamepadRateLimit.reset();
        } else if ((displayKind == DisplayKind.MANUAL) && (gamepad1.right_bumper)) {
            pattern = pattern.next();
            displayPattern();
            gamepadRateLimit.reset();
        }
    }

    protected void setDisplayKind(DisplayKind displayKind)
    {
        this.displayKind = displayKind;
        display.setValue(displayKind.toString());
    }

    protected void doAutoDisplay()
    {
        if (ledCycleDeadline.hasExpired()) {
            pattern = pattern.next();
            displayPattern();
            ledCycleDeadline.reset();
        }
    }

    protected void displayPattern()
    {
        blinkinLedDriver.setPattern(pattern);
        patternName.setValue(pattern.toString());
    }
}

*/