//Neleptcu Daniel-Andrei 332AB
//Imi selectez pachetele necesare functionarii clasei ImageReader(Producer)
package packWork;
import java.io.*;

//Definesc clasa Imagesharpen care mosteneste clasa startProcess
public class ImageSharpen extends StartProcess {
	//Clasa contine path-ul pentru imaginea sursa si buffer-ul comun
	//pentru Producer si Consumer.
	private Buffer buffer;
	private DataOutputStream dataOutputStream;
	
	
	//Constructorul pentru clasa ImageSharpen
	//Deoarece in contextul acestui proiect clasele nu au fost instantiate
	//fara parametru, nu adaugat constructor fara parametru.
	public ImageSharpen(Buffer newBuffer, DataOutputStream dataOutStream)
	{
		buffer = newBuffer;
		this.dataOutputStream = dataOutStream;
	}
	
	//O functie pe care o folosesc pentru a salva marginile imaginii pe care
	//doresc sa o transform folosind convolutia cu un kernel de sharpen. In 
	//mod general se considera ca vecinii exteriori muchiilor sunt 0 in operatia
	//de convolutie, dar acest rezultat poate genera valori mult prea deschise
	//din punct de vedere al culorii deoarece nu se modifica valoarea din centrul
	//kernel-ului in functie de cate valori sunt 0 sau nu. Astfel imaginea arata
	//mai apropiata de cea originala decat cea in care am considera 0 vecinii.
	//In concluzie sharpen-ul se va face pe blocul [length-1]x[width-1].
	public void saveBorders(int [][][] a, int [][][] b)
	{
		int height = a.length;
		int width = a[0].length;
		for(int i = 0 ; i < height; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				b[i][0][j] = a[i][0][j];
				b[height-i-1][width-1][j] = a[height-i-1][width-1][j];
			}
		}
		for(int i = 0 ; i < width; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				b[0][i][j] = a[0][i][j];
				b[height-1][width-i-1][j] = a[height-1][width-i-1][j];
			}
		}
	}
	
	//Suprascriu metoda abstracta processImage din clasa startProcess
	@Override
	public void processImage()
	{
		//Folosesc un bloc try-catch petnru a pune thread-ul pe sleep o secunda.
		try 
		{
			Thread.sleep(100);
			System.out.println("Am intrat in ImageSharpen");
			System.out.println("Astept o secunda in ImageSharpen");
			Thread.sleep(1000);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		
		//Preiau dimensiunile pozei din buffer-ul comun si declar
		//matricile pe care le voi folosi.
		int height = buffer.getHeight();
		int width = buffer.getWidth();
		int[][][] pixelMatrix = new int[height][width][3];
		int[][][] pixelMatrixSharp = new int[height][width][3];
		
		
		//preiau pixelii din buffer pe care ii impart in valorile lor pe culorile
		//Red Green Blue. (coloanele [0], [1], [2] )
		for(int i = 0 ; i < height; i++)
		{	
			int [] vec = buffer.get();
			for (int j = 0; j < width; j++)
			{
				pixelMatrix[i][j][0] = (vec[j] >> 16) & 0xFF;
				pixelMatrix[i][j][1] = (vec[j] >> 8) & 0xFF;
				pixelMatrix[i][j][2] = vec[j] & 0xFF;
			}
		}
		
		//Apelez functia care salveaza muchiile
		saveBorders(pixelMatrix,pixelMatrixSharp);
		System.out.println("Am primit pixelii, voi incepe procesarea intr-o secunda");
		
		//Pun thread-ul sa doarma o secunda, in blocul try-catch
		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e2) 
		{
			e2.printStackTrace();
		}
		
		//Declar kernel-ul folosit si transmit folosind blocul try-catch pentru
		//a trata exceptiile dimensiunile matricei prin pipe.
		int [][] kernel = { {-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1} };
		try 
		{
			dataOutputStream.writeInt(height);
			dataOutputStream.writeInt(width);
			dataOutputStream.flush();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		//Aplic propriu zis convolutia pentru matrice. Nu am fost sigur daca avem voie
		//Sa folosim clasele Kernel, ConvolveOp etc. asa ca l-am implementat manual, pe 
		//pixeli.
		for(int i = 1 ; i < height - 1 ; i++)
		{
			for(int j = 1; j < width - 1 ; j++)
			{
				pixelMatrixSharp[i][j][0] = 0;
				for(int k = -1; k < 2; k++)
				{
					for(int m = -1; m < 2; m++)
					{
						pixelMatrixSharp[i][j][0] += pixelMatrix[i+k][j+m][0] * kernel[k+1][m+1];
						pixelMatrixSharp[i][j][1] += pixelMatrix[i+k][j+m][1] * kernel[k+1][m+1];
						pixelMatrixSharp[i][j][2] += pixelMatrix[i+k][j+m][2] * kernel[k+1][m+1];
					}
				}
				pixelMatrixSharp[i][j][0] = Math.min(255,  Math.max(0, pixelMatrixSharp[i][j][0]));
				pixelMatrixSharp[i][j][1] = Math.min(255,  Math.max(0, pixelMatrixSharp[i][j][1]));
				pixelMatrixSharp[i][j][2] = Math.min(255,  Math.max(0, pixelMatrixSharp[i][j][2]));
		    }
		}
		
		//Parcurg matricea din nou intr-un bloc try-catch pentru a putea transmite fiecare valoare
		//prin pipe-ul dintre ImageSharpen si WriterResult. Cele 3 culori sunt construite inapoi
		//in pixelul rgb care este transmis prin pipe.
		try 
		{
			for(int i = 0 ; i < height ; i++)
			{
				for(int j = 0; j < width ; j++)
				{
					int rgb =  (0xFF << 24) | (pixelMatrixSharp[i][j][0] << 16) | (pixelMatrixSharp[i][j][1] << 8) | pixelMatrixSharp[i][j][2];
			        
					dataOutputStream.writeInt(rgb);
					dataOutputStream.flush();
				}
			}
			dataOutputStream.close();
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
        System.out.println("[Image Sharpen a fost pornit acum " + timp + " secunde si s-a incheiat]");
	}

}
