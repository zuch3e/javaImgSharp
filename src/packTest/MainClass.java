//Neleptcu Daniel-Andrei 332AB
//Imi selectez pachetele necesare functionarii clasei MainClass
package packTest;
import java.io.*;
import packWork.*;
public class MainClass {
	
    public static void main(String[] args) {
        
    	// Verific daca a fost introdus numarul potrivit de argumente pentru aplicatie
    	// Pentru 2 aplicatia va rula.
    	if (args.length == 2 || args.length == 1) 
        {
    		//Selectez input path-ul si output path-ul din args
        	String inputPath = args[0];
        	String outputPath;
        	if (args.length == 1)
        	{
        		outputPath = "sharpenOutput.bmp";
        	}
        	else
        	{
        		outputPath = args[1];
        	}
        	
        	//Imi instantiez obiectul  buffer ce va fi folosit de Producer si Consumer
        	Buffer b = new Buffer();
        	
        	//Imi creez fluxul de pipe-uri si realizez conexiunea dintre acestea.
        	//Pipe-urile vor fi folosite in Consumer si WriterResult pentru a transmite 
        	//informatia intre acestea
        	PipedOutputStream pipeOut = new PipedOutputStream();
        	PipedInputStream pipeIn = new PipedInputStream();
        	DataOutputStream out = new DataOutputStream(pipeOut);
        	DataInputStream in = new DataInputStream(pipeIn);
        	
        	//Verific daca pot realiza conexiunea folosind un bloc try-catch
        	try {
				pipeIn.connect(pipeOut);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	
        	//Imi instantiez obiectele Producer -> ImageReader
        	//						   Consumer -> ImageSharpen
        	//						   WriterResult
        	//Aceste obiecte sunt extind prin ierarhia lor clasa thread.
            ImageReader thread1 = new ImageReader(b, inputPath);
            ImageSharpen thread2 = new ImageSharpen(b, out);
            WriterResult thread3 = new WriterResult(in, outputPath);
            
            //Pornesc threadurile in acelasi timp.
            thread1.start();
            thread2.start();
            thread3.start();
            
            //Fortez thread-ul main sa astepte dupa terminarea celor 3 thread-uri
            //Acest lucru necesita folosirea unui bloc try-catch.
            try 
            {
            	thread1.join();
            	thread2.join();
            	thread3.join();
            	
			} 
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}
            
            //Mesajul care imi specifica ca cele 3 thread-uri s-au terminat cu succes.
            System.out.println("Am terminat main.");
        }
    	//Aici afisez un mesaj specific in caz ca se introduce numarul gresit de argumente in args.
        else
        {
        	System.out.println("Aplicatia a fost cu mai multe argumente decat trebuia. " + args.length + " argumente in loc de 2.");
        }
    }
}