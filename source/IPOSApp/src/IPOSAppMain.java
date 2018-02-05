import java.nio.charset.StandardCharsets;
import com.swirlds.platform.Browser;
import com.swirlds.platform.Console;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.SwirldState;

public class IPOSAppMain implements SwirldMain {

    public Platform platform;
    public int selfId;
    public Console console;
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
        this.console = platform.createConsole(true);
        platform.setAbout("Swirld v. 1.0\n");
        platform.setSleepAfterSync(sleepPeriod);
    }

    @Override
    public void run() {
        String myName = platform.getState().getAddressBookCopy()
                .getAddress(selfId).getSelfName();

        console.out.println("Hello Swirld from " + myName);

        byte[] transaction = myName.getBytes(StandardCharsets.UTF_8);

        platform.createTransaction(transaction, null);


        String lastReceived = "";

        while (true) {
            IPOSAppState state = (IPOSAppState) platform
                    .getState();
            String received = state.getReceived();

            if (!lastReceived.equals(received)) {
                lastReceived = received;
                console.out.println("Received: " + received);
            }
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