package com.autovend.devices.observers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;

/**
 * This is a sample class that demonstrates how to interact with a
 * BarcodeScanner, registering instances of {@link SampleBarcodeScannerObserver}
 * on it and checking that they have recorded the correct information.
 */
@SuppressWarnings("javadoc")
public class SampleBarcodeScannerDemo {
	public BarcodeScanner scanner;
	public BarcodedUnit item;
	public Barcode barcode;
	public SampleBarcodeScannerObserver listener1, listener2, listener3;

	/**
	 * Sets up the test suite. This is run before every test method.
	 */
	@Before
	public void setup() {
		scanner = new BarcodeScanner();
		barcode = new Barcode(new Numeral[] { Numeral.one, Numeral.two, Numeral.three, Numeral.four }); // 1234
		item = new BarcodedUnit(barcode, 10); // The weight doesn't matter for this demo.

		// Create 3 listeners ... so you can see which ones receive events and which
		// don't.
		listener1 = new SampleBarcodeScannerObserver("listener1");
		listener2 = new SampleBarcodeScannerObserver("listener2");
		listener3 = new SampleBarcodeScannerObserver("listener3");

		// Initialize the fields inside the listeners. Having these fields public would
		// be a bad idea in real code, but this is just a demo.
		listener1.device = null;
		listener1.barcode = null;
		listener2.device = null;
		listener2.barcode = null;
		listener3.device = null;
		listener3.barcode = null;

		// We'll register the first and second listeners, but not the third for now.
		scanner.register(listener1);
		scanner.register(listener2);
		
		// Explicitly enable the scanner so that the listeners find out the device on which they are registered.
		// Usually this isn't necessary for normal use, but this simplified the demo.
		scanner.disable();
		scanner.enable();
	}

	@After
	public void teardown() {
		scanner = null;
		barcode = null;
		item = null;
		listener1 = null;
		listener2 = null;
		listener3 = null;
	}

	/**
	 * Turns on the scanner and scans an item. It is expected that the two
	 * registered listeners will record the information from each event, but the
	 * unregistered one will not. Tries a bunch of other stuff too, which a proper
	 * test case really should not.
	 */
	@Test
	public void demoScan() {
		// The registered listeners should have recorded the information from the
		// "turnOn" event.
		assertEquals(scanner, listener1.device);
		assertEquals(scanner, listener2.device);
		assertEquals(null, listener3.device); // Not registered, so it shouldn't receive the event.

		// Nothing scanned yet.
		assertEquals(null, listener1.barcode);
		assertEquals(null, listener2.barcode);
		assertEquals(null, listener3.barcode);

		// Clear the recorded information.
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener1.barcode = null;
		listener2.barcode = null;
		listener3.barcode = null;

		// Deregister the second listener; register the third listener.
		scanner.deregister(listener2);
		scanner.register(listener3);

		// Now we scan an item. We may have to keep trying, as the scanner can be a
		// problem.
		boolean found = false;
		while(!found)
			found = scanner.scan(item);

		// The registered listeners should have recorded the information from the
		// "barcodeScanned" event.
		assertEquals(scanner, listener1.device);
		assertEquals(null, listener2.device); // Not registered, so it shouldn't receive the event.
		assertEquals(scanner, listener3.device);

		assertEquals(barcode, listener1.barcode);
		assertEquals(null, listener2.barcode); // Not registered, so it shouldn't receive the event.
		assertEquals(barcode, listener3.barcode);

		// Clear the recorded information.
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener1.barcode = null;
		listener2.barcode = null;
		listener3.barcode = null;

		// Disable scanner
		scanner.disable();

		found = false;

		// Check that scan cannot work when scanner is disabled
		try {
			scanner.scan(null);
		}
		catch(DisabledException e) {
			found = true;
		}

		assertTrue(found);

		// Re-enable scanner.
		scanner.enable();

		assertFalse(scanner.isDisabled());
	}
}
