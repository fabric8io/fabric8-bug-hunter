package io.fabric8.devops.apps.bughunter.codec;

import io.fabric8.devops.apps.bughunter.model.BugInfo;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;

/**
 * @author kameshs
 */
public class BugInfoCodec implements MessageCodec<BugInfo, BugInfo> {

    @Override
    public void encodeToWire(Buffer buffer, BugInfo bugInfo) {

        String jsonBugInfo = Json.encode(bugInfo);
        int length = jsonBugInfo.length();

        buffer.appendInt(length);
        buffer.appendString(jsonBugInfo);
    }

    @Override
    public BugInfo decodeFromWire(int position, Buffer buffer) {
        //FIXME do I need it ?
        return null;
    }

    @Override
    public BugInfo transform(BugInfo bugInfo) {
        return bugInfo;
    }

    @Override
    public String name() {
        return this.getClass().getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
