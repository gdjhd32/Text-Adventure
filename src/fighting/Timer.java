package fighting;

import java.util.LinkedList;
import java.util.Random;

public class Timer extends Thread {

	private LinkedList<TimerObject> timer;
	private TimerObject lastTimer;
	private boolean isLastTimer = false;
	private Timer otherTimer;

	// temporary
	boolean isPlayer;

	public Timer(TimerObject[] timer, Timer otherTimer, boolean isPlayer) {

		this.isPlayer = isPlayer;

		this.otherTimer = otherTimer;
		this.timer = new LinkedList<TimerObject>();
		for (int i = 0; i < timer.length; i++) {
			if (timer[i].timerLength() == -1)
				lastTimer = timer[i];
			else if (timer[i].timerLength() == 0)
				continue;
			else
				this.timer.add(timer[i]);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			int timeToWait = 0;
			if(!isPlayer) {
				timeToWait = (int) ((Math.random() * 5 + 0.5) * 1000);
				System.out.println("Time to Wait: " + timeToWait);
			}
			
			int sleptTime = 0;
			while (!timer.isEmpty()) {
				TimerObject[] currentTimer = shortestTimer();
				System.out.println(
						"isPlayer: " + isPlayer + ": I will sleep for " + currentTimer[0].timerLength() / 1000 + " seconds. Renmaining timer: " + timer.size());
				int timeToSleep = currentTimer[0].timerLength() - sleptTime;
				
				if(sleptTime + timeToSleep > timeToWait && !isPlayer) {
					this.sleep(timeToWait - sleptTime);
				} else {
					this.sleep(timeToSleep);	
				}
				
				sleptTime += timeToSleep;
				for (int i = 0; i < currentTimer.length; i++) {
					currentTimer[i].timerEnd();
				}
			}
			System.out.println("isPlayer: " + isPlayer + ": I am done.");
			isLastTimer = true;
			if (lastTimer != null && otherTimer.isLastTimer())
				lastTimer.timerEnd();

		} catch (InterruptedException e) {
			;
		}

	}

	private TimerObject[] shortestTimer() {
		LinkedList<TimerObject> shortestTimer = new LinkedList<TimerObject>();
		shortestTimer.add(timer.get(0));
		for (int i = 0; i < timer.size(); i++) {
			if (timer.get(i).timerLength() < shortestTimer.get(0).timerLength()) {
				shortestTimer.clear();
				shortestTimer.add(timer.get(i));
			} else if (timer.get(i).timerLength() == shortestTimer.get(0).timerLength())
				shortestTimer.add(timer.get(i));

		}
		for (int i = 0; i < shortestTimer.size(); i++) {
//			System.out.println(((CombatAction) shortestTimer.get(i)).key);
			timer.remove(shortestTimer.get(i));
		}
		return shortestTimer.toArray(new TimerObject[shortestTimer.size()]);
	}

	public boolean isLastTimer() {
		return isLastTimer;
	}

	public void setOtherTimer(Timer otherTimer) {
		this.otherTimer = otherTimer;
	}

}
