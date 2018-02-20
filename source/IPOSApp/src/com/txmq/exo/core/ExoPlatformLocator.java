package com.txmq.exo.core;

import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldState;
import com.txmq.exo.config.ExoConfig;
import com.txmq.exo.config.MessagingConfig;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.messaging.ExoTransactionType;
import com.txmq.exo.messaging.rest.CORSFilter;
import com.txmq.exo.persistence.BlockLogger;
import com.txmq.exo.persistence.IBlockLogger;
import com.txmq.exo.transactionrouter.ExoTransactionRouter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

//import com.txmq.exo.messaging.socket.TransactionServer;

/**
 * A static locator class for Exo platform constructs.  This class allows applications
 * to get access to Swirlds platform, Swirlds state, and the transaction router from
 * anywhere in the application.
 *
 * I'm not in love with using static methods essentially as global variables.  I'd
 * love to hear ideas on a better way to approach this.
 */
public class ExoPlatformLocator {
	/**
	 * Reference to the Swirlds platform
	 */
	private static Platform platform;

	/**
	 * Reference to Exo's transaction router.  An application should have only one
	 * router.  Developers should never need to create one.
	 */
	private static ExoTransactionRouter transactionRouter = new ExoTransactionRouter();
	
	/**
	 * Reference to the block logging manager
	 */
	private static BlockLogger blockLogger = new BlockLogger();

	/**
	 * Initializes the platform from an exo-config.json file located 
	 * in the same directory as the application runs in.
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void initFromConfig(Platform platform) {
		ExoPlatformLocator.platform = platform;
		String nodeName = platform.getAddress().getSelfName();
		ExoConfig config = ExoConfig.getConfig();
		
		//Initialize transaction types
		Class<? extends ExoTransactionType> transactionTypeClass;
		try {
			Class<?> ttClass = Class.forName(config.hashgraphConfig.transactionTypesClassName);
			transactionTypeClass = (Class<? extends ExoTransactionType>) ttClass;			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(
				"Error instantiating transaction types enumerator: " + config.hashgraphConfig.transactionTypesClassName
			);
		}
		
		init(platform, transactionTypeClass, config.hashgraphConfig.transactionProcessors);
		
		MessagingConfig messagingConfig = null;


		//Set up REST, if it's in the config..
		if (config.hashgraphConfig.rest != null) {
			try {
				messagingConfig = parseMessagingConfig(config.hashgraphConfig.rest);
				initREST(messagingConfig.port, messagingConfig.handlers);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Error configuring REST:  " + e.getMessage());
			}
		}
		
		//configure block logging, if indicated in the config file
		if (config.hashgraphConfig.blockLogger != null) {
			try {
				Class<? extends IBlockLogger> loggerClass =
					(Class<? extends IBlockLogger>) Class.forName(config.hashgraphConfig.blockLogger.loggerClass);
				IBlockLogger logger = loggerClass.newInstance();
				logger.configure(config.hashgraphConfig.blockLogger.parameters);
				blockLogger.setLogger(logger, nodeName);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException("Error configuring block logger:  " + e.getMessage());
			}			
		}				
	}
	
	private static MessagingConfig parseMessagingConfig(MessagingConfig config) {
		MessagingConfig result = new MessagingConfig();
		
		//If a port has been defined in the config, use it over the derived port.
		if (config.port > 0) {
			result.port = config.port;				
		} else {
			//Test if there's a derived port value.  If not, we have an invalid messaging config
			if (config.derivedPort != null) {
				//Calculate the port for socket connections based on the hashgraph's port
				result.port = platform.getAddress().getPortExternalIpv4() + config.derivedPort;
			} else {
				throw new IllegalArgumentException(
					"One of \"port\" or \"derivedPort\" must be defined."
				);
			}
		}
		
		if (config.handlers != null && config.handlers.length > 0) {
			result.handlers = config.handlers;
		} else {
			throw new IllegalArgumentException(
				"No handlers were defined in configuration"
			);
		}
		
		result.secured = config.secured;
		result.clientKeystore = config.clientKeystore;
		result.serverKeystore = config.serverKeystore;
		return result;
	}
	
	/**
	 * Initializes the platform from an exo-condig.json 
	 * file located using the path parameter.
	 * @param path
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void initFromConfig(Platform platform, String path) {
		ExoConfig.loadConfiguration(path);
		initFromConfig(platform);
	}
	
	/**
	 * Initialization method for the platform.  This should be called by your main's 
	 * init() or run() methods
	 */
	public static synchronized void init(Platform platform) {
		ExoPlatformLocator.platform = platform;
		
	}
	
	public static synchronized void init(	Platform platform, 
							Class<? extends ExoTransactionType> transactionTypeClass, 
							String[] transactionProcessorPackages) {
		init(platform);
		
		//Hokey, but we cause the transaction type class to initialize itself by simply instantiating one..
		try {
			transactionTypeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String tpp : transactionProcessorPackages) {
			transactionRouter.addPackage(tpp);
		}
	}
	
	
	public static synchronized void init(	Platform platform,
							Class<? extends ExoTransactionType> transactionTypeClass,
							String[] transactionProcessorPackages,
							IBlockLogger logger) {
		init(platform, transactionTypeClass, transactionProcessorPackages);
		blockLogger.setLogger(logger,  platform.getAddress().getSelfName());
	}

	/**
	 * Initializes Grizzly-based REST interfaces defined in the included package list, listening on the included port.
	 * Enabling REST will automatically expose the endpoints service and generate an ANNOUNCE_NODE message.
	 * @param port
	 * @param packages
	 */
	public static void initREST(int port, String[] packages) {
		URI baseUri = UriBuilder.fromUri("http://0.0.0.0").port(port).build();
		ResourceConfig config = new ResourceConfig()
				.packages("com.txmq.exo.messaging.rest")
				.register(new CORSFilter())
				.register(JacksonFeature.class);
		
		for (String pkg : packages) {
			config.packages(pkg);
		}
		
		System.out.println("Attempting to start Grizzly on " + baseUri);
		GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

		try {
			platform.createTransaction(
				new ExoMessage(
					new ExoTransactionType(ExoTransactionType.ANNOUNCE_NODE),
					baseUri.toString()
				).serialize(),
				null
			);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Accessor for a reference to the Swirlds platform.  Developers must call 
	 * ExoPlatformLocator.init() to intialize the locator before calling getPlatform()
	 */
	public static Platform getPlatform() throws IllegalStateException {
		if (platform == null) {
			throw new IllegalStateException(
				"PlatformLocator has not been initialized.  " + 
				"Please initialize PlatformLocator in your SwirldMain implementation."
			);
		}
		
		return platform;
	}
	
	/**
	 * Accessor for Swirlds state.  Developers must call ExoPlatformLocator.init()
	 * to initialize the locator before calling getState()
	 */
	public static SwirldState getState() throws IllegalStateException {
		if (platform == null) {
			throw new IllegalStateException(
				"PlatformLocator has not been initialized.  " + 
				"Please initialize PlatformLocator in your SwirldMain implementation."
			);
		}
		
		return platform.getState();
	}
	
	/**
	 * Accessor for the Exo transaction router.  
	 * @see
	 */
	public static ExoTransactionRouter getTransactionRouter() {
		return transactionRouter;
	}
	
	/**
	 * Accessor for the block logging manager
	 */
	public static BlockLogger getBlockLogger() {
		return blockLogger;
	}
}
