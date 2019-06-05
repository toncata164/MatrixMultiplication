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
		for(int i = 0; i<threads.length; i++)
		{
			for(int j = 0; j<threads[i].length; j++)
			{
				threads[i][j] = new MatrixThread(A[i], 
						getColumn(B, j));
				new Thread(threads[i][j]).start();
			}
		}
		int[][] result = new int[m][m];
		boolean ready = true;
		do
		{
			ready = true;
			for(int i = 0; i<threads.length; i++)
			{
				for(int j = 0; j<threads[i].length; j++)
				{
					if(threads[i][j].isReady())
						result[i][j] = threads[i][j].getResult();
					ready = ready && threads[i][j].isReady();
					if(!ready)
						break;
				}
				if(!ready)
					break;
			}
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(!ready);
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
