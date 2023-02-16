package com.autovend.hardware.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.SelfCheckoutStation;

public class BillValidatorTest {
	public BillValidator validator;
	public Bill five, ten, twenty, fifty, onehundred;
	public BillValidatorObserverTest listener1, listener2, listener3, listener4, listener5;
	public Currency cad;
	public SelfCheckoutStation checkout;
	
	@Before
	public void setup() {
		cad = Currency.getInstance(Locale.CANADA);
		five = new Bill(5, cad);
		ten = new Bill(10, cad);
		twenty = new Bill(20, cad);
		fifty = new Bill(50, cad);
		onehundred = new Bill(100, cad);
		int[] demonination = {5,10,25,50,100};
		validator = new BillValidator(cad, demonination);
		listener1 = new BillValidatorObserverTest("listener1");
		listener2 = new BillValidatorObserverTest("listener2");
		listener3 = new BillValidatorObserverTest("listener3");
		listener4 = new BillValidatorObserverTest("listener4");
		listener5 = new BillValidatorObserverTest("listener5");
		
		listener1.device = null;
		listener1.money = null;
		listener1.value = 0;
		listener2.device = null;
		listener2.money = null;
		listener2.value = 0;
		listener3.device = null;
		listener3.money = null;
		listener3.value = 0;
		listener4.device = null;
		listener4.money = null;
		listener4.value = 0;
		listener5.device = null;
		listener5.money = null;
		listener5.value = 0;
		
		validator.register(listener1);
		
		validator.disable();
		validator.enable();
	}
	@After
	public void teardown() {
		five = null;
		ten = null;
		twenty = null;
		fifty = null;
		onehundred = null;
		listener1 = null;
		listener2 = null;
		listener3 = null;
		listener4 = null;
		listener5 = null;
	}
	
	@Test
	public void plainvalidator() {
		try {
		assertEquals(validator, listener1.device);
		assertEquals(null, listener2.device);
		assertEquals(null, listener3.device);
		assertEquals(null, listener4.device);
		assertEquals(null, listener5.device);
		
		assertEquals(null, listener1.money);
		assertEquals(null, listener2.money);
		assertEquals(null, listener3.money);
		assertEquals(null, listener4.money);
		assertEquals(null, listener5.money);
		
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener4.device = null;
		listener5.device = null;
		listener1.money = null;
		listener2.money = null;
		listener3.money = null;
		listener4.money = null;
		listener5.money = null;
		
		validator.deregister(listener1);
		validator.register(listener2);
		validator.register(listener3);
		validator.register(listener4);
		validator.register(listener5);
		
		boolean found = false;
		while(!found)
			found = validator.accept(five);
		}
		catch (Exception e){
			System.out.println("FAILURE: Sink and Source are not connected.");
			try {
				validator.connect(null, null);
			}
			catch (Exception ex){
				fail();
			}
		}
	}
	@Test
	public void propersetup() {
		machinemake();
		assertEquals(validator, listener1.device);
		assertEquals(null, listener2.device);
		assertEquals(null, listener3.device);
		assertEquals(null, listener4.device);
		assertEquals(null, listener5.device);
		
		assertEquals(null, listener1.money);
		assertEquals(null, listener2.money);
		assertEquals(null, listener3.money);
		assertEquals(null, listener4.money);
		assertEquals(null, listener5.money);
		
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener4.device = null;
		listener5.device = null;
		listener1.money = null;
		listener2.money = null;
		listener3.money = null;
		listener4.money = null;
		listener5.money = null;
		
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.register(listener3);
		checkout.billValidator.register(listener5);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(onehundred);
		
		assertEquals(null, listener1.device);
		assertEquals(checkout.billValidator, listener2.device);
		assertEquals(checkout.billValidator, listener3.device);
		assertEquals(null, listener4.device);
		assertEquals(checkout.billValidator, listener5.device);
		
		assertEquals(null, listener1.money);
		assertEquals(onehundred.getCurrency(), listener2.money);
		assertEquals(onehundred.getCurrency(), listener3.money);
		assertEquals(null, listener4.money);
		assertEquals(onehundred.getCurrency(), listener5.money);
		
		assertEquals(null, listener1.money);
		assertEquals(onehundred.getValue(), listener2.value);
		assertEquals(onehundred.getValue(), listener3.value);
		assertEquals(null, listener4.money);
		assertEquals(onehundred.getValue(), listener5.value);
		
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener4.device = null;
		listener5.device = null;
		listener1.money = null;
		listener2.money = null;
		listener3.money = null;
		listener4.money = null;
		listener5.money = null;
		
		checkout.billValidator.disable();
		found = false;

		try {
			checkout.billValidator.accept(null);
		}
		catch(DisabledException e) {
			found = true;
		}

		assertTrue(found);

		checkout.billValidator.enable();

		assertFalse(checkout.billValidator.isDisabled());
		assertTrue(checkout.billValidator.hasSpace());
	}
	@Test
	public void billvalidate() {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(five);
		assertEquals(null, listener1.device);
		assertEquals(checkout.billValidator, listener2.device);
		assertEquals(null, listener3.device);
		assertEquals(null, listener4.device);
		assertEquals(null, listener5.device);
		
		checkout.billValidator.register(listener1);
		checkout.billValidator.deregister(listener2);
		found = false;
		while(!found)
		found = checkout.billValidator.accept(ten);
		
		checkout.billValidator.register(listener3);
		checkout.billValidator.deregister(listener1);
		found = false;
		while(!found)
		found = checkout.billValidator.accept(twenty);
		
		checkout.billValidator.register(listener4);
		checkout.billValidator.deregister(listener3);
		found = false;
		while(!found)
		found = checkout.billValidator.accept(fifty);
		
		checkout.billValidator.register(listener5);
		checkout.billValidator.deregister(listener4);
		found = false;
		while(!found)
		found = checkout.billValidator.accept(onehundred);
		
		assertEquals(checkout.billValidator, listener1.device);
		assertEquals(checkout.billValidator, listener2.device);
		assertEquals(checkout.billValidator, listener3.device);
		assertEquals(checkout.billValidator, listener4.device);
		assertEquals(checkout.billValidator, listener5.device);
		
		assertEquals(ten.getCurrency(), listener1.money);
		assertEquals(five.getCurrency(), listener2.money);
		assertEquals(twenty.getCurrency(), listener3.money);
		assertEquals(fifty.getCurrency(), listener4.money);
		assertEquals(onehundred.getCurrency(), listener5.money);
		
		assertEquals(ten.getValue(), listener1.value);
		assertEquals(five.getValue(), listener2.value);
		assertEquals(twenty.getValue(), listener3.value);
		assertEquals(fifty.getValue(), listener4.value);
		assertEquals(onehundred.getValue(), listener5.value);
		
		listener1.device = null;
		listener2.device = null;
		listener3.device = null;
		listener4.device = null;
		listener5.device = null;
		listener1.money = null;
		listener2.money = null;
		listener3.money = null;
		listener4.money = null;
		listener5.money = null;
		
		checkout.billValidator.disable();
		found = false;
	}
	@Test
	public void nullbill() {
		try {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(null);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Null Bill.");
			assertEquals(null, listener1.device);
			assertEquals(checkout.billValidator, listener2.device);
			assertEquals(null, listener3.device);
			assertEquals(null, listener4.device);
			assertEquals(null, listener5.device);
			
			assertEquals(null, listener1.money);
			assertEquals(null, listener2.money);
			assertEquals(null, listener3.money);
			assertEquals(null, listener4.money);
			assertEquals(null, listener5.money);
			
			assertEquals(0, listener1.value);
			assertEquals(0, listener2.value);
			assertEquals(0, listener3.value);
			assertEquals(0, listener4.value);
			assertEquals(0, listener5.value);
			
			listener1.device = null;
			listener2.device = null;
			listener3.device = null;
			listener4.device = null;
			listener5.device = null;
			listener1.money = null;
			listener2.money = null;
			listener3.money = null;
			listener4.money = null;
			listener5.money = null;
			
			checkout.billValidator.disable();
		}
	}
	@Test
	public void disabledtest() {
		try {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(five);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Validator is disabled.");
			assertEquals(null, listener1.device);
			assertEquals(checkout.billValidator, listener2.device);
			assertEquals(null, listener3.device);
			assertEquals(null, listener4.device);
			assertEquals(null, listener5.device);
			
			assertEquals(null, listener1.money);
			assertEquals(null, listener2.money);
			assertEquals(null, listener3.money);
			assertEquals(null, listener4.money);
			assertEquals(null, listener5.money);
			
			assertEquals(0, listener1.value);
			assertEquals(0, listener2.value);
			assertEquals(0, listener3.value);
			assertEquals(0, listener4.value);
			assertEquals(0, listener5.value);
			
			listener1.device = null;
			listener2.device = null;
			listener3.device = null;
			listener4.device = null;
			listener5.device = null;
			listener1.money = null;
			listener2.money = null;
			listener3.money = null;
			listener4.money = null;
			listener5.money = null;
			
			checkout.billValidator.disable();
		}
	}
	@Test
	public void foreignbill() {
		try {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		Bill faker = new Bill(100, Currency.getInstance(Locale.JAPAN));
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(faker);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Overload.");
			fail();
		}
	}
	@Test
	public void fakebill() {
		try {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		Bill faker = new Bill(25, Currency.getInstance(Locale.CANADA));
		boolean found = false;
		while(!found)
		found = checkout.billValidator.accept(faker);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Overload.");
			fail();
		}
	}
	@Test
	public void badvalidator() {
		try {
		BillValidator validator = new BillValidator(null, null);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Bad setup.");
		}
	}
	@Test
	public void badvalidator2() {
		try {
		BillValidator validator = new BillValidator(cad, null);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Bad setup.");
		}
	}
	@Test
	public void badvalidator3() {
		try {
		int[] denomination = {-1};
		BillValidator validator = new BillValidator(cad, denomination);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Bad setup.");
		}
	}
	@Test
	public void badvalidator4() {
		try {
		int[] denomination = {1,1};
		BillValidator validator = new BillValidator(cad, denomination);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Bad setup.");
		}
	}
	@Test
	public void badvalidator5() {
		try {
		int[] denomination = {};
		BillValidator validator = new BillValidator(cad, denomination);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Bad setup.");
		}
	}
	@Test
	public void fullsink() {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		boolean found = false;
		
		int n = 999;
		try {
		while (n > 0){
			checkout.billStorage.accept(five);
			n--;
		}
		while(!found)
		found = checkout.billValidator.accept(five);
		}
		catch (Exception e) {
			System.out.println("FAILURE: Not enough space.");
			fail();
		}
	}
	@Test
	public void rngfail() {
		machinemake();
		listener1.device = null;
		validator.deregister(listener1);
		checkout.billValidator.register(listener2);
		checkout.billValidator.disable();
		checkout.billValidator.enable();
		boolean found = false;
		int n = 999;
		try {
		while (n > 0){
			found = checkout.billValidator.accept(five);
			n--;
		}
		}
		catch (Exception e) {
			System.out.println("FAILURE: Random failure.");
		}
	}
	public void machinemake() {
		int[] denomination = {5,10,20,50,100};
		BigDecimal[] koin = new BigDecimal[7];
		koin[0] = new BigDecimal("0.01");
		koin[1] = new BigDecimal("0.05");
		koin[2] = new BigDecimal("0.10");
		koin[3] = new BigDecimal("0.25");
		koin[4] = new BigDecimal("0.50");
		koin[5] = new BigDecimal("1.00");
		koin[6] = new BigDecimal("2.00");
		checkout = new SelfCheckoutStation(cad, denomination, koin, 1, 1);
	}
}
