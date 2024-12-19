@file:Suppress("unused")

package org.firstinspires.ftc.teamcode.lib.units

import com.acmerobotics.roadrunner.AccelConstraint
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.InstantFunction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.TurnConstraints
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.VelConstraint

class TrajectoryActionBuilderEx(private var builder: TrajectoryActionBuilder) {

    /**
     * Ends the current trajectory in progress. No-op if no trajectory segments are pending.
     */
    fun endTrajectory(): TrajectoryActionBuilderEx {
        builder = builder.endTrajectory()
        return this
    }

    /**
     * Stops the current trajectory (like [endTrajectory]) and adds action [a] next.
     */
    fun stopAndAdd(a: Action): TrajectoryActionBuilderEx {
        builder = builder.stopAndAdd(a)
        return this
    }
    fun stopAndAdd(f: InstantFunction) = stopAndAdd(InstantAction(f))

    /**
     * Waits [t] seconds.
     */
    fun waitSeconds(t: Double): TrajectoryActionBuilderEx {
        builder = builder.waitSeconds(t)
        return this
    }

    /**
     * Waits [t] time.
     */
    fun waitSeconds(t: Time) = waitSeconds(t.s)

    /**
     * Schedules action [a] to execute in parallel starting at a displacement [ds] after the last trajectory segment.
     * The action start is clamped to the span of the current trajectory.
     *
     * Cannot be called without an applicable pending trajectory.
     */
    // TODO: Should calling this without an applicable trajectory implicitly begin an empty trajectory and execute the
    // action immediately?
    fun afterDisp(ds: Double, a: Action): TrajectoryActionBuilderEx {
        builder = builder.afterDisp(ds, a)
        return this
    }
    fun afterDisp(ds: Double, f: InstantFunction) = afterDisp(ds, InstantAction(f))

    /**
     * Schedules action [a] to execute in parallel starting at a displacement [ds] after the last trajectory segment.
     * The action start is clamped to the span of the current trajectory.
     *
     * Cannot be called without an applicable pending trajectory.
     */
    fun afterDisp(ds: Distance, a: Action) = afterDisp(ds.inch, a)
    fun afterDisp(ds: Distance, f: InstantFunction) = afterDisp(ds, InstantAction(f))

    /**
     * Schedules action [a] to execute in parallel starting [dt] seconds after the last trajectory segment, turn, or
     * other action.
     */
    fun afterTime(dt: Double, a: Action): TrajectoryActionBuilderEx {
        builder = builder.afterTime(dt, a)
        return this
    }
    fun afterTime(dt: Double, f: InstantFunction) = afterTime(dt, InstantAction(f))

    /**
     * Schedules action [a] to execute in parallel starting [dt] time after the last trajectory segment, turn, or
     * other action.
     */
    fun afterTime(dt: Time, a: Action) = afterTime(dt.s, a)
    fun afterTime(dt: Time, f: InstantFunction) = afterTime(dt, InstantAction(f))

    fun setTangent(r: Rotation2d): TrajectoryActionBuilderEx {
        builder = builder.setTangent(r)
        return this
    }
    fun setTangent(r: Double) = setTangent(Rotation2d.exp(r))

    fun setTangent(r: Angle) = setTangent(r.rad)

    fun setReversed(reversed: Boolean): TrajectoryActionBuilderEx {
        builder = builder.setReversed(reversed)
        return this
    }

    @JvmOverloads
    fun turn(angle: Double, turnConstraintsOverride: TurnConstraints? = null): TrajectoryActionBuilderEx {
        builder = builder.turn(angle, turnConstraintsOverride)
        return this
    }

    @JvmOverloads
    fun turn(angle: Angle, turnConstraintsOverride: TurnConstraints? = null) = turn(angle.rad, turnConstraintsOverride)

    @JvmOverloads
    fun turnTo(heading: Rotation2d, turnConstraintsOverride: TurnConstraints? = null): TrajectoryActionBuilderEx {
        builder = builder.turnTo(heading, turnConstraintsOverride)
        return this
    }
    @JvmOverloads
    fun turnTo(heading: Double, turnConstraintsOverride: TurnConstraints? = null) =
        turnTo(Rotation2d.exp(heading), turnConstraintsOverride)

    @JvmOverloads
    fun turnTo(heading: Angle, turnConstraintsOverride: TurnConstraints? = null) = turnTo(heading.rad, turnConstraintsOverride)

    @JvmOverloads
    fun lineToX(
        posX: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToX(posX, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToX(
        posX: Distance,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToX(posX.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToXConstantHeading(
        posX: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToXConstantHeading(posX, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToXConstantHeading(
        posX: Distance,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToXConstantHeading(posX.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToXLinearHeading(
        posX: Double,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToXLinearHeading(posX, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun lineToXLinearHeading(
        posX: Double,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToXLinearHeading(posX, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToXLinearHeading(
        posX: Distance,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToXLinearHeading(posX.inch, heading.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToXSplineHeading(
        posX: Double,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToXSplineHeading(posX, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun lineToXSplineHeading(
        posX: Double,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToXSplineHeading(posX, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToXSplineHeading(
        posX: Distance,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToXSplineHeading(posX.inch, heading.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToY(
        posY: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToY(posY, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToY(
        posY: Distance,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToY(posY.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToYConstantHeading(
        posY: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToYConstantHeading(posY, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToYConstantHeading(
        posY: Distance,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToYConstantHeading(posY.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToYLinearHeading(
        posY: Double,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToYLinearHeading(posY, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun lineToYLinearHeading(
        posY: Double,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToYLinearHeading(posY, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToYLinearHeading(
        posY: Distance,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToYLinearHeading(posY.inch, heading.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun lineToYSplineHeading(
        posY: Double,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToYSplineHeading(posY, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun lineToYSplineHeading(
        posY: Double,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.lineToYSplineHeading(posY, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun lineToYSplineHeading(
        posY: Distance,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = lineToYSplineHeading(posY.inch, heading.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun strafeTo(
        pos: Vector2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeTo(pos, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun strafeTo(
        pos: Distance2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = strafeTo(pos.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun strafeToConstantHeading(
        pos: Vector2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeToConstantHeading(pos, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun strafeToConstantHeading(
        pos: Distance2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = strafeToConstantHeading(pos.inch, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun strafeToLinearHeading(
        pos: Vector2d,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeToLinearHeading(pos, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun strafeToLinearHeading(
        pos: Vector2d,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeToLinearHeading(pos, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun strafeToLinearHeading(
        pos: Distance2d,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = strafeToLinearHeading(pos.inch, heading.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun strafeToSplineHeading(
        pos: Vector2d,
        heading: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeToSplineHeading(pos, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun strafeToSplineHeading(
        pos: Vector2d,
        heading: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.strafeToSplineHeading(pos, heading, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun strafeToSplineHeading(
        pos: Distance2d,
        heading: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = strafeToSplineHeading(pos.inch, heading.rad, velConstraintOverride, accelConstraintOverride)


    @JvmOverloads
    fun splineTo(
        pos: Vector2d,
        tangent: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineTo(pos, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun splineTo(
        pos: Vector2d,
        tangent: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineTo(pos, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun splineTo(
        pos: Distance2d,
        tangent: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = splineTo(pos.inch, tangent.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun splineToConstantHeading(
        pos: Vector2d,
        tangent: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToConstantHeading(pos, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun splineToConstantHeading(
        pos: Vector2d,
        tangent: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToConstantHeading(pos, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun splineToConstantHeading(
        pos: Distance2d,
        tangent: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = splineToConstantHeading(pos.inch, tangent.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun splineToLinearHeading(
        pose: Pose2d,
        tangent: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToLinearHeading(pose, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun splineToLinearHeading(
        pose: Pose2d,
        tangent: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToLinearHeading(pose, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun splineToLinearHeading(
        pose: Pose,
        tangent: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = splineToLinearHeading(pose.pose2d, tangent.rad, velConstraintOverride, accelConstraintOverride)

    @JvmOverloads
    fun splineToSplineHeading(
        pose: Pose2d,
        tangent: Rotation2d,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToSplineHeading(pose, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }
    @JvmOverloads
    fun splineToSplineHeading(
        pose: Pose2d,
        tangent: Double,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ): TrajectoryActionBuilderEx {
        builder = builder.splineToSplineHeading(pose, tangent, velConstraintOverride, accelConstraintOverride)
        return this
    }

    @JvmOverloads
    fun splineToSplineHeading(
        pose: Pose,
        tangent: Angle,
        velConstraintOverride: VelConstraint? = null,
        accelConstraintOverride: AccelConstraint? = null
    ) = splineToSplineHeading(pose.pose2d, tangent.rad, velConstraintOverride, accelConstraintOverride)

    /**
     * Creates a new builder with the same settings at the current pose, tangent.
     */
    fun fresh(): TrajectoryActionBuilderEx {
        builder = builder.fresh()
        return this
    }

    fun build() = builder.build()
}

fun TrajectoryActionBuilder.ex() = TrajectoryActionBuilderEx(this)