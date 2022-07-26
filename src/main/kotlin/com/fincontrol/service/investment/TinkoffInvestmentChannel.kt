package com.fincontrol.service.investment

import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Component for creating tinkoff investment gRPC channel
 */
@Component
class TinkoffInvestmentChannel(
    @Value("\${tinkoff.investment.host}")
    private val host: String,
    @Value("\${tinkoff.investment.port}")
    private val port: Int,
    @Value("\${tinkoff.investment.tokenPrefix}")
    private val tokenPrefix: String,
) {
    /**
     * Get channel for tinkoff investment gRPC API
     * @param token Investment token
     * @return channel
     */
    fun getChannel(token: String): Channel {
        val headers = Metadata()
        val authKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
        headers.put(authKey, "$tokenPrefix $token")

        return ManagedChannelBuilder.forAddress(host, port)
            .intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
            .useTransportSecurity()
            .build()
    }
}
