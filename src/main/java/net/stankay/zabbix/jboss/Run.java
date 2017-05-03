package net.stankay.zabbix.jboss;

import java.io.IOException;

import org.jboss.as.cli.CommandLineException;
import org.jboss.dmr.ModelNode;

public class Run {
	/**
	 * Connect to local JBoss and execute given command
	 */
	public static void main(String[] args) {
		String mode = "unknown";
		if (args.length == 3) {
			if (args[0].equals("-heap")) mode = "heap";
			if (args[0].equals("-nonheap")) mode = "nonheap";
		} else {
			System.err.println("Usage: Run [-heap|-nonheap] [username] [password]");
			System.out.print("-1");
			return;
		}
		
		JBossCLIClient cl = new JBossCLIClient(args[1], args[2]);

		if (mode.equals("unknown")) {
			System.err.println("Usage: Run [-heap|-nonheap] [username] [password]");
			System.out.print("-1");
			return;
		}
		
		ModelNode returnVal;
		try {
			returnVal = cl.sendCommand("/core-service=platform-mbean/type=memory:read-resource(include-runtime=true)");
			if (mode.equals("heap")) {
				System.out.print(percentage(returnVal.get("result").get("heap-memory-usage")));
				
			//non heap does not make sense for Wildfly
			} else if (mode.equals("nonheap")) {
				System.out.print(percentage(returnVal.get("result").get("non-heap-memory-usage")));
			} else {
				
			}
		} catch (CommandLineException | IOException e) {
			System.out.print("-1");
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public static String percentage(ModelNode node) {
		return String.format("%.0f", 100*node.get("used").asDouble()/node.get("max").asDouble());
	}
}
