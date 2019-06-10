package edu.uni.matrixmultiplication;

import java.util.Date;
import java.util.Random;

import javax.swing.event.EventListenerList;

public class MatrixThread implements Runnable
{
	private Matrix first;
	private Matrix second;
	private int threadIndex;
	private Matrix multiplicationResult;
	private int numberOfThreads;
	
	
	private static boolean showLog = true;
	private static int workingThreads = 0;
	
	public static void showLog(boolean value)
	{
		showLog = value;
	}
	
	public int getIndex()
	{
		return threadIndex;
	}
	
	private EventListenerList events = new EventListenerList();
	
	public void addMatrixThreadListener(MatrixThreadEventListener l)
	{
		events.add(MatrixThreadEventListener.class, l);
	}
	
	public void removeMatrixThreadListener(MatrixThreadEventListener l)
	{
		events.remove(MatrixThreadEventListener.class, l);
	}
	
	private void fireThreadFinished(MatrixThreadEvent e)
	{
		Object[] o = events.getListenerList();
		for(int i = 0; i<o.length; i+=2)
		{
			if(o[i] == MatrixThreadEventListener.class)
			{
				((MatrixThreadEventListener) o[i+1]).threadFinished(e);
			}
		}
	}
	
	private void fireAllThreadsFinished(MatrixThreadEvent e)
	{
		Object[] o = events.getListenerList();
		for(int i = 0; i<o.length; i+=2)
		{
			if(o[i] == MatrixThreadEventListener.class)
			{
				((MatrixThreadEventListener) o[i+1]).allThreadsFinished(e);
			}
		}
	}
	
	private void fireThreadStarted(MatrixThreadEvent e)
	{
		Object[] o = events.getListenerList();
		for(int i = 0; i<o.length; i+=2)
		{
			if(o[i] == MatrixThreadEventListener.class)
			{
				((MatrixThreadEventListener) o[i+1]).threadStarted(e);
			}
		}
	}
	
	public MatrixThread(Matrix first, Matrix second, int threadIndex, int numberOfThreads, Matrix multiplicationResult)
	{
		this.first = first;
		this.second = second;
		this.threadIndex = threadIndex;
		this.numberOfThreads = numberOfThreads;
		this.multiplicationResult = multiplicationResult;
		workingThreads+=1;
	}
	
	
	//r = 4 c = 7  t = 5
	
	@Override
	public void run()
	{
		fireThreadStarted(new MatrixThreadEvent(this, new Date()));
		//calculate how many work has to do the current thread
		/*int maxNumberOfOperations = first.getNumberOfRows() * second.getNumberOfColumns();
		int operationsPerThread = maxNumberOfOperations / numberOfThreads;
		int currentThreadFirstOperation = operationsPerThread * threadIndex;
		//every row of first multiplies k times where k is number of columns of second
		int startRow = currentThreadFirstOperation / first.getNumberOfRows()-1;
		int startColumn = currentThreadFirstOperation % second.getNumberOfColumns();*/
		//the main work
		
		fireThreadFinished(new MatrixThreadEvent(this, new Date()));
		decreaseNumberOfWorkingThreadsByOne();
	}
	
	private synchronized void decreaseNumberOfWorkingThreadsByOne()
	{
		workingThreads-=1;
		if(workingThreads == 0)
		{
			fireAllThreadsFinished(new MatrixThreadEvent(this, new Date()));
		}
	}
	
	public static int getWorkingThreads()
	{
		return workingThreads;
	}
}
