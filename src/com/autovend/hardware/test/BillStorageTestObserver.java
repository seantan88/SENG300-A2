package com.autovend.hardware.test_01rc2;
import com.autovend.BarcodedUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.BillStorage;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillStorageObserver;

public class BillStorageTestObserver implements BillStorageObserver {
	
	private String name;
	private AbstractDevice<? extends AbstractDeviceObserver> device;
	
	
	
	
	
	
	public BillStorageTestObserver(String name) {
		this.name = name;
		this.device = null;
	}
	
	public String getName() {
		return new String(this.name);
	}
	
	
	public AbstractDevice<? extends AbstractDeviceObserver> getDevice() {
		return this.device;
	}
	
	public void setDevice(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
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
	public void reactToBillsFullEvent(BillStorage unit) {
		this.device = unit;
		int billCount = unit.getBillCount();
		int capacity = unit.getCapacity();
		String msg = String.format("%s device capacity full: %d capacity, %d attempted to load", this.name, capacity, billCount);
		System.out.println(msg);
		
		
	}

	@Override
	public void reactToBillAddedEvent(BillStorage unit) {
		this.device = unit;
		int billCount = unit.getBillCount();
		String msg = String.format("%s device has added bills through pay slot, current bill count: %d", this.name, billCount);
		System.out.println(msg);
		
	
	}

	
	@Override
	public void reactToBillsLoadedEvent(BillStorage unit) {
		this.device = unit;
		int billCount = unit.getBillCount();
		String msg = String.format("%s device has loaded bills directly, current bill count: %d", this.name, billCount);
		System.out.println(msg);
		
	}

	@Override
	public void reactToBillsUnloadedEvent(BillStorage unit) {
		this.device = unit;
		int billCount = unit.getBillCount();
		String msg = String.format("%s device has unloaded bills directly, current bill count: %d", this.name, billCount);
		System.out.println(msg);
	}

	
	
	
}
