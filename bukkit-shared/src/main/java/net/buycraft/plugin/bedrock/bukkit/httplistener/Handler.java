package net.buycraft.plugin.bedrock.bukkit.httplistener;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import net.buycraft.plugin.bedrock.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.bedrock.data.QueuedCommand;
import net.buycraft.plugin.bedrock.data.QueuedPlayer;
import net.buycraft.plugin.bedrock.execution.strategy.ToRunQueuedCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Handler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private JsonArray body;
    private BuycraftPluginBase plugin;

    public Handler(BuycraftPluginBase plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(422));
        ChannelPromise promise = ctx.channel().newPromise();

        if (request.getUri().equalsIgnoreCase("/ping")) {
            response.content().writeBytes(Charsets.UTF_8.encode("Connection Established"));
            response.setStatus(HttpResponseStatus.valueOf(200));
        } else {
            String body = request.content().toString(Charsets.UTF_8);
            String hash = Hashing.sha256().hashString(body.concat(plugin.getConfiguration().getServerKey()), Charsets.UTF_8).toString();
            if (hash.equals(request.headers().get("X-Signature"))) {
                try {
                    this.body = new JsonParser().parse(body).getAsJsonArray();
                } catch (Exception e) {
                    response.content().writeBytes(Charsets.UTF_8.encode("Invalid JSON"));
                    response.setStatus(HttpResponseStatus.valueOf(422));
                }

                if (this.body != null) {
                    Object[] pc = pushCommand();
                    response.content().writeBytes(Charsets.UTF_8.encode(String.valueOf(pc[1])));
                    response.setStatus(HttpResponseStatus.valueOf(Integer.valueOf(pc[0].toString())));
                }
            } else {
                response.content().writeBytes(Charsets.UTF_8.encode("Invalid signature"));
                response.setStatus(HttpResponseStatus.valueOf(422));
            }
        }

        ctx.channel().writeAndFlush(response, promise);
        promise.addListener((ChannelFutureListener) future -> future.channel().close());
    }

    private Object[] pushCommand() {
        int playerId = 0;
        for (JsonElement command : this.body) {
            if (command instanceof JsonObject) {
                JsonObject commandObject = command.getAsJsonObject();
                String uuid = commandObject.get("username").getAsString().replace("-", "");
                if (!plugin.getServerInformation().getAccount().isOnlineMode()) {
                    uuid = null;
                }
                QueuedPlayer qp = new QueuedPlayer(playerId, commandObject.get("username_name").getAsString(), uuid);
                Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
                map.put("delay", commandObject.get("delay").getAsInt());

                if (commandObject.get("require_slots").getAsInt() > 0) {
                    map.put("slots", commandObject.get("require_slots").getAsInt());
                }

                int packageId = 0;
                if (commandObject.has("package") && !commandObject.get("package").isJsonNull()) {
                    packageId = commandObject.get("package").getAsInt();
                }

                QueuedCommand qc = new QueuedCommand(commandObject.get("id").getAsInt(),
                        commandObject.get("payment").getAsInt(),
                        packageId,
                        map,
                        commandObject.get("command").getAsString(),
                        qp);
                plugin.getCommandExecutor().queue(new ToRunQueuedCommand(qp, qc, commandObject.get("require_online").getAsInt() == 1));
                playerId += 1;
            }
        }

        return new Object[]{200, "Commands executed"};
    }
}