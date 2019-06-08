package edu.uni.matrixmultiplication;

import java.util.Date;
import java.util.EventObject;

public class MatrixThreadEvent extends EventObject
{

	public MatrixThreadEvent(MatrixThread source, Date eventTime)
	{
		super(source);
	}

}
