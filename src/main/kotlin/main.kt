import de.javagl.obj.Obj
import de.javagl.obj.ObjReader
import de.javagl.obj.ObjWriter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun main(args: Array<String>) {
    println("Start calc!")
    val resolution = args.firstOrNull()  ?: throw IllegalArgumentException("Resolution of sphere missing!")
    createSphere(resolution.toInt())
}

fun calcSpherePoints(resolution: Int, obj: Obj) {
    println("Adding vertices...")
    val radius = 1
    for (i in 0..resolution) {
        val lon = IntRange(0, resolution).map(i.toDouble(), -PI, PI)
        for (j in 0..resolution) {
            val lat = IntRange(0, resolution).map(j.toDouble(), -PI / 2, PI / 2)
            val x = radius * sin(lon) * cos(lat)
            val y = radius * sin(lon) * sin(lat)
            val z = radius * cos(lon)
            print("x,y,z: $x,$y,$z\n")
            obj.addVertex(x.toFloat(), y.toFloat(), z.toFloat())
        }
    }
}

fun createFile(): Obj {
    // Read an empty OBJ from file
    val objInputStream: InputStream = FileInputStream("./data/empty.obj")
    return ObjReader.read(objInputStream)
}

fun createSphere(resolution: Int) {
    val obj = createFile()
    calcSpherePoints(resolution, obj)
    addPolygonalFaceElements(resolution, obj)

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

    println("Writing file ./data/polygon-$resolution-$timestamp.obj ...")


    // Write a new OBJ file
    val objOutputStream: OutputStream = FileOutputStream("./data/polygon-$resolution-$timestamp.obj")
    ObjWriter.write(obj, objOutputStream)
    println("Done!")
}

fun addPolygonalFaceElements(resolution: Int, obj: Obj) {
    println("Adding polygons...")

    for (i in 0 until resolution) {
        for (j in 0 until resolution) {
            val upperCorner = i * resolution + j
            val lowerCorner = (i + 1) * resolution + j
            obj.addFace(upperCorner, upperCorner + 1, lowerCorner)
            obj.addFace(lowerCorner, lowerCorner + 1, upperCorner + 1)
        }
    }
}
