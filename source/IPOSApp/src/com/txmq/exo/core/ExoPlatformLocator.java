package com.txmq.exo.core;

import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldState;
import com.txmq.exo.config.ExoConfig;
import com.txmq.exo.config.MessagingConfig;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.messaging.ExoTransactionType;
import ipos.hashgraph.rest.CORSFilter;
import com.txmq.exo.persistence.BlockLogger;
import com.txmq.exo.persistence.IBlockLogger;
import com.txmq.exo.transactionrouter.ExoTransactionRouter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * A static locator class for Exo platform constructs.  This class allows applications
 * to get access to Swirlds platform, Swirlds state, and the transaction router from
 * anywhere in the application.
 *
 */
public class ExoPlatformLocator {

	private static Platform platform;

	private static ExoTransactionRouter transactionRouter = new ExoTransactionRouter();
	
	private static BlockLogger blockLogger = new BlockLogger();

	public static synchronized void initFromExoConfig(Platform platform) {
		ExoPlatformLocator.platform = platform;
		String nodeName = platform.getAddress().getSelfName();
		ExoConfig config = ExoConfig.getConfig();
        Class<? extends ExoTransactionType> transactionTypeClass = initializeTransactionType(config);
		
		init(platform, transactionTypeClass, config.hashgraphConfig.transactionProcessors);
        initializeRestService(config);
        initializeBlockLogger(nodeName, config);
    }

	public static synchronized void init(Platform platform) {
		ExoPlatformLocator.platform = platform;
		
	}
	
	public static synchronized void init(	Platform platform, 
							Class<? extends ExoTransactionType> transactionTypeClass, 
							String[] transactionProcessorPackages) {
		init(platform);
		try {
			transactionTypeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		for (String tpp : transactionProcessorPackages) {
			transactionRouter.addPackage(tpp);
		}
	}
	

	public static void initREST(int port, String[] packages) {
		URI baseUri = UriBuilder.fromUri("http://0.0.0.0").port(port).build();
		ResourceConfig config = new ResourceConfig()
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
			e1.printStackTrace();
		}
	}
	

	public static Platform getPlatform() throws IllegalStateException {
		if (platform == null) {
			throw new IllegalStateException(
				"PlatformLocator has not been initialized.  " + 
				"Please initialize PlatformLocator in your SwirldMain implementation."
			);
		}
		
		return platform;
	}
	

	public static SwirldState getState() throws IllegalStateException {
		if (platform == null) {
			throw new IllegalStateException(
				"PlatformLocator has not been initialized.  " +
				"Please initialize PlatformLocator in your SwirldMain implementation."
			);
		}

		return platform.getState();
	}


	public static ExoTransactionRouter getTransactionRouter() {
		return transactionRouter;
	}
	

	public static BlockLogger getBlockLogger() {
		return blockLogger;
	}


    /**
     * Private methods
     */

    private static Class<? extends ExoTransactionType> initializeTransactionType(ExoConfig config) {
        Class<? extends ExoTransactionType> transactionTypeClass;
        try {
            Class<?> ttClass = Class.forName(config.hashgraphConfig.transactionTypesClassName);
            transactionTypeClass = (Class<? extends ExoTransactionType>) ttClass;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                    "Error instantiating transaction types enumerator: " + config.hashgraphConfig.transactionTypesClassName
            );
        }
        return transactionTypeClass;
    }

    private static void initializeBlockLogger(String nodeName, ExoConfig config) {
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

    private static void initializeRestService(ExoConfig config) {
        MessagingConfig messagingConfig = null;
        if (config.hashgraphConfig.rest != null) {
            try {
                messagingConfig = parseMessagingConfig(config.hashgraphConfig.rest);
                initREST(messagingConfig.port, messagingConfig.handlers);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error configuring REST:  " + e.getMessage());
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


}
