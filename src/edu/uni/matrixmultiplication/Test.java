package edu.uni.matrixmultiplication;

import java.util.Random;

public class Test
{

	public static void main(String[] args)
	{
		int m=2, n=3, k=3;
		int[][] A = new int[m][n];
		int[][] B = new int[k][m];
		generate(A);
		generate(B);
		print(A);
		print(B);
		MatrixThread[][] threads = new MatrixThread[m][m];
		int[][] result = new int[m][m];
		for(int i = 0; i<threads.length; i++)
		{
			for(int j = 0; j<threads[i].length; j++)
			{
				threads[i][j] = new MatrixThread(A[i], 
						getColumn(B, j), i, j);
				threads[i][j].addMatrixThreadListener(new MatrixThreadEventListener(){

					@Override
					public void threadFinished(MatrixThreadEvent e)
					{
						MatrixThread t = (MatrixThread) e.getSource();
						result[t.getRowIndex()][t.getColumnIndex()] = t.getResult();
					}
					
				});
				new Thread(threads[i][j]).start();
			}
		}
		
		while(MatrixThread.getWorkingThreads() > 0)
		{
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		print(result);
	}
	
	public static void print(int[][] matrix)
	{
		for(int i = 0; i<matrix.length; i++)
		{
			for(int j = 0; j<matrix[i].length; j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void generate(int[][] matrix)
	{
		Random r = new Random();
		for(int i = 0; i<matrix.length; i++)
		{
			for(int j = 0; j<matrix[i].length; j++)
			{
				matrix[i][j] = r.nextInt(2);
			}
		}
	}
	
	public static int[] getColumn(int[][] matrix, int colIndex)
	{
		int[] column = new int[matrix.length];
		for(int i = 0; i<matrix.length; i++)
		{
			column[i] = matrix[i][colIndex];
		}
		return column;
	}
 	
	

}
