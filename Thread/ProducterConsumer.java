import java.util.concurrent.locks.*;
class Resource
{
	private String name;
	private int count = 1;
	private boolean flag = false;
	Lock l = new ReentrantLock();
	Condition producter_con = l.newCondition();
	Condition consumer_con = l.newCondition();
	public void set(String name)
	{
		l.lock();
		try{
			while(flag)
			{
				try
				{
					producter_con.await();
				}
				catch (InterruptedException e)
				{
				}
			}
            this.name = name + count;
			System.out.println(Thread.currentThread().getName()+"................................Producter....."+this.name);
			count++;
			flag = true;
			consumer_con.signal();			
		}
		finally
		{
			l.unlock();
		}
	}
	public void out()
	{
		l.lock();
        try{
			while(!flag)
			{
				try
				{
					consumer_con.await();
				}
				catch (InterruptedException e)
				{
				}
			}
			System.out.println(Thread.currentThread().getName()+".....Consumer....."+name);
			flag = false;
			producter_con.signal();			
		}
		finally
		{
			l.unlock();
		}
	}

}
class Producter implements Runnable
{
	Resource r;
	Producter(Resource r)
	{
		this.r = r;
	}
    public void run()
    {
        while(true)
        {
            r.set("¿¾Ñ¼");
        }
    }
}
class Consumer implements Runnable
{
	Resource r;
	Consumer(Resource r)
	{
		this.r = r;
	}
    public void run()
    {
        while(true)
        {
            r.out();
        }
    }
}
class ProducterConsumer 
{
	public static void main(String[] args) 
	{
		Resource r = new Resource();
		Producter p = new Producter(r);
		Consumer c = new Consumer(r);

		Thread t0 = new Thread(p);
		Thread t1 = new Thread(p);
		Thread t2 = new Thread(c);
		Thread t3 = new Thread(c);

		t0.start();
		t1.start();
		t2.start();
		t3.start();
        System.out.println("OVER")

	}
}
