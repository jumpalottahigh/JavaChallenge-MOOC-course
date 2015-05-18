import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

import static org.powermock.api.easymock.PowerMock.*;
import static org.easymock.EasyMock.expect;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import org.powermock.modules.junit4.rule.PowerMockRule;

@Points("5")
@PrepareForTest({HashMap.class, Nicknames.class})
public class NicknamesTest {

    @Rule public PowerMockRule p = new PowerMockRule();

    @Test public void testCreation() throws Exception {

        HashMap hm = new HashMap();
        expectNew(HashMap.class).andReturn(hm);
        replayAll();

        try {
            Nicknames.main(new String[0]);
        } catch (AssertionError e) {
            fail("help "+e);
        } catch (Throwable t) {
            fail("Something went wrong when main-method was run: "+t);
        }

        try {
            verifyAll();
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap()")) {
                fail("You didn't create any HashMap-objects! Additional information:\n"+e);
            }
            fail("Something went wrong: "+e);
        }

    }

    @Test public void testAll() throws Exception {
        HashMap hm = createMock(HashMap.class);
        expectNew(HashMap.class).andReturn(hm);

        expect(hm.put("matti", "mage")).andReturn(null);
        expect(hm.put("mikael", "mixu")).andReturn(null);
        expect(hm.put("arto", "arppa")).andReturn(null);

        //expect(hm.toString()).andReturn(null);
        expect(hm.get("mikael")).andReturn(null);

        replayAll();

        try {
            Nicknames.main(new String[0]);
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap.put")) {
                fail("You made wrong put-call to HashMap! Check the assignment! Additional information:\n"+e);
            }
            if (e.getMessage().contains("HashMap.get")) {
                fail("You made wrong get-call to HashMap! Check the assignment! Additional information:\n"+e);
            }
            fail("help: "+e);
        } catch (Throwable t) {
            fail("Something went wrong when main-method was run: "+t);
        }

        try {
            verifyAll();
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap()")) {
                fail("You didn't create any HashMap-objects! Additional information:\n"+e);
            }
            if (e.getMessage().contains("HashMap.put")) {
                fail("You didn't write right put-calls to HashMap! Additional information:\n"+e);
            }
            if (e.getMessage().contains("HashMap.get")) {
                fail("You didn't write right get-calls to HashMap! Additional information:\n"+e);
            }
            fail("Something went wrong: "+e);
        }
    }

}
