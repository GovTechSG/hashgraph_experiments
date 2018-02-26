import com.swirlds.platform.Browser;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.SwirldState;
import com.txmq.exo.core.ExoPlatformLocator;
import ipos.hashgraph.IPOSAppState;


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
		platform.setAbout("Hello Swirld v. 1.0\n");
		platform.setSleepAfterSync(sleepPeriod);

		ExoPlatformLocator.initFromExoConfig(platform);
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
		return new IPOSAppState();
	}
}