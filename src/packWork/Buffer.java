//Neleptcu Daniel-Andrei
package packWork;

//Definesc clasa Buffer, din care voi instantia un obiect comun
//pentru Producer si Consumer.
public class Buffer 
{
	//Datele ce vor fi stocate in buffer si flag-ul available ce
	//este folosit in metodele synchronized pentru a asigura ordinea
	//scriere->citire->scriere.
	private int [] pixel;
	private int height;
	private int width;
	public boolean available = false;
	
	//Bloc initializare 
	{
		height = 0;
		width = 0;
	}
	
	//Am declarat 2 metode getHeight si getWidth sincronizate pentru a 
	//putea returna dimensiunile matricei cand acestea au fost introduse in buffer
	//Apoi am o metoda get simpla ce imi returneaza vectorul de pixeli stocat in buffer
	
	//Metoda put fara parametru este folosita pentru a stoca vectorul de pixeli transmis
	//de Producer in buffer. Metoda este rescrisa cu 2 parametri pentru a stoca dimensiunile
	//transmise in buffer de catre Producer.
	public synchronized int getHeight() 
	{
		while (!available) 
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		available = false;
		notifyAll();
		return height;
	}
	
	public synchronized int getWidth() 
	{
		while (!available) 
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		available = false;
		notifyAll();
		return width;
	}
	
	public synchronized void put (String which, int... varargs) 
	{
		while (available) 
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		if (varargs.length == 2)
		{
			this.height = varargs[0];
			this.width = varargs[1];
		}
		else if(varargs.length == 1)
		{
			if(which == "height")
			{
				this.height = varargs[0];
			}
			if(which == "width")
			{
				this.width = varargs[0];
			}
		}
		available = true;
		notifyAll();
	}
	
	public synchronized int []  get() 
	{
		while (!available) 
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		available = false;
		notifyAll();
		return pixel;
	}
	
	public synchronized void put (int [] values) //puteam respecta aici varargs folosing ... in loc de []
	{
		while (available) 
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		pixel = new int[width];
		for (int i =0; i<width; i++)
		{
			pixel[i]=values[i];
		}
		available = true;
		notifyAll();
	}

}