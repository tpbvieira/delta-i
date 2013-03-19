package test.alphablending;

import jSIMD.PackedInt;
import jSIMD.Processor;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class AlphaBlendSIMD
{
  private static final String FG_IMAGE_PATH_CONST = "data\\foreground_";
  private static final String BG_IMAGE_PATH_CONST = "data\\background_";
  private static final String MATTE_IMAGE_PATH_CONST = "data\\matte_";
  private static final String RESULT_IMAGE_PATH_CONST = "data\\result_";
  private static final String REPORT_PATH_CONST = "report_";
  private static final int MAX_NUM_ITERATIONS = 1;
  private static int BLOCK_SIZE = 50000;
//  private static final int MAX_NUM_ITERATIONS = 2;
  private static final int MAX_NUM_RESOLUTIONS = 8;

  public AlphaBlendSIMD()
  {
	  
  }
  
  public static void main(String[] args)
  {
	Processor js = Processor.INSTANCE;
	int[] fgRawData, bgRawData, matteRawData, resultRawData;
	BufferedImage fg = null;
	BufferedImage bg = null;
	BufferedImage matte = null;
	BufferedImage result = null;
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
    for (int k = 0; k < 1; k++)
//    for(int jump=0;jump<100;jump++)
    {
//    	BLOCK_SIZE+=100;
//    	System.out.println("BLOCK_SIZE ="+BLOCK_SIZE);
    	try {
  		  System.gc();
			Thread.sleep(10);
			
//			System.runFinalization();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
//      int[] emptyArray;
      
      int numPixels = fg.getWidth() * fg.getHeight();
      
      
//      int fgPixel_r, fgPixel_g, fgPixel_b;
//      int bgPixel_r, bgPixel_g, bgPixel_b;
//      int resultPixel_r, resultPixel_g, resultPixel_b;

      long startTime=0, endTime=0;
      long timeDiff=0, total = 0;
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
//      System.out.println();
      for (int j = 0; j < MAX_NUM_ITERATIONS; j++)
      {
    	  try {
    		  System.gc();
			Thread.sleep(1000);
			
//			System.runFinalization();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	  int startIndex=0;
    	 int extra = fgRawData.length%BLOCK_SIZE == 0 ? 0 : 1;
    	 for (int blocks = 0; blocks < ((fgRawData.length/BLOCK_SIZE)+extra); blocks++)
         {      		 
       int endIndex=startIndex+BLOCK_SIZE;
       if (endIndex>fgRawData.length)
    	   endIndex=fgRawData.length;
        int[] fg_r = new int[BLOCK_SIZE];
//        int[] fg_g = new int[BLOCK_SIZE];
//        int[] fg_b = new int[BLOCK_SIZE];
        int[] bg_r = new int[BLOCK_SIZE];
//        int[] bg_g = new int[BLOCK_SIZE];
//        int[] bg_b = new int[BLOCK_SIZE];
        int[] shortMatte = new int[BLOCK_SIZE];
        int[] empty0 = new int[BLOCK_SIZE];
        int[] empty1 = new int[BLOCK_SIZE];
        int[] empty2 = new int[BLOCK_SIZE];
        int[] empty3 = new int[BLOCK_SIZE];
        int[] empty4 = new int[BLOCK_SIZE];
        int[] empty5 = new int[BLOCK_SIZE];
      //instantiate data
//        System.out.println("start \t"+startIndex+"/"+fgRawData.length);
//        System.out.println("end \t"+endIndex+"/"+fgRawData.length);
//        System.out.println();
//        startTime = System.currentTimeMillis();
        for(int i=startIndex;i<endIndex;i++)
        {
        	int index=i%BLOCK_SIZE;
//        	String s = fgRawData[i] + "";
//        	byte [] barr = s.getBytes();
//        	System.out.println(barr.length);
//        	String s = Integer.toHexString(fgRawData[i]);
//        	fg_r[index] =  Integer.parseInt(s.substring(2,4),16);
//        	fg_g[index] = Integer.parseInt(s.substring(4,6),16);
//        	fg_b[index] = Integer.parseInt(s.substring(6,8),16);
        	
        	fg_r[index] = fgRawData[i]; 
//        	fg_g[index] = fgRawData[i];
//        	fg_b[index] = fgRawData[i];
        	
        	bg_r[index] = bgRawData[i]; 
//        	bg_g[index] = bgRawData[i];
//        	bg_b[index] = bgRawData[i];
 
        	shortMatte[index] = matteRawData[i];
        }	
//        endTime = System.currentTimeMillis();
        //packl arrays
        PackedInt p_matte = new PackedInt(shortMatte); 
        PackedInt fgPixel_r = new PackedInt(fg_r);
        PackedInt fgPixel_g = new PackedInt(empty0);
        PackedInt fgPixel_b = new PackedInt(empty1);
        PackedInt bgPixel_r = new PackedInt(bg_r);  
        PackedInt bgPixel_g = new PackedInt(empty2);
        PackedInt bgPixel_b = new PackedInt(empty3);
        PackedInt p_temp = new PackedInt(empty4);
        
		//actual alphablending
        
        //shift the data and mask blue
        js.SRL(fgPixel_r, 8, fgPixel_g);
        js.And(fgPixel_r, 0x000000FF, fgPixel_b);
       
        js.SRL(fgPixel_r, 16, fgPixel_r);
 
        js.SRL(bgPixel_r, 8, bgPixel_g);
        js.And(bgPixel_r, 0x000000FF, bgPixel_b);
        js.SRL(bgPixel_r, 16, bgPixel_r);
        
        //mask the data for red and green
        
        js.And(fgPixel_r, 0x000000FF, fgPixel_r);
        js.And(fgPixel_g, 0x000000FF, fgPixel_g);

        js.And(bgPixel_r, 0x000000FF, bgPixel_r);
        js.And(bgPixel_g, 0x000000FF, bgPixel_g);
        
        js.And(p_matte, 0x000000FF, p_matte);
       
       
        
        //red
        js.Sub(fgPixel_r,bgPixel_r, p_temp);
        
        js.MultL(p_temp, p_matte, p_temp);
        
        js.SRA(p_temp, 8, p_temp);
        js.Add(p_temp, bgPixel_r, bgPixel_r);
        
        //green
        js.Sub(fgPixel_g,bgPixel_g, p_temp);
        js.MultL(p_temp, p_matte, p_temp);
        js.SRA(p_temp, 8, p_temp);
        js.Add(p_temp, bgPixel_g, bgPixel_g);
        
        //blue
        
        js.Sub(fgPixel_b,bgPixel_b, p_temp);
        js.MultL(p_temp, p_matte, p_temp);
        js.SRA(p_temp, 8, p_temp);
        js.Add(p_temp, bgPixel_b, bgPixel_b);       
        
        //left shift data before OR
        js.SLL(bgPixel_r, 16, bgPixel_r);
        js.SLL(bgPixel_g, 8, bgPixel_g);
        
        //do the OR
        js.Or(bgPixel_r, bgPixel_g, bgPixel_g);
        js.Or(bgPixel_g, bgPixel_b, bgPixel_b);
        js.Or(bgPixel_b, 0xFF000000, bgPixel_r);
        
        js.Execute();
        
        fg_r = bgPixel_r.toArray();
        
        int deltaCells = endIndex-startIndex;
        for(int i=0;i<BLOCK_SIZE;i++)
        {
        	//if(startIndex+i==resultRawData.length)
        	if(i==deltaCells)
        		break;
        	resultRawData[startIndex+i] = fg_r[i];

//        	if(i==((640*480)-1))
//        	{
//        		System.out.println();
//        		System.out.println("SIMD");
//        		System.out.println("matte\t"+Integer.toHexString(p_matte.toArray()[i]));
//        		System.out.println("bgRaw\t"+Integer.toHexString(bgRawData[i]));
//        		System.out.println("fgRaw\t"+Integer.toHexString(fgRawData[i]));
//        		System.out.println("bg \t"+Integer.toHexString(bg_r[i])+" "+Integer.toHexString(bg_g[i])+" "+Integer.toHexString(bg_b[i]));
//  				System.out.println("fg \t"+Integer.toHexString(fgPixel_r.toArray()[i])+" "+Integer.toHexString(fgPixel_g.toArray()[i])+" "+Integer.toHexString(fgPixel_b.toArray()[i]));
//  				System.out.println("res \t"+Integer.toHexString(fg_r[i])+" "+Integer.toHexString(fg_g[i])+" "+Integer.toHexString(fg_b[i]));
//  				System.out.println("resultRawData["+i+"]\t"+Integer.toHexString(finalPixel).substring(6,8));
//  				System.out.println("aftershifty=["+i+"]\t"+Integer.toHexString(p_temp.toArray()[i]));
////	        	System.exit(0);
//        	}
//        	else
//        		System.exit(0);
        }
        
        //unpack and build image
        
        js.clear();
        startIndex+=BLOCK_SIZE;
        
        timeDiff = endTime - startTime;
        total += timeDiff;
         }
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

      result = new BufferedImage(matte.getWidth(), matte.getHeight(), BufferedImage.TYPE_INT_RGB);
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
