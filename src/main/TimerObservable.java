package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import javax.swing.Timer;

import java.util.LinkedList;

/**
 * 
 * @author
 *
 */

public class TimerObservable extends Observable {

	private ComputeCoordinates computeCoordinatesObj;
	private Timer timer;
	private LinkedList<Object> CommandHistoryList = new LinkedList<Object>();
	private boolean loadGame; // need this to resume game after load.

	

	boolean gameFlag = true;
	private int replayFrameCounter;
	int count = 0;

	private SaveLogic saveLogic;
	private LoadFromFile loadFromFile;

	private LinkedList<Object> ReplayList = new LinkedList<Object>();
	private ArrayList<Object> shapeObjects;

	public boolean isLoadGame() {
		return loadGame;
	}

	public void setLoadGame(boolean loadGame) {
		this.loadGame = loadGame;
	}

	public LinkedList<Object> getCommandHistoryList() {
		return CommandHistoryList;
	}

	public void setCommandHistoryList(LinkedList<Object> commandHistoryList) {
		CommandHistoryList = commandHistoryList;
	}

	public LinkedList<Object> getReplayList() {
		return ReplayList;
	}

	public void setReplayList(LinkedList<Object> replayList) {
		ReplayList = replayList;
	}

	public LoadFromFile getLoadFromFile() {
		return loadFromFile;
	}

	public void setLoadFromFile(LoadFromFile loadFromFile) {
		this.loadFromFile = loadFromFile;
	}

	public SaveLogic getSaveLogic() {
		return saveLogic;
	}

	public boolean isGameFlag() {
		return gameFlag;
	}

	public void setGameFlag(boolean gameFlag) {
		this.gameFlag = gameFlag;
	}

	public LinkedList<Object> getHistoricCommandList() {
		return CommandHistoryList;
	}

	public ArrayList<Object> getShapeObjects() {
		return shapeObjects;
	}

	public void setShapeObjects(ArrayList<Object> shapeObjects) {
		this.shapeObjects = shapeObjects;
	}

	public ComputeCoordinates getComputeCoordinatesObj() {
		return computeCoordinatesObj;
	}

	public void setComputeCoordinatesObj(
			ComputeCoordinates computeCoordinatesObj) {
		this.computeCoordinatesObj = computeCoordinatesObj;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public TimerObservable() {
		this.computeCoordinatesObj = new ComputeCoordinates();
		this.timer = new Timer(5, null);
		this.replayFrameCounter = 0;
	}

	public void computeAndNotify() {
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameFlag) {
					if (count % 10 == 0) {
						CommandHistoryList.add(getComputeCoordinatesObj()
								.gameData());
					}
					ReplayList.add(getComputeCoordinatesObj().gameData());
					getComputeCoordinatesObj().performGameMovement();
					getComputeCoordinatesObj().updateDisplayClock();
					
					shapeObjects = getComputeCoordinatesObj()
							.getListShapeObjects();
					setChanged();
					notifyObservers(shapeObjects);
					count++;
				} else {

					StoreDimensions storeDimensions;
					if (replayFrameCounter < ReplayList.size()) {
						storeDimensions = (StoreDimensions) ReplayList
								.get(replayFrameCounter);
						getComputeCoordinatesObj().saveDimensions(
								storeDimensions);
						setChanged();
						shapeObjects = getComputeCoordinatesObj()
								.getListShapeObjects();
						setChanged();
						notifyObservers(shapeObjects);
						replayFrameCounter++;
					} else {
						replayFrameCounter = 0;
						setGameFlag(true);
						timer.stop();
						getComputeCoordinatesObj().setGameFlag(2);
					}
				}

				if(getComputeCoordinatesObj().getGameFlag() == 2)
				{
					deleteObservers();
					timer.stop();
				}
			}
		});
		timer.setDelay(5);
		timer.restart();
		setReplayList(ReplayList);
	}

	public void undoTesting() {
		this.timer.stop();
		System.out.println(CommandHistoryList);
		if (CommandHistoryList.size() != 0) {

			StoreDimensions storeDimensions = (StoreDimensions) this.CommandHistoryList
					.removeLast();
			System.out.println(storeDimensions.ballX);
			System.out.println(CommandHistoryList.size());

			getComputeCoordinatesObj().saveDimensions(storeDimensions);
			ReplayList.add(storeDimensions);
			this.CommandHistoryList.removeLast();
			shapeObjects = getComputeCoordinatesObj().getListShapeObjects();
			setChanged();
			notifyObservers(shapeObjects);
		}
	}

	public void pauseGame() {
		this.getTimer().stop();
	}

	public void resumeGame() {
		if (isLoadGame()) {
			computeAndNotify();
		}
		this.getTimer().setDelay(5);
		this.getTimer().restart();
	}

	public void saveGame() {
		// TODO Auto-generated method stub
		saveLogic.setListToSave(ReplayList);
		saveLogic.save();
	}

	public void loadGame() {
		// TODO Auto-generated method stub
		setLoadGame(true);
		StoreDimensions storeDimensions;


		LinkedList<Object> list = loadFromFile.load();
		setReplayList(list); // setting replay list for replay after load.
		setCommandHistoryList(list); // setting command list for undo after load.

		storeDimensions = (StoreDimensions) list.get(list.size() - 1);
		getComputeCoordinatesObj().saveDimensions(storeDimensions);
		shapeObjects = getComputeCoordinatesObj().getListShapeObjects();
		setChanged();
		notifyObservers(shapeObjects);
	}

	public void setSaveLogic(SaveLogic saveLogic) {
		// TODO Auto-generated method stub
		this.saveLogic = saveLogic;
	}
}
