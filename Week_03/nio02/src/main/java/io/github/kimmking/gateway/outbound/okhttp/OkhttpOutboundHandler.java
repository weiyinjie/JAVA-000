package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.router.CustomRouter;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountLockedException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class OkhttpOutboundHandler {

    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    private OkHttpClient client;

    private HttpEndpointRouter httpEndpointRouter;

    private List<String> routes;

    private ExecutorService executorService = Executors.newFixedThreadPool(12);

    public OkhttpOutboundHandler(List<String> routes) {
        if (routes != null && routes.size() > 0) {
            List<String> list = new ArrayList<>(routes.size());
            for (String route : routes) {
                if (route.endsWith("/")) {
                    route = route.substring(0, route.length() - 1);
                }
                list.add(route);
            }
            this.routes = list;
        }
        this.httpEndpointRouter = new CustomRouter(CustomRouter.RouteStrategy.ROUND_ROBIN);
        this.client = new OkHttpClient();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        executorService.execute(() -> {
            // 拼装请求地址（路由）
            final String url = httpEndpointRouter.route(routes) + fullRequest.uri();
            // 拼接请求信息
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            FullHttpResponse response = null;
            try {
                Response res = call.execute();
                String result = res.body().string();
                logger.info("result is {}", result);
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
                response.headers().set("Content-Type", "application/json");
                response.headers().setInt("Content-Length", response.content().readableBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fullRequest != null) {
                    if (!HttpUtil.isKeepAlive(fullRequest)) {
                        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        response.headers().set(CONNECTION, KEEP_ALIVE);
                        ctx.write(response);
                    }
                }
                ctx.flush();
            }

        });

    }
}
