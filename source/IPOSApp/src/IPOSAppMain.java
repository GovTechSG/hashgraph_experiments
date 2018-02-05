import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.UriBuilder;

import com.swirlds.platform.Browser;
import com.swirlds.platform.Console;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.SwirldState;
import com.txmq.exo.config.ExoConfig;
import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.messaging.rest.CORSFilter;
import com.txmq.exo.messaging.socket.TransactionServer;
import com.txmq.exo.persistence.BlockLogger;
import com.txmq.exo.persistence.couchdb.CouchDBBlockLogger;
import com.txmq.exo.transactionrouter.ExoTransactionRouter;
import com.txmq.socketdemo.SocketDemoState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


public class IPOSAppMain implements SwirldMain {
	public Platform platform;
	public int selfId;
	public final int sleepPeriod = 100;

	public static void main(String[] args) {
		Browser.main(null);
	}

	@Override
	public void preEvent() {
	}

	@Override
	public void init(Platform platform, int id) {
		this.platform = platform;
		this.selfId = id;
		//this.console = platform.createConsole(true); // create the window, make it visible
		platform.setAbout("Hello Swirld v. 1.0\n"); // set the browser's "about" box
		platform.setSleepAfterSync(sleepPeriod);

		ExoPlatformLocator.initFromConfig(platform);
		
		/*
		//Initialize the platform locator, so Exo code can get a reference to the platform when needed.
		String[] transactionProcessorPackages = {"com.txmq.exo.messaging.rest", "com.txmq.socketdemo.transactions"};
		CouchDBBlockLogger blockLogger = new CouchDBBlockLogger(
				"zoo-" + platform.getAddress().getSelfName().toLowerCase(),
				"http",
				"couchdb",
				//"localhost",
				5984);
		ExoPlatformLocator.init(platform, SocketDemoTransactionTypes.class, transactionProcessorPackages, blockLogger);
		
		//Initialize REST endpoints exposed by this Hashgraph
		ExoPlatformLocator.initREST(
			platform.getState().getAddressBookCopy().getAddress(selfId).getPortExternalIpv4() + 2000, 
			new String[] {"com.txmq.socketdemo.rest"}
		);
		
		//Initialize socket server
		ExoPlatformLocator.initSecuredSocketMessaging(
			platform.getState().getAddressBookCopy().getAddress(selfId).getPortExternalIpv4() + 1000,
			new String[] {"com.txmq.socketdemo.socket"},
			"client.public",
			"client",
			"server.private",
			"server"			
		);
		 */

		//Initialize REST endpoints exposed by this Hashgraph
//		ExoPlatformLocator.initREST(
//				platform.getState().getAddressBookCopy().getAddress(selfId).getPortExternalIpv4() + 2000,
//				new String[] {"rest"}
//		);
	}

	@Override
	public void run() {
		
		while (true) {
			try {
				Thread.sleep(sleepPeriod);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public SwirldState newState() {
		return new SocketDemoState();
	}
}