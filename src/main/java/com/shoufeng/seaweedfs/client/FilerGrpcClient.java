package com.shoufeng.seaweedfs.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

/**
 * @author shoufeng
 */
public class FilerGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(FilerGrpcClient.class);

    private final ManagedChannel channel;
    private final SeaweedFilerGrpc.SeaweedFilerBlockingStub blockingStub;
    private final SeaweedFilerGrpc.SeaweedFilerStub asyncStub;
    private final SeaweedFilerGrpc.SeaweedFilerFutureStub futureStub;

    static SslContext sslContext;

    static {
        try {
            sslContext = FilerSslContext.loadSslContext();
        } catch (SSLException e) {
            logger.warn("failed to load ssl context", e);
        }
    }

    public FilerGrpcClient(String host, int grpcPort) {
        this(host, grpcPort, sslContext);
    }

    public FilerGrpcClient(String host, int grpcPort, SslContext sslContext) {

        this(sslContext == null ?
                ManagedChannelBuilder.forAddress(host, grpcPort).usePlaintext() :
                NettyChannelBuilder.forAddress(host, grpcPort)
                        .negotiationType(NegotiationType.TLS)
                        .sslContext(sslContext));

    }

    public FilerGrpcClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = SeaweedFilerGrpc.newBlockingStub(channel);
        asyncStub = SeaweedFilerGrpc.newStub(channel);
        futureStub = SeaweedFilerGrpc.newFutureStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public SeaweedFilerGrpc.SeaweedFilerBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public SeaweedFilerGrpc.SeaweedFilerStub getAsyncStub() {
        return asyncStub;
    }

    public SeaweedFilerGrpc.SeaweedFilerFutureStub getFutureStub() {
        return futureStub;
    }

}
