package Principal;

import java.util.concurrent.Semaphore;

public class Meta 
{
	private boolean hayGanador;
	
	public Meta()
	{
		hayGanador=false;

	}
	
	public synchronized void inicio() 
	{
		try 
		{
			wait();
			
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public synchronized void cruzar( ) 
	{
		notifyAll();
		if (!hayGanador)
		{
			hayGanador=true;
		}
	}
}