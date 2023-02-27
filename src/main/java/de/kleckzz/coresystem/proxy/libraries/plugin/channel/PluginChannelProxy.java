/*
MIT License

Copyright (c) 2021 Paul Rakutt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package de.kleckzz.coresystem.proxy.libraries.plugin.channel;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author KeksGauner
 * from https://github.com/Kleckzz/KleckzzCoreLib-Outdated
 * this is outdated but works
 */
public class PluginChannelProxy implements Listener {

    private final Plugin plugin;
    private String incomingPluginChannel;
    private String outgoingPluginChannel;

    public PluginChannelProxy(Plugin plugin) {
        if (plugin == null)
            throw new IllegalArgumentException("plugin cannot be null");
        this.plugin = plugin;
    }

    public void registerIncomingPluginChannel(String incomingPluginChannel) {
        this.incomingPluginChannel = incomingPluginChannel.toLowerCase();
        plugin.getProxy().registerChannel(this.incomingPluginChannel);
        plugin.getLogger().log(Level.INFO, "Plugin registered incoming plugin channel: " + this.incomingPluginChannel);
    }

    public void registerOutgoingPluginChannel(String outgoingPluginChannel) {
        this.outgoingPluginChannel = outgoingPluginChannel.toLowerCase();
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
        plugin.getLogger().log(Level.INFO, "Plugin registered outgoing plugin channel: " + this.outgoingPluginChannel);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent pluginMessageEvent) {
        if (pluginMessageEvent.getTag().equalsIgnoreCase(incomingPluginChannel)) {
            plugin.getLogger().log(Level.INFO, "Reserving bytes from: " + incomingPluginChannel);
            ProxyServer.getInstance().getPluginManager().callEvent(new PluginChannel(plugin, incomingPluginChannel, pluginMessageEvent));
        }
    }

    public ByteArrayOutputStream createStream(ArrayList<Object> stringArrayList) throws IOException, IllegalClassFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        for(Object input : stringArrayList) {
            // Convert to ByteArrayDataOutput
            switch (input.getClass().getSimpleName()) {
                case "String":
                    dataOutputStream.writeUTF((String) input);
                    break;
                case "Integer":
                    dataOutputStream.writeInt((int) input);
                    break;
                case "Boolean":
                    dataOutputStream.writeBoolean((boolean) input);
                    break;
                case "Byte":
                    dataOutputStream.writeByte((byte) input);
                    break;
                case "Char":
                    dataOutputStream.writeChar((char) input);
                    break;
                case "Double":
                    dataOutputStream.writeDouble((double) input);
                    break;
                case "Float":
                    dataOutputStream.writeFloat((float) input);
                    break;
                case "Long":
                    dataOutputStream.writeLong((long) input);
                    break;
                case "Short":
                    dataOutputStream.writeShort((short) input);
                    break;
                default:
                    //Main.getInstance().getProxy().getLogger().log(Level.SEVERE, "Cannot find \"" + input.getClass().getSimpleName() + "\" this is not implemented yet. Please report it!");
                    throw new IllegalClassFormatException();
            }
        }
        return outputStream;
    }

    public void sendMessage(ServerInfo server, ArrayList<Object> sendArrayList) throws IOException, IllegalClassFormatException {
        plugin.getLogger().log(Level.INFO, "Sending bytes with: " + outgoingPluginChannel);
        server.sendData(outgoingPluginChannel, createStream(sendArrayList).toByteArray());
    }
}