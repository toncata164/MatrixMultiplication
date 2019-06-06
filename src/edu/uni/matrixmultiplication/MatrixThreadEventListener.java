package edu.uni.matrixmultiplication;

import java.util.EventListener;

public interface MatrixThreadEventListener extends EventListener
{
	public void threadFinished(MatrixThreadEvent e);
}
