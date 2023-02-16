package com.autovend.devices.observers;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;

/**
 * This is a sample of how one could create a class to listen for events
 * emanating from a barcode scanner. This sample only reacts to "turnedOn",
 * "turnedOff", and "barcodeScanned" events. You can see this class in action by
 * running the test suite in {@link SampleBarcodeScannerDemo}.
 */
public class SampleBarcodeScannerObserver implements BarcodeScannerObserver {
	/**
	 * Here, we will record the device on which an event occurs.
	 */
	public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	/**
	 * Here, we will record the barcode that has been scanned.
	 */
	public Barcode barcode = null;
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
	public SampleBarcodeScannerObserver(String name) {
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
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		this.device = barcodeScanner;
		this.barcode = barcode;
		System.out.println(name + ": A barcode has been scanned: " + barcode.toString());
	}
}
