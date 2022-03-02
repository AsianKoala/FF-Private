package neil.ftc21.v2.auto.roadrunner.trajectorysequence.sequencesegment

import com.acmerobotics.roadrunner.trajectory.Trajectory

class TrajectorySegment // Note: Markers are already stored in the `Trajectory` itself.
// This class should not hold any markers
(val trajectory: Trajectory) : SequenceSegment(trajectory.duration(), trajectory.start(), trajectory.end(), emptyList())
