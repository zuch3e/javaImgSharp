//Neleptcu Daniel-Andrei 332AB
//Imi selectez pachetele necesare functionarii clasei ImageReader(Producer)
package packWork;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

//Definesc clasa ImageReader care mosteneste clasa startProcess
public class ImageReader extends StartProcess {
	//Clasa contine path-ul pentru imaginea sursa si buffer-ul comun
	//pentru Producer si Consumer.
	
	String inputImagePath;
	private Buffer buffer;
	
	
	//Constructorul pentru clasa ImageReader
	//Deoarece in contextul acestui proiect clasele nu au fost instantiate
	//fara parametru, nu adaugat constructor fara parametru.
	public ImageReader(Buffer newBuffer, String pathToImage)
	{
		buffer = newBuffer;
		inputImagePath = pathToImage;
	}
	
	//Suprascriu metoda abstracta din clasa startProcess
	@Override
	public void processImage()
	{
		System.out.println("Am intrat in ImageReader");
        BufferedImage image = null;
        // Folosesc un bloc try-catch pentru tratarea lucrului cu functiile
        // sincronizate, respectiv citirea imaginii.
        try {
        	
        	//Citesc imaginea si stochez inaltimea si latimea acesteia
        	//in bufferul comun.
        	image = ImageIO.read(new File(inputImagePath));
        	int width = image.getWidth();
        	int height = image.getHeight();
        	int rgb;
        	
        	buffer.put("height",height);
        	buffer.put("width",width);

        	System.out.println("Incep sa trimit pixelii");
        	// Citesc pixelii RGB din imagine si ii introduc in vectorul vec
        	// pe care il transmit in buffer. Nu am mai afisat mesaje separate
        	// in consola deoarece transmitand foarte multe linii ar fi deranjant
        	// din punct de vedere vizual.
        	for (int y = 0; y < height; y++) 
        	{
        		int [] vec = new int[width]; 
        		
        		for (int x = 0; x < width; x++) 
        		{
        			rgb = image.getRGB(x, y);		
        			vec[x] = rgb;
        		}        		       		
        		buffer.put(vec);       		
        	}
        }
        catch (IIOException e) {
        	e.printStackTrace();
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	//Metoda run suprascrisa a clasei thread in care calculez timpul
	//in care metoda processImage() a fost executata.
	@Override
    public void run() 
	{
			tStart();
        	
			processImage();
			
            tStop();
            float timp = getTime();
            System.out.println("[Image reader a fost pornit acum " + timp + " secunde si s-a incheiat]");
	}
}
