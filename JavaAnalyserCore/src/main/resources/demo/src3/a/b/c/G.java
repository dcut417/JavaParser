package a.b.c;

import a.b.F;

public class G {
	public void g(F anF) { // As F is in a different source directory, it may not be available to the JavaParserTypeSolver so what happens then?
		anF.f();
	}
}
