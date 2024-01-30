//Neleptcu Daniel-Andrei 332AB
package packWork;

//Clasa abstracta (desi nu era necesar, deoarece putem sa o folosim pentru
//a calcula timpul si in alte situatii) Timp ce extinde Thread pentru a avea 
//acces celelalte clase la Thread si implementeaza interfata InterfataTimp
public abstract class Timp extends Thread implements InterfataTimp 
{
	//Clasa are timpul de inceput si timpul de sfarsit calculate intr-un
	//anumit punct, respectiv duratia care este calculata in metoda getTime.
	private long startTime;
	private long endTime;
	private long duration;
	public static int instancesNumber;
	
	//bloc static de initializare pentru numar instante
	static 
	{
		instancesNumber = 0;
	}
	
	public Timp()
	{
		instancesNumber++;
	}
	//Tstart si Tstop atribuie timpul in nanosecunde variabilelor corespunzatoare
	//iar getTime calculeaza in secunde diferenta dintre momentul final si cel initial
	//si returneaza valoarea ca un float.
	public void tStart() 
	{
		startTime = System.nanoTime();
	}
	
	public void tStop()
	{
		endTime = System.nanoTime();
	}
	
	public float getTime()
	{
		duration = (endTime - startTime)/1000000;
		return ((float)duration)/1000;
	}
	
}
