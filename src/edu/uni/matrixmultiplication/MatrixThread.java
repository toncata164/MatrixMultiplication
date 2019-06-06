package edu.uni.matrixmultiplication;

import javax.swing.event.EventListenerList;

public class MatrixThread implements Runnable
{	
	private int[] row;
	private int[] column;
	private int result;
	private int rowIndex;
	private int columnIndex;

	private static int workingThreads = 0;
	
	private EventListenerList events;
	
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
	
	public MatrixThread(int[] row, int[] column, int rowIndex, int columnIndex)
	{
		this.row = row;
		this.column = column;
		result = 0;
		events = new EventListenerList();
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		workingThreads+=1;
	}
	
	
	@Override
	public void run()
	{
		
		for(int i = 0; i<row.length; i++)
		{
			result += row[i]*column[i];
		}
		fireThreadFinished(new MatrixThreadEvent(this));
		a();
	}
	
	private static synchronized void a()
	{
		workingThreads-=1;
	}
	
	public static int getWorkingThreads()
	{
		return workingThreads;
	}
	
	public int getResult()
	{
		return result;
	}
	
	public int getRowIndex()
	{
		return rowIndex;
	}
	
	public int getColumnIndex()
	{
		return columnIndex;
	}
}
