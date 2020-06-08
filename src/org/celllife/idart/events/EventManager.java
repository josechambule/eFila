package org.celllife.idart.events;

import org.celllife.idart.integration.eKapa.EkapaEventListener;

import java.util.HashSet;
import java.util.Set;



public class EventManager {

	private final Set<Object> participants = new HashSet<Object>();

	public void register() {
		participants.add(new EkapaEventListener());
		
		for (Object part : participants) {
			
		}
	}

	public void deRegister() {
	}
	
	

}
