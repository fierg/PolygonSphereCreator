fun IntRange.map(number: Double, targetStart: Double, targetEnd: Double): Double {
    val ratio = number/ (endInclusive - start)
    return ratio * (targetEnd - targetStart) + targetStart
}