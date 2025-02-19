package com.rooxchicken.pmc.Objects;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rooxchicken.pmc.PMC;
import com.rooxchicken.pmc.Data.Parser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.VarInt;

public class Component extends Payload
{
    private static final short componentID = 1;

    protected double posX = 0;
    protected double posY = 0;

    protected double scaleX = 1;
    protected double scaleY = 1;

    public Component(String _id, double _posX, double _posY, double _scaleX, double _scaleY)
    {
        super(_id);

        posX = _posX;
        posY = _posY;

        scaleX = _scaleX;
        scaleY = _scaleY;
    }

    public void setPosition(double _posX, double _posY, @Nullable List<Player> _players)
    {
        if(_posX == posX && _posY == posY)
            return;

        posX = _posX;
        posY = _posY;
        
        checkAndSend(_players);
    }

    public void setScale(double _scaleX, double _scaleY, @Nullable List<Player> _players)
    {
        if(_scaleX == scaleX && _scaleY == scaleY)
            return;

        scaleX = _scaleX;
        scaleY = _scaleY;
        
        checkAndSend(_players);
    }

    @Override
    protected void _sendData(List<Player> _players)
    {
        ByteBuf _buf = Unpooled.buffer();
        _buf.writeShort(componentID);

        Parser.writeString(id, _buf);
        _buf.writeDouble(posX);
        _buf.writeDouble(posY);
        _buf.writeDouble(scaleX);
        _buf.writeDouble(scaleY);

        for(Player _player : _players)
            PMC.sendData(_player, _buf.array());
    }

    @Override
    public void destroy(List<Player> _players)
    {

    }
}
