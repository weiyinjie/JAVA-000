package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomRouter implements HttpEndpointRouter {

    private RouteStrategy strategy;

    private AtomicInteger idx;

    /**
     * 随机
     *
     * @param endpoints
     * @return
     */
    @Override
    public String route(List<String> endpoints) {
        return strategy.route(endpoints, idx);

    }

    public CustomRouter(RouteStrategy routeStrategy) {
        this.strategy = routeStrategy;
        this.idx = new AtomicInteger(0);
    }

    public enum RouteStrategy {
        /**
         * 随机
         */
        RANDOM {
            @Override
            public String route(List<String> endpoints, AtomicInteger currentIdx) {
                int idx = (int) (Math.random() * endpoints.size());
                return endpoints.get(idx);
            }
        },
        /**
         * 轮询
         */
        ROUND_ROBIN() {
            @Override
            public String route(List<String> endpoints, AtomicInteger currentIdx) {

                return null;
            }
        };

        public abstract String route(List<String> endpoints, AtomicInteger currentIdx);
    }
}