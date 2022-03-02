package neil.ftc21.v2.util

object Constants {
    const val loadingSensorThreshold = 20.0
    
    const val slideHomeValue = 0.0
    const val slideDepositInches = 33.5

    const val slideSharedInches = 0.0

    const val turretPostZeroValue = 180.0
    const val turretHomeValue = 180.0
    private const val turretDepositDiff = 60.0
    const val turretBlueAngle = turretHomeValue + turretDepositDiff
    const val turretRedAngle = turretHomeValue - turretDepositDiff
    const val turretSharedBlueAngle = 90.0
    const val turretSharedRedAngle = 270.0

    const val outtakeHomePosition = 0.25
    const val outtakeCockMore = 0.63
    const val outtakeCockPosition = 0.6
    const val outtakeHighPosition = 0.7
    const val outtakeSharedPosition = 1.0

    const val indexerOpenPosition = 0.62
    const val indexerLockedPosition = 0.72
    const val indexerIndexingPosition = 0.93

    const val armHomePosition = 0.10
    const val armHighPosition = 0.68
    const val armSharedPosition = 1.0

    const val duckSpeed = 0.3
}