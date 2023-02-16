package com.autovend.hardware.test;

import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillValidator;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillValidatorObserver;

public class BillValidatorObserverTest implements BillValidatorObserver {
	/**
	 * Here, we will record the device on which an event occurs.
	 */
	public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	/**
	 * Here, we will record the bill type and value that has been validated.
	 */
	public Currency money;
	public int value;
	/**
	 * This is the name of this listener.
	 */
	public String name;

	/**
	 * Basic constructor.
	 * 
	 * @param name
	 *            The name to use for this listener.
	 */
	public BillValidatorObserverTest(String name) {
		this.name = name;
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
	}

	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		this.device = validator;
		this.money = currency;
		this.value = value;
		System.out.println(name + ": The detected bill has been validated of value: " + value + " " + money.getCurrencyCode() );
	}

	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		this.device = validator;
		System.out.println(name + ": The bill detected was not valid.");
	}

}
