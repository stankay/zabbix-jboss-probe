package net.stankay.zabbix.jboss;

import java.io.IOException;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

public class JBossCLIClient {

	private ModelControllerClient client;
	private CommandContext ctx;
	private String username;
	private String password;

	public JBossCLIClient(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public ModelNode sendCommand(String command) throws CommandLineException, IOException {
		
		ctx = CommandContextFactory.getInstance().newCommandContext(username, password.toCharArray());
        ctx.connectController("localhost");
        client = ctx.getModelControllerClient();

		ModelNode op = ctx.buildRequest(command);
		ModelNode result = client.execute(op);
		
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				System.err.println("Error when closing client" + e);
			}
		}
		
		return result;
	}
}
