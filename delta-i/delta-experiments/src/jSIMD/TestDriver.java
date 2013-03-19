package jSIMD;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO: register contents tracking
		// TODO: scheduling instructions into SIMD core
		// TODO: resolve transactions with casting in the middle of them
		// TODO: integrate JNI code for a few operations
		
		Processor js = Processor.INSTANCE;
		BufferedImage fg = null, bg = null, matte = null, result = null;
	    int[] fgRawData, bgRawData, matteRawData, resultRawData;
	    String resolutionStrs[];
	    String fgPath, bgPath, mattePath, resultPath, reportPath;
	    
		  String FG_IMAGE_PATH_CONST = "data\\foreground_";
		  String BG_IMAGE_PATH_CONST = "data\\background_";
		  String MATTE_IMAGE_PATH_CONST = "data\\matte_";
		  String RESULT_IMAGE_PATH_CONST = "data\\result_";
		  String REPORT_PATH_CONST = "report_";
		  
		//  private static final int MAX_NUM_ITERATIONS = 1;
		  int MAX_NUM_RESOLUTIONS = 8;
		
		  resolutionStrs = new String[8];
	    resolutionStrs[0] = "640_480";
	    resolutionStrs[1] = "768_576";
	    resolutionStrs[2] = "800_600";
	    resolutionStrs[3] = "1024_600";
	    resolutionStrs[4] = "1280_720";
	    resolutionStrs[5] = "1366_768";
	    resolutionStrs[6] = "1680_1050";
	    resolutionStrs[7] = "1920_1080";
	    int k =5;
		fgPath = FG_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
	      bgPath = BG_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
	      mattePath = MATTE_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
	      resultPath = RESULT_IMAGE_PATH_CONST + resolutionStrs[k] + ".jpg";
	      reportPath = REPORT_PATH_CONST + resolutionStrs[k] + ".csv";
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

      int[] fg_r = new int[numPixels];
      int[] bg_r = new int[numPixels];
      int[] shortMatte = new int[numPixels];
      int[] empty0 = new int[numPixels];
      int[] empty1 = new int[numPixels];
      int[] empty2 = new int[numPixels];
      int[] empty3 = new int[numPixels];
      int[] empty4 = new int[numPixels];
      
      for(int i=0;i<numPixels;i++)
      {      	
      	fg_r[i] = fgRawData[i]; 
      	bg_r[i] = bgRawData[i]; 
      	shortMatte[i] = matteRawData[i];
      	empty0[i] = 0;
        empty1[i] = 0;
        empty2[i] = 0;
        empty3[i] = 0;
        empty4[i] = 0;
      }	
		
		PackedInt p_matte = new PackedInt(shortMatte); 
        PackedInt fgPixel_r = new PackedInt(fg_r);
        PackedInt fgPixel_g = new PackedInt(empty1);
        PackedInt fgPixel_b = new PackedInt(empty0);
        PackedInt bgPixel_r = new PackedInt(bg_r);  
        PackedInt bgPixel_g = new PackedInt(empty2);
        PackedInt bgPixel_b = new PackedInt(empty3);
        PackedInt p_temp = new PackedInt(empty4);			
        startTime = System.currentTimeMillis();   
	        js.Add(fgPixel_r,bgPixel_r, p_temp);
	        js.SRL(fgPixel_r, 8, fgPixel_g);
	        
	        
	    	
			//actual alphablending
	        
	        //shift the data and mask blue
	        js.SRL(fgPixel_r, 8, fgPixel_g);
	        js.And(fgPixel_r, 0x000000FF, fgPixel_b);
//	       
	        
	        js.SRL(fgPixel_r, 16, fgPixel_r);
//	 
	        js.SRL(bgPixel_r, 8, bgPixel_g);
	        js.And(bgPixel_r, 0x000000FF, bgPixel_b);
	        js.SRL(bgPixel_r, 16, bgPixel_r);
	        
//	        //mask the data for red and green
//	        
	        js.And(fgPixel_r, 0x000000FF, fgPixel_r);
	        js.And(fgPixel_g, 0x000000FF, fgPixel_g);
//
	        js.And(bgPixel_r, 0x000000FF, bgPixel_r);
	        js.And(bgPixel_g, 0x000000FF, bgPixel_g);
	        
	        js.And(p_matte, 0x000000FF, p_matte);
	       

////	        
////	        //red
	        js.Sub(fgPixel_r,bgPixel_r, p_temp);
//	        
	        js.MultL(p_temp, p_matte, p_temp);
//
	        js.SRA(p_temp, 8, p_temp);
	        js.Add(p_temp, bgPixel_r, bgPixel_r);
//	        
//	        //green
	        js.Sub(fgPixel_g,bgPixel_g, p_temp);
	        js.MultL(p_temp, p_matte, p_temp);
	        js.SRA(p_temp, 8, p_temp);
	        js.Add(p_temp, bgPixel_g, bgPixel_g);
//	        
//	        //blue
//	        
	        js.Sub(fgPixel_b,bgPixel_b, p_temp);
	        js.MultL(p_temp, p_matte, p_temp);
	        js.SRA(p_temp, 8, p_temp);
	        js.Add(p_temp, bgPixel_b, bgPixel_b);
////
//	        
//	        
////	        //left shift data before OR
	        js.SLL(bgPixel_r, 16, bgPixel_r);
	        js.SLL(bgPixel_g, 8, bgPixel_g);
//	        
////	        //do the OR
	        js.Or(bgPixel_r, bgPixel_g, bgPixel_g);
	        js.Or(bgPixel_g, bgPixel_b, bgPixel_b);
	        js.Or(bgPixel_b, 0xFF000000, bgPixel_r);
//	        
	        js.Execute();
	        endTime = System.currentTimeMillis();
	        fg_r = bgPixel_r.toArray();
	        js.clear();
	        System.out.println(endTime - startTime);
	        
	        
	        result = new BufferedImage(matte.getWidth(), matte.getHeight(), BufferedImage.TYPE_INT_RGB);
	        result.setRGB(0, 0, result.getWidth(), result.getHeight(), fg_r, 0, result.getWidth());
	        try
	        {
	          ImageIO.write(result, "jpeg", new File(resultPath));
	        }
	        catch (IOException e)
	        {
	          System.out.println("Unable to write images");
	          return;
	        }
/*
		
		long 	endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println((endTime-startTime));
		System.out.println("C: " + packedC.toArray()[0]+" " + packedC.toArray()[1]+" " + packedC.toArray()[2]+" " + packedC.toArray()[3]+" " 
		+ packedC.toArray()[4]+" " + packedC.toArray()[5]+" " + packedC.toArray()[6]+" " + packedC.toArray()[7]+" " 
		+ packedC.toArray()[8]+" " + packedC.toArray()[9]+" " + packedC.toArray()[10]);
System.out.println("E: " + packedE.toArray()[0]+" " + packedE.toArray()[1]+" " + packedE.toArray()[2]+" " + packedF.toArray()[3]+ " " + packedE.toArray()[4]
       +" " + packedE.toArray()[5]+" " + packedE.toArray()[6]+" " + packedF.toArray()[7]+" " + packedF.toArray()[8]);

System.out.println("F: "+packedF.toArray()[0]+" " + packedF.toArray()[1]+" " + packedF.toArray()[2]+" " + packedF.toArray()[3]+ " " + packedF.toArray()[4]
       +" " + packedF.toArray()[5]+" " + packedF.toArray()[6]+" " + packedF.toArray()[7]+" " + packedF.toArray()[8]);
System.out.println("B: "+packedB.toArray()[0]+" " + packedB.toArray()[1]+" " + packedB.toArray()[2]+" " + packedB.toArray()[3]+ " " + packedB.toArray()[4]
       +" " + packedB.toArray()[5]+" " + packedB.toArray()[6]+" " + packedB.toArray()[7]+" " + packedB.toArray()[8]);
System.out.println("D: "+packedD.toArray()[0]+" " + packedD.toArray()[1]+" " + packedD.toArray()[2]+" " + packedD.toArray()[3]+ " " + packedD.toArray()[4]
       +" " + packedD.toArray()[5]+" " + packedD.toArray()[6]+" " + packedD.toArray()[7]+" " + packedD.toArray()[8]);
System.out.println("A: "+packedA.toArray()[0]+" " + packedA.toArray()[1]+" " + packedA.toArray()[2]+" " + packedA.toArray()[3]+ " " + packedA.toArray()[4]
       +" " + packedA.toArray()[5]+" " + packedA.toArray()[6]+" " + packedA.toArray()[7]+" " + packedA.toArray()[8]);		//System.out.println("Wait:");
js.clear();

	      try
	      {
	        Thread.sleep(400);        
	             
	       }
	       catch (InterruptedException ie)
	       {
	            System.out.println(ie.getMessage());
	       }
*/
		//js.Equal(packedA, packedA, packedA);
	       
//	       int j=0, max = 11;
//	       for(j=0; j<max; j++){
//	    	   System.out.print(packedA.toArray()[j]+"  ");
//	       }
//	       System.out.println();
//	       for(j=0; j<max; j++)
//	    	   System.out.print(packedB.toArray()[j]+"  ");
//	       System.out.println();
//	       for(j=0; j<max; j++)
//	    	   System.out.print(packedC.toArray()[j]+"  ");
//	       System.out.println();
//	       for(j=0; j<max; j++)
//	    	   System.out.print(packedD.toArray()[j]+"  ");
//	       System.out.println();
//	       for(j=0; j<max; j++)
//	    	   System.out.print(packedE.toArray()[j]+"  ");
//	       System.out.println();
//	       for(j=0; j<max; j++)
//	    	   System.out.print(packedF.toArray()[j]+"  ");
//	       System.out.println();
	    
	       
	}
}
