package edu.uni.matrixmultiplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test
{

	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<>();
		for(int i = 0; i<args.length; i+=2)
		{
			params.put(args[i], args[i+1]);
		}
		
		int m = -1, n = -1, k = -1, t = -1;
		String fileName = null;
		String output = null;
		
		if(params.containsKey("-m"))
			m = Integer.parseInt(params.get("-m"));
		if(params.containsKey("-n"))
			n = Integer.parseInt(params.get("-n"));
		if(params.containsKey("-k"))
			k = Integer.parseInt(params.get("-k"));
		if(params.containsKey("-t"))
			t = Integer.parseInt(params.get("-t"));
		if(params.containsKey("-i"))
			fileName = params.get("-i");
		if(params.containsKey("-o"))
			output = params.get("-o");
		
		Matrix first = null;
		Matrix second = null;
		Matrix result = null;
		
		if(m != -1 && n != -1 && k != -1 && fileName == null)
		{
			second  = new Matrix(n, k);
			second.generateRandomMatrix(0, 10);
			first = new Matrix(m, n);
			first.generateRandomMatrix(0, 10);
			result = new Matrix(m, k);
		}
		else if(fileName != null && m == -1 && n == -1 && k == -1)
		{
			try
			{
				readMatricesFromFile(fileName, first, second);
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (FileFormatException e)
			{
				e.printStackTrace();
			}
			result = new Matrix(first.getNumberOfRows(), second.getNumberOfColumns());
		}
		else
		{
			System.out.print("Wrong parameters passed to application!");
		}
		
		if(t == -1)
			t = 1;
		
		for(int i = 0; i<t; i++)
		{
			MatrixThread matrixThread = new MatrixThread(first, second, i, t, result);
			Thread tm = new Thread(matrixThread);
			tm.start();
		}
	}

	private static void readMatricesFromFile(String fileName, Matrix first, Matrix second) throws IOException, FileFormatException
	{
		List<String> rows = Files.readAllLines(new File(fileName).toPath());
		String[] firstRow = rows.get(0).trim().split(" ");
		if(firstRow.length != 3)
		{
			throw new FileFormatException("The dimensions of the matrices must be 3 integer not negative and not zero values.");
		}
		int m = Integer.parseInt(firstRow[0]);
		int n = Integer.parseInt(firstRow[1]);
		int k = Integer.parseInt(firstRow[2]);
		
		first = new Matrix(m, n);
		second = new Matrix(n, k);
		
		
		
	} 	
}
