package edu.uni.matrixmultiplication;

import java.util.EventListener;

public interface MatrixThreadEventListener extends EventListener
{
	public void threadStarted(MatrixThreadEvent e);
	public void threadFinished(MatrixThreadEvent e);
	public void allThreadsFinished(MatrixThreadEvent e);
}
