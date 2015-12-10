package pl.mbogusz3.invaders.controller;

import pl.mbogusz3.invaders.controller.eventHandlers.*;
import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEvent;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersExitException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class InvadersController {
	private final LinkedBlockingQueue<InvadersEvent> eventQueue;
	private final InvadersModel model;
	private final HashMap<String, InvadersEventHandler> eventHandlerMap;

	private final static int tickRate = 100;
	private long previousTime;
	private long nextTime;
	private long newTime;
	private double frameTime;

	public InvadersController(InvadersModel model) {
		this.eventQueue = new LinkedBlockingQueue<InvadersEvent>();
		this.model = model;
		this.eventHandlerMap = new HashMap<String, InvadersEventHandler>();
		this.fillEventHandlerMap();
	}

	public void putEvent(InvadersEvent event) {
		try {
			eventQueue.put(event);
		} catch(InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	public void start() {
		InvadersEvent event;
		InvadersEventHandler eventHandler;

		System.out.println("Controller: starting timer");
		previousTime = System.nanoTime();
		nextTime = previousTime + 1000000000 / tickRate;
		startTimer(1000/tickRate);

		System.out.println("Controller: starting main event loop");
		while (true) {
			try {
				event = eventQueue.take();
			} catch(Exception e) {
				System.out.print("Unexpected exception on event taking: " + e);
				continue;
			}

			try {
				eventHandler = this.eventHandlerMap.get(event.type);
				eventHandler.execute(event.payload);
			} catch(NullPointerException e) {
				System.out.println("Unknown event!");
			} catch(InvadersExitException e) {
				System.out.println("Caught exit exception, ending!");
				break;
			} catch(Exception e) {
				System.out.println("Unexpected exception on event handling: " + e);
			}
		}

		System.exit(0);
	}

	private void startTimer(long delay) {
		Timer tickRateTimer = new Timer();
		tickRateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				newTime = System.nanoTime();
				frameTime = (double) (newTime - previousTime) / 1000000000.0;
				previousTime = newTime;
				long nextDelay = 1000 / tickRate + (previousTime >= nextTime ? 0 : 1);
				nextTime = previousTime + 1000000000 / tickRate;

				synchronized (model) {
					model.recalculate(frameTime);
					model.notifyView();
				}

				tickRateTimer.cancel();
				startTimer(nextDelay);
			}
		}, delay);
	}

	private void fillEventHandlerMap() {
		this.eventHandlerMap.put("newGame", new OnNewGameEvent(this.model));
		this.eventHandlerMap.put("exitGame", new OnExitEvent(this.model));
		this.eventHandlerMap.put("keyDown", new OnKeyDownEvent(this.model));
		this.eventHandlerMap.put("keyUp", new OnKeyUpEvent(this.model));
		this.eventHandlerMap.put("timer", new OnViewTimerEvent(this.model));
	}
}