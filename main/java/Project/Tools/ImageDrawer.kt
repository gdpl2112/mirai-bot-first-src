package Project.Tools

import kotlin.Throws
import com.madgag.gif.fmsware.AnimatedGifEncoder
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import Project.Tools.ImageDrawer
import java.awt.geom.RoundRectangle2D
import java.io.IOException
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import java.awt.*
import java.io.File
import java.lang.Exception
import java.net.URL

object ImageDrawer {
    /**
     * 推 的动画
     *
     * @param files   gif集
     * @param oFile   要被推的 url
     * @param outFile 输出
     * @return
     * @throws Exception
     */
    @JvmStatic
    @Throws(Exception::class)
    fun getTuiGift(files: Array<File?>, oFile: URL?, outFile: File): File {
        val encoder = AnimatedGifEncoder()
        encoder.start(outFile.absolutePath)
        encoder.setRepeat(0)
        encoder.setQuality(5)
        encoder.setFrameRate(200f)
        val rotateEve = 360 / files.size
        for (i in files.indices) {
            encoder.setDelay(100)
            val main = ImageIO.read(files[i])
            var image = ImageIO.read(oFile)
            val rotate = (rotateEve * i).toFloat()
            image = Image2Size(image, 200, 200) as BufferedImage
            image = rotateImage(image, rotate) as BufferedImage
            image = roundImage(image, 9999)
            image = putImage(main, image, 93, 83)
            encoder.addFrame(image)
        }
        encoder.finish()
        return outFile
    }

    /**
     * 图片圆角
     *
     * @param image        图片
     * @param cornerRadius 幅度
     * @return
     */
    fun roundImage(image: BufferedImage, cornerRadius: Int): BufferedImage {
        val width = image.width
        val height = image.height
        val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2 = outputImage.createGraphics()
        g2.composite = AlphaComposite.Src
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fill(
            RoundRectangle2D.Float(
                0f, 0f,
                width.toFloat(),
                height.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat()
            )
        )
        g2.composite = AlphaComposite.SrcAtop
        g2.drawImage(image, 0, 0, null)
        g2.dispose()
        return outputImage
    }

    /**
     * 压缩指定宽、高
     *
     * @param bimg
     * @param width
     * @param height
     * @param tagFilePath
     * @return
     */
    @Throws(IOException::class)
    fun Image2Size(bimg: BufferedImage?, width: Int, height: Int): Image {
        var tempFile: File? = null
        try {
            tempFile = File.createTempFile("temp1", ".png")
            Thumbnails.of(bimg).size(width, height).outputQuality(1f).toFile(tempFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val image = ImageIO.read(tempFile)
        tempFile!!.delete()
        return image
    }

    /**
     * 旋转图片
     *
     * @param image
     * @param rotate
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun rotateImage(image: BufferedImage, rotate: Float): Image {
        var image = image
        var tempFile: File? = null
        try {
            val _w = image.width
            val _h = image.height
            tempFile = File.createTempFile("temp2", ".png")
            Thumbnails.of(image).scale(1.0).rotate(rotate.toDouble()).toFile(tempFile)
            val i1 = ImageIO.read(tempFile)
            val w = i1.width
            val h = i1.height
            Thumbnails.of(i1)
                .sourceRegion(Positions.CENTER, 200, 200)
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile(tempFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        image = ImageIO.read(tempFile)
        tempFile!!.delete()
        return image
    }

    /**
     * 将 一张图片放到 一张图片上
     *
     * @param image
     * @param im
     * @param x
     * @param y
     * @return
     */
    fun putImage(image: BufferedImage, im: BufferedImage?, x: Int, y: Int): BufferedImage {
        val graphics = image.graphics
        graphics.drawImage(im, x, y, null)
        return image
    }
}