package org.cosmodict;

import java.util.Properties;
import java.util.Set;

public class Test {

	public Test() {
	}

	public static void main(String[] args) {
		Properties props = System.getProperties();
		Set<String> keys = props.stringPropertyNames();
		for (String k : keys) {
			System.out.println(k + "=" + props.getProperty(k));
		}
	}

}
