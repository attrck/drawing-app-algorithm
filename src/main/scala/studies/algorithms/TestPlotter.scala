package studies.algorithms

import java.awt.{Color, Desktop}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import com.xeiam.xchart.{SwingWrapper, Chart}
import com.xeiam.xchart.StyleManager.{LegendPosition, ChartType}

object TestPlotter extends App {
  val pathPrefix = "/Users/atte/Pictures/"
  val drawnImagePath = pathPrefix + "algorithm_source.png"
  val modelImagePath = pathPrefix + "algorithm_model.png"

  val drawnCloud = getCenteredCloud(drawnImagePath)
  val modelCloud = getCenteredCloud(modelImagePath)

  val alignedDrawnCloud = drawnCloud.alignByStandardDeviation(modelCloud)

  drawScatterChart(alignedDrawnCloud.points)

  def getCenteredCloud(imagePath: String) = {
    val black = Color.BLACK.getRGB

    val image = ImageIO.read(new File(imagePath))
    val imagePixels = getImagePixels(image).toArray
    val cloud = PointCloud.fromImagePixelArray(imagePixels, image.getWidth, black)
    cloud.centerByMean
  }

  def drawScatterChart(points: List[Vector2d]) = {
    val chart = new Chart(800, 600)
    chart.getStyleManager().setChartType(ChartType.Scatter)
    chart.getStyleManager().setChartTitleVisible(false)
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW)
    // TODO add after XChart update: chart.getStyleManager().setMarkerSize(3)

    chart.addSeries("Aligned cloud", points.map(_.x).toArray, points.map(-_.y).toArray)
    new SwingWrapper(chart).displayChart()
  }

  def getImagePixels(img: BufferedImage) = {
    for {
      y <- 0 until img.getHeight()
      x <- 0 until img.getWidth()
    } yield img.getRGB(x, y)
  }

  def previewFile(file: String) = Desktop.getDesktop().open(new File(file))
}