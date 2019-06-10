package edu.uni.matrixmultiplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test
{
	private static MatrixThreadEventListener e = new MatrixThreadEventListener()
	{
		
		@Override
		public void threadStarted(MatrixThreadEvent e)
		{
			Date d = e.getEventTime();
			MatrixThread t = (MatrixThread)e.getSource();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
			System.out.printf("Thread %d started at %s\n", t.getIndex(), sdf.format(d));
		}
		
		@Override
		public void threadFinished(MatrixThreadEvent e)
		{
			Date d = e.getEventTime();
			MatrixThread t = (MatrixThread)e.getSource();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
			System.out.printf("Thread %d finished at %s\n", t.getIndex(), sdf.format(d));
		}
		
		@Override
		public void allThreadsFinished(MatrixThreadEvent e)
		{
			System.out.println("All the work is ready!");
		}
	};

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
		
		ByRef<Matrix> first = new ByRef<>();
		ByRef<Matrix> second = new ByRef<>();
		Matrix result = null;
		
		if(m != -1 && n != -1 && k != -1 && fileName == null)
		{
			second.setValue(new Matrix(n, k));
			second.getValue().generateRandomMatrix(0, 10);
			first.setValue(new Matrix(m, n));
			first.getValue().generateRandomMatrix(0, 10);
			/*System.out.println(first.getValue());
			System.out.println();
			System.out.println(second.getValue());*/
			result = new Matrix(m, k);
		}
		else if(fileName != null && m == -1 && n == -1 && k == -1)
		{
			try
			{
				readMatricesFromFile(fileName, first, second);
				/*System.out.println(first.getValue());
				System.out.println();
				System.out.println(second.getValue());*/
			
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (FileFormatException e)
			{
				e.printStackTrace();
			}
			//result = new Matrix(first.getNumberOfRows(), second.getNumberOfColumns());
		}
		else
		{
			System.out.print("Wrong parameters passed to application!");
		}
		
		if(t == -1)
			t = 1;
		
		for(int i = 0; i<t; i++)
		{
			MatrixThread matrixThread = new MatrixThread(first.getValue(), second.getValue(), i, t, result);
			matrixThread.addMatrixThreadListener(e);
			Thread tm = new Thread(matrixThread);
			tm.start();
		}
	}

	private static void readMatricesFromFile(String fileName, ByRef<Matrix> first, ByRef<Matrix> second) throws IOException, FileFormatException
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
		
		first.setValue(new Matrix(m, n));
		second.setValue(new Matrix(n, k));
		
		int column = 0;
		for(int row = 1; row<=m; row++)
		{
			String[] numbers = rows.get(row).trim().split(" ");
			for(String number : numbers)
			{
				first.getValue().setValueAt(Double.parseDouble(number), row-1, column++);
			}
			column = 0;
		}
		
		for(int row = m+1; row<m+n; row++)
		{
			String[] numbers = rows.get(row).trim().split(" ");
			for(String number : numbers)
			{
				second.getValue().setValueAt(Double.parseDouble(number), row-m-1, column++);
			}
			column = 0;
		}
	} 	
}
