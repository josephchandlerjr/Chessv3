package com.chessv3;

public interface Observable{
	public void registerObserver(Observer obs);
	public void notifyObservers();
}
