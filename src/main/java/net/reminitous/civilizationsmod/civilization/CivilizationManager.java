public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("civilizationsmod", "main"),
        () -> "1.0",
        s -> true,
        s -> true
);

private static int packetId = 0;

public static <T> void registerMessage(Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder,
                                       Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler) {
    CHANNEL.registerMessage(packetId++, clazz, encoder, decoder, handler);
}

// Example registration in your init method
public static void registerPackets() {
    registerMessage(SyncCivilizationDataPacket.class,
            (packet, buf) -> packet.toBytes(buf),
            buf -> new SyncCivilizationDataPacket(buf),
            (packet, ctx) -> packet.handle(ctx));
}
