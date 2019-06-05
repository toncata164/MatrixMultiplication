package edu.uni.matrixmultiplication;

public class MatrixThread implements Runnable
{
	private int[] row;
	private int[] column;
	private int result;
	private boolean isReady;
	
	public MatrixThread(int[] row, int[] column)
	{
		this.row = row;
		this.column = column;
		result = 0;
		isReady = false;
	}
	
	@Override
	public void run()
	{
		for(int i = 0; i<row.length; i++)
		{
			result += row[i]*column[i];
		}
		isReady = true;
	}
	
	public int getResult()
	{
		return result;
	}
	
	public boolean isReady()
	{
		return isReady;
	}
}
