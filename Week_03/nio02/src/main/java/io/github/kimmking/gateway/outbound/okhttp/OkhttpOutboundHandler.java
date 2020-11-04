package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.router.CustomRouter;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.List;


public class OkhttpOutboundHandler {

    private OkHttpClient client;

    private HttpEndpointRouter httpEndpointRouter;

    private List<String> routes;

    public OkhttpOutboundHandler(List<String> routes) {
        this.httpEndpointRouter = new CustomRouter(CustomRouter.RouteStrategy.RANDOM);
        this.routes = routes;
        this.client = new OkHttpClient();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        // 路由
        final String url = httpEndpointRouter.route(routes) + fullRequest.uri();
        // 拼接请求信息
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        // 处理响应信息
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String value = response.body().string();
                    Response res = null;
                    try {
                        // 组装响应信息返回
                        res = new Response.Builder()
                                .request(request)
                                .protocol(Protocol.HTTP_1_1)
                                .code(200)
                                .body(ResponseBody.create(MediaType.parse("application/json"), value))
                                .build();

                    } finally {
                        if (fullRequest != null) {
                            if (!HttpUtil.isKeepAlive(fullRequest)) {
                                ctx.write(res).addListener(ChannelFutureListener.CLOSE);
                            } else {
                                ctx.write(res);
                            }
                        }
                        ctx.flush();
                    }
                }
            }
        });
    }
}
