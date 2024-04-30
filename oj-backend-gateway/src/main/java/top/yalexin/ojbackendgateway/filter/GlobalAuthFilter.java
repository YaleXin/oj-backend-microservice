package top.yalexin.ojbackendgateway.filter;

import com.alibaba.excel.event.Order;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 拦截器
 */
@Component
public class GlobalAuthFilter implements GlobalFilter, Order {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 内部调用不能走网关
        if(antPathMatcher.match("/**/inner/**", path)){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = dataBufferFactory.wrap(String.valueOf("你没有权限噢").getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(dataBuffer));
        }
        // 其他情况下放行（走下一步）
        return chain.filter(exchange);

        // TODO 使用 JWT 实现用户登录，在网关层面进行统一权限校验
        // TODO 实现Sentinel接口完成限流降级
    }


    /**
     * 如果我们有多个拦截器，则要定义优先级
     * @return
     */
    @Override
    public int order() {
        return 0;
    }
}
