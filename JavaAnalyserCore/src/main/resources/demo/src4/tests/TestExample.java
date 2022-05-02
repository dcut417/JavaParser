package tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import a.b.c.D;
import a.b.c.E;

public class TestExample {

	@Test
	public void test() {
		D aD = new D();
		aD.d(new E());
		assertNotEquals(null, aD);
		fail("failed");
	}

}
