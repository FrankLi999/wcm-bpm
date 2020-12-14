package com.bpwizard.gateway.web.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.bpwizard.gateway.plugin.api.result.GatewayResultEnum;
import com.bpwizard.gateway.plugin.base.utils.GatewayResultWrap;
import com.bpwizard.gateway.plugin.base.utils.WebFluxResultUtils;
import com.bpwizard.gateway.web.filter.support.BodyInserterContext;
import com.bpwizard.gateway.web.filter.support.CachedBodyOutputMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type File size filter.
 */
public class FileSizeFilter implements WebFilter {

    private static final int BYTES_PER_MB = 1024 * 1024;

    @Value("${file.size:10}")
    private int maxSize;

    private final List<HttpMessageReader<?>> messageReaders;

    public FileSizeFilter() {
        HandlerStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxSize * BYTES_PER_MB));
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }

    @Override
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)) {
            ServerRequest serverRequest = ServerRequest.create(exchange,
                    messageReaders);
            return serverRequest.bodyToMono(DataBuffer.class)
                    .flatMap(size -> {
                        if (size.capacity() > BYTES_PER_MB * maxSize) {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.BAD_REQUEST);
                            Object error = GatewayResultWrap.error(GatewayResultEnum.PAYLOAD_TOO_LARGE.getCode(), GatewayResultEnum.PAYLOAD_TOO_LARGE.getMsg(), null);
                            return WebFluxResultUtils.result(exchange, error);
                        }
                        BodyInserter<Mono<DataBuffer>, ReactiveHttpOutputMessage> bodyInsert = BodyInserters.fromPublisher(Mono.just(size), DataBuffer.class);
                        HttpHeaders headers = new HttpHeaders();
                        headers.putAll(exchange.getRequest().getHeaders());
                        headers.remove(HttpHeaders.CONTENT_LENGTH);
                        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                                exchange, headers);
                        return bodyInsert.insert(outputMessage, new BodyInserterContext())
                                .then(Mono.defer(() -> {
                                    ServerHttpRequest decorator = decorate(exchange, outputMessage);
                                    return chain.filter(exchange.mutate().request(decorator).build());

                                }));
                    });
        }
        return chain.filter(exchange);

    }

    private ServerHttpRequestDecorator decorate(final ServerWebExchange exchange, final CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}
