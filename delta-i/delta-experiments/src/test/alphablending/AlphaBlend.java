package test.alphablending;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class AlphaBlend
{
  private static final String FG_IMAGE_PATH_CONST = "data\\foreground_";
  private static final String BG_IMAGE_PATH_CONST = "data\\background_";
  private static final String MATTE_IMAGE_PATH_CONST = "data\\matte_";
  private static final String RESULT_IMAGE_PATH_CONST = "data\\result_";
  private static final String REPORT_PATH_CONST = "report_";
  private static final int MAX_NUM_ITERATIONS = 1;
//  private static final int MAX_NUM_ITERATIONS = 1;
  private static final int MAX_NUM_RESOLUTIONS = 8;

  public AlphaBlend()
  {
  }
  
  public static void main(String[] args)
  {
    BufferedImage fg = null, bg = null, matte = null, result = null;
    int[] fgRawData, bgRawData, matteRawData, resultRawData;
    String resolutionStrs[];
    String fgPath, bgPath, mattePath, resultPath, reportPath;

    resolutionStrs = new String[MAX_NUM_RESOLUTIONS];
    resolutionStrs[0] = "640_480";
    resolutionStrs[1] = "768_576";
    resolutionStrs[2] = "800_600";
    resolutionStrs[3] = "1024_600";
    resolutionStrs[4] = "1280_720";
    resolutionStrs[5] = "1366_768";
    resolutionStrs[6] = "1680_1050";
    resolutionStrs[7] = "1920_1080";
    
//    int k=0;
    for (int k = 0; k < MAX_NUM_RESOLUTIONS; k++)
    {
      fgPath = FG_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
      bgPath = BG_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
      mattePath = MATTE_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
      resultPath = RESULT_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
      reportPath = REPORT_PATH_CONST + resolutionStrs[k] + ".csv";

      System.out.print("Performing alpha blending on image resolution: " + resolutionStrs[k]);

      try
      {
        fg = ImageIO.read(new File(fgPath));
        bg = ImageIO.read(new File(bgPath));
        matte = ImageIO.read(new File(mattePath));
      }
      catch(IOException e)
      {
    	  System.out.println(new File(fgPath).getAbsolutePath());
        System.out.println("Unable to read the images");
        return;
      }

      fgRawData = fg.getRGB(0, 0, fg.getWidth(), fg.getHeight(), null, 0, fg.getWidth());
      bgRawData = bg.getRGB(0, 0, bg.getWidth(), bg.getHeight(), null, 0, bg.getWidth());
      matteRawData = matte.getRGB(0, 0, matte.getWidth(), matte.getHeight(), null, 0, matte.getWidth());

      int numPixels = fg.getWidth() * fg.getHeight();
      int fgPixel_r, fgPixel_g, fgPixel_b;
      int bgPixel_r, bgPixel_g, bgPixel_b;
      int resultPixel_r, resultPixel_g, resultPixel_b;
      int mattePixel, finalPixel;
      long startTime, endTime;
      long timeDiff, total = 0;
      resultRawData = new int[numPixels];
      FileWriter reportFile;
      String timeStr;

      try
      {
        reportFile = new FileWriter(new File(reportPath));
      }
      catch (IOException e)
      {
        System.out.println("Unable to open the report file: " + reportPath);
        return;
      }
      
      
//      System.out.println(Integer.toHexString(bgRawData[0]));
//      System.exit(0);
      
      
      for (int j = 0; j < MAX_NUM_ITERATIONS; j++)
      {
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numPixels; i++)
        {
        	fgPixel_r = (fgRawData[i] & 0x00FF0000) >> 16;
  			fgPixel_g = (fgRawData[i] & 0x0000FF00) >> 8;
  			fgPixel_b =  fgRawData[i] & 0x000000FF;
  			bgPixel_r = (bgRawData[i] & 0x00FF0000) >> 16;
  			bgPixel_g = (bgRawData[i] & 0x0000FF00) >> 8;
  			bgPixel_b =  bgRawData[i] & 0x000000FF;
  			mattePixel = matteRawData[i] & 0x000000FF;

  			resultPixel_r = (((fgPixel_r - bgPixel_r) * mattePixel) >> 8) + bgPixel_r;
  			resultPixel_g = (((fgPixel_g - bgPixel_g) * mattePixel) >> 8) + bgPixel_g;
  			resultPixel_b = (((fgPixel_b - bgPixel_b) * mattePixel) >> 8) + bgPixel_b;
  			finalPixel = 0xFF000000;
  			finalPixel |= (resultPixel_r << 16) | (resultPixel_g << 8) | resultPixel_b;
  			
  			if(i==17904)
        	{
//        		System.out.println();
//        		System.out.println("JAVA");
//        		System.out.println("matte \t"+Integer.toHexString(mattePixel));
//        		System.out.println("bgRaw \t"+Integer.toHexString(bgRawData[i]));
//        		System.out.println("fgRaw \t"+Integer.toHexString(fgRawData[i]));
//  				System.out.println("bg \t"+Integer.toHexString(bgPixel_r)+" "+Integer.toHexString(bgPixel_g)+" "+Integer.toHexString(bgPixel_b));
//  				System.out.println("fg \t"+Integer.toHexString(fgPixel_r)+" "+Integer.toHexString(fgPixel_g)+" "+Integer.toHexString(fgPixel_b));
//  				System.out.println("res \t"+Integer.toHexString(resultPixel_r)+" "+Integer.toHexString(resultPixel_g)+" "+Integer.toHexString(resultPixel_b));
//  				System.out.println("resultRawData["+i+"]\t"+Integer.toHexString(finalPixel).substring(6,8));
//  				System.out.println("aftershifty=["+i+"]\t"+Integer.toHexString((((fgPixel_b - bgPixel_b) * mattePixel) >> 8)));
        	}
//  			else
//  				System.exit(0);
  			
  			resultRawData[i] = finalPixel;
  		}
        
  		endTime = System.currentTimeMillis();
  		System.out.println(endTime - startTime);
        timeDiff = endTime - startTime;
        total += timeDiff;
        timeStr = String.valueOf(timeDiff) + ",\n";

        try
        {
          reportFile.write(timeStr);
        }
        catch (IOException e)
        {
          System.out.println("Unable to write time value to report");
          return;
        }
      }

      result = new BufferedImage(fg.getWidth(), fg.getHeight(), BufferedImage.TYPE_INT_RGB);
      result.setRGB(0, 0, result.getWidth(), result.getHeight(), resultRawData, 0, result.getWidth());

      double average = total / MAX_NUM_ITERATIONS;
      System.out.println(", average time: " + String.valueOf(average));

      try
      {
        reportFile.write(String.valueOf(average) + ",\n");
        reportFile.close();
      }
      catch (IOException e)
      {
        System.out.println("Unable to write the results to report file");
        return;
      }

      try
      {
        ImageIO.write(result, "jpeg", new File(resultPath));
      }
      catch (IOException e)
      {
        System.out.println("Unable to write images");
        return;
      }
    }
  }
}
