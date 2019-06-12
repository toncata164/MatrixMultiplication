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
	
	
	private static boolean quietMode = false;
	private static int workingThreads = 0;
	
	public static void quietMode(boolean value)
	{
		quietMode = value;
	}
	
	public static boolean quietMode()
	{
		return quietMode;
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
	
	
	//rows1 = 2
	//cols1 = 5
	//rows2 = 5
	//cols2 = 3
	//threads = 3
	@Override
	public void run()
	{
		fireThreadStarted(new MatrixThreadEvent(this, new Date()));
		//calculate how many work has to do the current thread
		int maxNumberOfOperations = first.getNumberOfRows() * second.getNumberOfColumns();
		int operationsPerThread = maxNumberOfOperations / numberOfThreads;
		int remainder = maxNumberOfOperations % numberOfThreads;
		if(threadIndex < remainder)
			operationsPerThread += 1;
				//+ (maxNumberOfOperations % numberOfThreads == 0 ? 0 : 1);
		
		int currentThreadFirstOperation = operationsPerThread * threadIndex;
		if(threadIndex >= remainder)
			currentThreadFirstOperation += remainder;
		//every row of first multiplies k times where k is number of columns of second
		int startRow = currentThreadFirstOperation / second.getNumberOfColumns();
		int startColumn = currentThreadFirstOperation % second.getNumberOfColumns();
		//the main work
		if(!quietMode)
		{
			System.out.printf("Thread %d will do %d operation%s\n", threadIndex, operationsPerThread, operationsPerThread==1?"":"s");
		}
		for(int k = 0; k < operationsPerThread && startRow < first.getNumberOfRows(); k++)
		{
			if(!quietMode)
				System.out.printf("Starts operation %d for thread %d\n", k, threadIndex);
			double[] r = first.getRow(startRow);
			if(!quietMode)
				System.out.printf("Thread %d gets row %d\n", threadIndex, startRow);
			double[] c = second.getColumn(startColumn);
			if(!quietMode)
				System.out.printf("Thread %d gets column %d\n", threadIndex, startColumn);
			double result = 0;
			for(int p = 0; p<r.length; p++)
			{
				result += r[p]*c[p];
			}
			multiplicationResult.setValueAt(result, startRow, startColumn);
			if(!quietMode)
				System.out.printf("Thread %d sets result at (%d %d)\n", threadIndex, startRow, startColumn);
			startColumn++;
			if(startColumn >= second.getNumberOfColumns())
			{
				startColumn = 0;
				startRow = startRow+1;
			}
			if(!quietMode)
				System.out.printf("End of operation %d for thread %d\n", k, threadIndex);
		}
			
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
