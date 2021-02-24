package test;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.SwingUtilities;

public class CarThread extends Thread
{
	JButtonPlus b = null;
	int x;
	int y;
	boolean horizontal = true;
	int interval;
	Vector<JButtonPlus> registeredCars = null;

	public CarThread(JButtonPlus _b, boolean _horizontal, int _interval, Vector<JButtonPlus> _registeredCars)
	{
		horizontal = _horizontal;
		b = _b;
		interval = _interval;
		registeredCars = _registeredCars;
	}

	private void moveLocation(int x, int y)
	{
		b.setLocation(x,y);
	}

	private void removeCar()
	{
		synchronized (b)
		{
			// notify for others
			b.waiting = false;
			b.notifyAll();
		}
		synchronized (registeredCars)
		{
			registeredCars.remove(b);
		}
		b.setVisible(false);
		b = null;
	}

	private boolean amItooClose(JButtonPlus _other)
	{
		if (horizontal == true)
		{
			if ( _other.getLocation().y == b.getLocation().y)
			{
				if ((_other.getLocation().x - b.getLocation().x) > 0 && (_other.getLocation().x - b.getLocation().x) <= 20)
				{
					return true;
				}
			}
			else // 상대차는 수직방향 이동중
			{
				if (Math.abs(b.getLocation().y - _other.getLocation().y ) <= 20 &&
					(_other.getLocation().x - b.getLocation().x) > 0 && (_other.getLocation().x - b.getLocation().x) <= 15)
				{
					if (Math.abs(b.getLocation().y - _other.getLocation().y ) <= Math.abs(_other.getLocation().x - b.getLocation().x))
					{
						return true;
					}
				}
			}
		}
		else // vertical
		{
			if (_other.getLocation().x == b.getLocation().x)
			{
				if ((_other.getLocation().y - b.getLocation().y) > 0 && (_other.getLocation().y - b.getLocation().y) <= 20)
				{
					return true;
				}
			}
			else // 상대차는 수평방향 이동 중
			{

				if (Math.abs(b.getLocation().x - _other.getLocation().x ) <= 20 &&
					(_other.getLocation().y - b.getLocation().y) > 0 && (_other.getLocation().y - b.getLocation().y) <= 15)
				{
					if (Math.abs(b.getLocation().y - _other.getLocation().y ) > Math.abs(_other.getLocation().x - b.getLocation().x))
					{
						return true;
					}

				}

			}
		}
		return false;
	}

	@Override
	public void run()
	{
		x = b.getLocation().x;
		y = b.getLocation().y;

		while (x < 590 && y < 390)
		{
			JButtonPlus btn = null;
			synchronized(registeredCars)
			{

				Iterator<JButtonPlus> it = registeredCars.iterator();
				boolean found = false;
				while (it.hasNext())
				{
					btn = it.next();
					if (btn != b && amItooClose(btn) == true)
					{

						found = true;
						btn.waiting = true;
						break;
					}
				}

				if (found == false)
				{
					btn = null;
				}
			}

			if (btn != null)
			{
				synchronized (btn)
				{
					try
					{
						if (btn.waiting == true)
						{
							btn.wait();
							//System.out.println(this.getName()+" "+b.getBackground().toString()+" wait--------------------> ");
						}
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}


			if (btn == null || amItooClose(btn) == false)
			{

				if (horizontal == true)
				{
					x = x+1;
				}
				else
				{
					y = y+1;
				}
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					moveLocation(x, y);
				}
			});

			synchronized (b)
			{
				// notify for others
				b.waiting = false;
				b.notifyAll();
			}

			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				removeCar();
			}
		});
	}
}
