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

public class BillStorageTest {
	public BillStorage storage;
	public int testCapacity = 100;
	public BillStorageTestObserver l1, l2, l3;
	

	

	@Before
	public void setUp() throws Exception {
		storage = new BillStorage(testCapacity);
		l1 = new BillStorageTestObserver("Listener1");
		l2 = new BillStorageTestObserver("Listener2");
		l3 = new BillStorageTestObserver("Listener3");


		storage.register(l1);
		storage.register(l2);
		
		
		storage.disable();
		storage.enable();
		
	}

	
	
	
	
	@After
	public void tearDown() throws Exception {
		storage = null;
		l1 = null;
		l2 = null;
		l3 = null;
	}

	
	
	@Test
	public void testConstructor() {
		System.out.println("Testing Constructor");
		//TODO: REGISTER L3 TO A BILLSTORAGE WITH CAPACITY OF 0 THAT IS LOCAL TO THIS METHOD
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		assertThrows(SimulationException.class, () -> new BillStorage(0));
		assertTrue(new BillStorage(testCapacity) instanceof BillStorage);
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	@Test
	public void testGetCapacity() {
		System.out.println("Testing getCapacity");
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		assertEquals(storage.getCapacity(), testCapacity);
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	@Test
	public void testBillCount() throws SimulationException, OverloadException {
		System.out.println("Testing getBillCount");
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		storage.unload();
		assertEquals(storage.getBillCount(), 0);
		
		Bill[] bills = new Bill[storage.getCapacity() / 2 ];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		Bill[] loadableBills = bills;
		
		storage.load(loadableBills);
		assertEquals(storage.getBillCount(), storage.getCapacity() / 2);
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		System.out.println();
	}
	
	
	
	@Test
	public void testLoad() throws SimulationException, OverloadException {
		System.out.println("Testing load");
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		
		// try to load 'null' bills
		assertThrows(SimulationException.class, () -> storage.load(null));
		
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		// try to load a number of bills greater than capacity
		Bill[] bills = new Bill[storage.getBillCount() + (storage.getCapacity() - storage.getBillCount()) + 1];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		Bill[] greater_than_capacity = bills;
		
		assertThrows(OverloadException.class, () -> storage.load(greater_than_capacity));
		
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		// try to load a number of bill where one bill is unacceptable (null)
		bills[bills.length / 2] = null;
		bills = new Bill[storage.getCapacity() - storage.getBillCount()];
		for (int i = 0; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		
		bills[bills.length / 2] = null;
		Bill[] bills_with_one_null = bills;
		assertThrows(SimulationException.class, () -> storage.load(bills_with_one_null));
		
		assertEquals(storage, l1.getDevice());
		assertEquals(storage, l2.getDevice());
		assertEquals(null, l3.getDevice());
		
		l1.setDevice(null);
		l2.setDevice(null);
		l3.setDevice(null);
		
		
		int originalCount = storage.getBillCount();
		
		bills = new Bill[storage.getCapacity() - originalCount];
		for (int i = 0 ; i < bills.length; ++i) {
			bills[i] = new Bill(5, Currency.getInstance(Locale.CANADA));
		}
		Bill[] valid_bills = bills;
		
		storage.load(valid_bills);
		List<Bill> unloaded_bills = storage.unload();
		
		/*
		Bill[] unloadedArr = new Bill[unloaded_bills.size()];
		for (int i = 0; i < unloadedArr.length; ++i) {
			unloadedArr[i] = unloaded_bills.get(i);
		}*/
		
		for (int i = originalCount; i < storage.getCapacity(); ++i) {
			assertEquals(valid_bills[i], unloaded_bills.get(i));
		}
		
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
