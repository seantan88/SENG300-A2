/*
 * Desmond O'Brien - 30064340
 * Sean Tan - 30094560
 * Victor Campos - 30106934
 * Imran Haji - 30141571
 */




package com.autovend.hardware.test_01rc2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertThrows;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;




import com.autovend.devices.BillStorage;
import com.autovend.devices.DisabledException;
import com.autovend.Bill;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.BillStorageObserver;
import com.autovend.devices.OverloadException;

/*
 * Tester class for BillStorage class
 */
public class BillStorageTest {
	
	// member BillStorage, capacity for testing, and observers 
	public BillStorage storage;
	public final int testCapacity = 100;
	public BillStorageTestObserver l1, l2, l3;
	

	
	// Setup function
	@Before
	public void setUp() throws Exception {
		storage = new BillStorage(testCapacity);
		l1 = new BillStorageTestObserver("Listener1");
		l2 = new BillStorageTestObserver("Listener2");
		l3 = new BillStorageTestObserver("Listener3");

		// Register L1 and L2 to BillStorage object
		storage.register(l1);
		storage.register(l2);
		
		// Enable and disable BillStorage for simulation
		storage.disable();
		storage.enable();
	}

	// Function to clear up references when testing is done
	@After
	public void tearDown() throws Exception {
		storage = null;
		l1 = null;
		l2 = null;
		l3 = null;
	}

	
	/*
	 * Function to test the BillStorage constructor
	 * 
	 */
	@Test
	public void testConstructor() {
		// message for output formatting
		System.out.println("Testing Constructor");
		// Make sure observers are correct
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		// assure that a SimulationException is thrown if a BillStorage <= 0 is instantiated
		assertThrows(SimulationException.class, () -> new BillStorage(0));
		// assure that a new BillStorage with a valid capacity is instantiated correctly
		assertTrue(new BillStorage(testCapacity) instanceof BillStorage);
		
		// Clear up listener's devices and print line for output formatting
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	
	/*
	 * 
	 * Function to test the getCapacity method
	 * 
	 */
	@Test
	public void testGetCapacity() {
		// formatting
		System.out.println("Testing getCapacity");
		
		// checking observers
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		// assure that capacity of member variable is correctly returneds
		assertEquals(storage.getCapacity(), testCapacity);
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	
	/*
	 * 
	 * Function to test billCount method
	 * 
	 */
	@Test
	public void testBillCount() throws SimulationException, OverloadException {
		System.out.println("Testing getBillCount");
		
		// check observers
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		// make sure storage is empty
		storage.unload();
		
		// ensure that 0 is returned if storage is empty
		assertEquals(storage.getBillCount(), 0);
		
		// create am array of bills half the length of max capacity
		Bill[] bills = new Bill[storage.getCapacity() / 2 ];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		// immutable reference of same array for testing
		Bill[] loadableBills = bills;
		
		// load the bills and assure length is equal to half max capacity
		storage.load(loadableBills);
		assertEquals(storage.getBillCount(), storage.getCapacity() / 2);
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	
	/*
	 * Function to test load method
	 * 
	 */
	@Test
	public void testLoad() throws SimulationException, OverloadException {
		System.out.println("Testing load");
		
		// check observers
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		//unload all bills
		storage.unload();
		
		
		// assure that an exception is thrown if we try to load no bills
		assertThrows(SimulationException.class, () -> storage.load(null));
		

		
		// Create an array of bills greater than max capacity
		Bill[] bills = new Bill[storage.getBillCount() + (storage.getCapacity() - storage.getBillCount()) + 1];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		
		// set reference to immutable as reference to array above
		Bill[] greater_than_capacity = bills;
		
		// Assure that an exception is thrown if we try to load more bills than max capacity
		assertThrows(OverloadException.class, () -> storage.load(greater_than_capacity));
		
	
		// create another array of bills, this time excatly enough bills to fill rest of storage
		bills[bills.length / 2] = null;
		bills = new Bill[storage.getCapacity() - storage.getBillCount()];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		// set the middle bill to null
		bills[bills.length / 2] = null;
		
		//array reference for testing
		Bill[] bills_with_one_null = bills;
		// assure that an exception is thrown if a single bill is null / unrecognizable
		assertThrows(SimulationException.class, () -> storage.load(bills_with_one_null));
		
		
		bills = new Bill[storage.getCapacity() / 2];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		Bill[] firstHalf = bills;
		
		bills = new Bill[storage.getCapacity() / 2];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(10, Currency.getInstance(Locale.CANADA));
		}
		Bill[] secondHalf = bills;
		
		
		List<Bill> expectedStorage = Arrays.asList(firstHalf);
		for (Bill bill : secondHalf) {
			expectedStorage.add(bill);
		}
		
		storage.unload();
		storage.load(firstHalf);
		storage.load(secondHalf);
		
		List<Bill> unloaded_bills = storage.unload();
		
		for (int i = 0; i < unloaded_bills.size(); ++i) {
			assertEquals(expectedStorage.get(i), unloaded_bills.get(i));
		}
		
		/*
		Bill[] unloadedArr = new Bill[unloaded_bills.size()];
		for (int i = 0; i < unloadedArr.length; ++i) {
			unloadedArr[i] = unloaded_bills.get(i);
		}*/
		
		//for (int i = originalCount; i < storage.getCapacity(); ++i) {
	//		assertEquals(valid_bills[i], unloaded_bills.get(i));
	//	}
		
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	@Test
	public void testUnload() {
		System.out.println("Testing Unload");
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		

		
		List<Bill> empty = storage.unload();
		assertEquals(storage.getBillCount(), 0);
		
		Bill[] emptyArr = new Bill[empty.size()];
		
		for (int i = 0; i< emptyArr.length; ++i) {
			emptyArr[i] = empty.get(i);
		}
		
		assertArrayEquals(emptyArr, new Bill[storage.getCapacity()]);
		
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	
	}
	
	@Test
	public void testAccept() throws SimulationException, OverloadException {
		System.out.println("Testing accept");
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		Bill goodBill = new Bill(5, Currency.getInstance(Locale.CANADA));
		Bill badBill = null;
		
		storage.disable();
		assertThrows(DisabledException.class, () -> storage.accept(goodBill));
		
		storage.enable();
		
		assertThrows(SimulationException.class, () -> storage.accept(badBill));
		
		Bill[] bills = new Bill[storage.getCapacity()];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		Bill[] loadableBills = bills;
		storage.unload();
		storage.load(loadableBills);
		

		
		assertThrows(OverloadException.class, () -> storage.accept(goodBill));
		
		
		storage.unload();
		storage.accept(goodBill);
		storage.unload();
		
		
		boolean found = true;
		
		
		bills = new Bill[storage.getCapacity() - 1];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		Bill[] loadableBillsOneLess = bills;
		storage.load(loadableBillsOneLess);
		
		
		try {
			storage.accept(goodBill);
		}
		catch (IndexOutOfBoundsException e) {
			found = false;
		}
		
		assertTrue(found);
		storage.unload();
		
		
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		
		System.out.println();
		
	
	}
	
	@Test
	public void testHasSpace() throws SimulationException, OverloadException {
		storage.unload();
		assertTrue(storage.hasSpace());
		
		Bill[] bills = new Bill[storage.getCapacity()];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
			
		Bill[] loadableBills = bills;
		storage.load(loadableBills);
		assertFalse(storage.hasSpace());
	}

	

}
