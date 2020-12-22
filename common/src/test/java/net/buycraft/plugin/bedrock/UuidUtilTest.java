package net.buycraft.plugin.bedrock;

import com.google.common.base.Strings;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class UuidUtilTest {
    private static final String MOJANG_UUID = "10000";
    private static final UUID JAVA_UUID = UUID.fromString("00000000-0000-0000-0000-000000002710");

    @Test
    public void testMojangUuidToJavaUuid() throws Exception {
        Assert.assertEquals(JAVA_UUID, UuidUtil.xuidToJavaUuid(MOJANG_UUID));
    }

    @Test(expected = NullPointerException.class)
    public void testMojangUuidToJavaUuid_NullId() throws Exception {
        UuidUtil.xuidToJavaUuid(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMojangUuidToJavaUuid_InvalidRegex1() throws Exception {
        UuidUtil.xuidToJavaUuid("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMojangUuidToJavaUuid_InvalidRegex2() throws Exception {
        UuidUtil.xuidToJavaUuid(Strings.repeat("!", 32));
    }
}
