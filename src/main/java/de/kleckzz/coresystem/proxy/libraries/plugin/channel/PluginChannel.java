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

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author KeksGauner
 * from https://github.com/Kleckzz/KleckzzCoreLib-Outdated
 * this is outdated but works
 */
@SuppressWarnings("unused")
public class PluginChannel extends Event {

    private final Plugin plugin;
    private final String channel;
    private final byte[] message;
    private final PluginMessageEvent pluginMessageEvent;

    public PluginChannel(Plugin plugin, String channel, PluginMessageEvent pluginMessageEvent) {
        this.plugin = plugin;
        this.channel = channel;
        this.message = pluginMessageEvent.getData();
        this.pluginMessageEvent = pluginMessageEvent;
    }

    public Plugin getPlugin() { return this.plugin; }

    public ServerInfo getServer() {
        return plugin.getProxy().getPlayer(pluginMessageEvent.getReceiver().toString()).getServer().getInfo();
    }

    public PluginMessageEvent getPluginMessageEvent() { return this.pluginMessageEvent; }

    public String getChannel() { return this.channel; }

    public byte[] getMessage() { return this.message; }

    public DataInputStream getDataInputStream() {
        return new DataInputStream(new ByteArrayInputStream(this.message)); }

    public ArrayList<Object> getStringArray() throws IOException {
        DataInputStream dataInputStream = getDataInputStream();
        ArrayList<Object> arrayList = new ArrayList<>();

        while (dataInputStream.available() > 0) {
            arrayList.add(dataInputStream.readUTF());
        }

        return arrayList;
    }

}