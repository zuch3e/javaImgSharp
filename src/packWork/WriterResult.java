//Neleptcu Daniel-Andrei 332AB
//Imi selectez pachetele necesare functionarii clasei WriterResult
package packWork;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

//Definesc clasa Imagesharpen care mosteneste clasa startProcess
public class WriterResult extends StartProcess
{
	//Clasa contine pipe-ul de legatura cu clasa Consumer si output
	//path-ul catre imaginea rezultata, primita de Consumer.
	private DataInputStream dataInputStream;
	private String outputPath;
	private int flag = 0;
	
	//bloc initializare statica
	//Constructorul pentru clasa WriterResult
	//Deoarece in contextul acestui proiect clasele nu au fost instantiate
	//fara parametru, nu adaugat constructor fara parametru.
	public WriterResult(DataInputStream newInputStream, String pathToImage)
	{
		this.dataInputStream = newInputStream;
		outputPath = pathToImage;
	}
	
	
	//Suprascriu metoda abstracta processImage din clasa startProcess
	@Override
	public void processImage()
	{
		//Declar dimensiunile pe care le citesc din pipe prin intermediul
		//unui bloc try-catch pentru a trata exceptiile.
		int height = 0;
		int width = 0;
		try 
		{
			height = dataInputStream.readInt();
			width = dataInputStream.readInt();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		//Instantiez un obiect de tipul BufferedImage cu valorile dimensiunilor
		//Apoi citesc din pipe valorile trimise din Consumer, pe care le plasez
		//in obiectul de tip BufferedImage, acest lucru se face intr-un bloc
		//try-catch de asemenea.
		int rgb;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				try 
				{
					rgb =  dataInputStream.readInt();
					bufferedImage.setRGB(j, i, rgb);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			if(i == height/4)
			{
				System.out.println("Segmentul 1 a fost primit prin pipe in WriterResult");
			}
			if(i == 2*height/4)
			{
				System.out.println("Segmentul 2 a fost primit prin pipe in WriterResult");
			}
			if(i == 3*height/4)
			{
				System.out.println("Segmentul 3 a fost primit prin pipe in WriterResult");
			}
			if(i == height-1)
			{
				System.out.println("Segmentul 4 a fost primit prin pipe in WriterResult");
			}
		}
		
		//Inchid fluxul de intrare intr-un bloc try-catch deoarece datele s-au terminat.
		try 
		{
			dataInputStream.close();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		//Incerc sa scriu imaginea rezultata la path-ul specificat in args si afisez un mesaj
		//in cazul in care acest lucru a reusit.
		try 
		{
			File outputBMP = new File(outputPath);
		    ImageIO.write(bufferedImage, "bmp", outputBMP);
		    this.flag = 1;
		} 
		catch (IOException e) 
		{
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
        System.out.println("[Writer Result a fost pornit acum " + timp + " secunde si s-a incheiat]");
        
        if (flag == 1)
        {
        	System.out.println("Exista " + Timp.instancesNumber + " instante ale clasei Timp");
        	System.out.println("\n\n\nAm procesat pixelii si am salvat imaginea cu succes.");
        }
        
	}

}
