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
	
	
	//rows1 = 5
	//cols1 = 14
	//rows2 = 14
	//cols2 = 9
	//threads = 7
	//4
	@Override
	public void run()
	{
		fireThreadStarted(new MatrixThreadEvent(this, new Date()));
		//calculate how many work has to do the current thread
		//45
		int maxNumberOfOperations = first.getNumberOfRows() * second.getNumberOfColumns();
		//6+1=7
		int operationsPerThread = maxNumberOfOperations / numberOfThreads + 
				maxNumberOfOperations % numberOfThreads == 0 ? 0 : 1;
		//0, 7, 14, 21, 28, 35, 42
		int currentThreadFirstOperation = operationsPerThread * threadIndex;
		//every row of first multiplies k times where k is number of columns of second
		//1 row - > 9 op
		//0 r -> 0-8 op
		//1 r -> 9-17 op
		//2 r -> 18-26 op
		//3 r -> 27-35 op
		//4 r -> 36-44 op
		int startRow = -1;
		for(int row = 0; row<first.getNumberOfRows(); row++)
		{
			int start = row*operationsPerThread;
			int end = start+operationsPerThread-1;
			if(currentThreadFirstOperation >= start && end >= currentThreadFirstOperation)
			{
				startRow = row;
			}
		}
		int startColumn = currentThreadFirstOperation - (startRow*operationsPerThread);//currentThreadFirstOperation % second.getNumberOfColumns();
		
		//the main work
		
		int j = startColumn;
		int i = startRow;
		for(int k = 0; k < operationsPerThread; k++)
		{
			double[] r = first.getRow(i);
			double[] c = second.getColumn(j);
			double result = 0;
			for(int p = 0; p<r.length; p++)
			{
				result += r[p]*c[p];
			}
			multiplicationResult.setValueAt(result, i, j);
			j++;
			if(j >= second.getNumberOfColumns())
			{
				j = 0;
				i = i+1;
			}
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
