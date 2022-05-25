package fighting;

import java.util.LinkedList;

import fighting.CombatAutomat.CombatAction;

public class Timer extends Thread {

	LinkedList<TimerObject> timer;
	TimerObject lastTimer;
	
	public Timer(TimerObject[] timer) {
		this.timer = new LinkedList<TimerObject>();
		for(int i = 0; i < timer.length; i++) {
			if(timer[i].timerLength() == -1) 
				lastTimer = timer[i];
			else if(timer[i].timerLength() == 0)
				continue;
			else
				this.timer.add(timer[i]);
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			while(!timer.isEmpty()) {
				TimerObject[] currentTimer = shortestTimer();
				this.sleep(currentTimer[0].timerLength());
				for(int i = 0; i < currentTimer.length; i++) {
					currentTimer[i].timerEnd();
				}
			}
			if(lastTimer != null)
				lastTimer.timerEnd();
		} catch (InterruptedException e) {
			System.out.println("Timer was interrupted.");
		}
		
	}
	
	private TimerObject[] shortestTimer() {
		LinkedList<TimerObject> shortestTimer = new LinkedList<TimerObject>();
		shortestTimer.add(timer.get(0));
		for(int i = 0; i < timer.size(); i++) {
			if(timer.get(i).timerLength() < shortestTimer.get(0).timerLength()) {
				shortestTimer.clear();
				shortestTimer.add(timer.get(i));
			} else if(timer.get(i).timerLength() == shortestTimer.get(0).timerLength())
				shortestTimer.add(timer.get(i));
				
		}
		for(int i = 0; i < shortestTimer.size(); i++) {
//			System.out.println(((CombatAction) shortestTimer.get(i)).key);
			shortestTimer.remove(shortestTimer.get(i));
		}
		return shortestTimer.toArray(new TimerObject[shortestTimer.size()]);
	}
	
}
