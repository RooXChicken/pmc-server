package com.rooxchicken.pmc.Objects;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.rooxchicken.pmc.PMC;
import com.rooxchicken.pmc.Data.Parser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Image extends Component
{
    private static HashMap<String, List<byte[]>> preloadedTextures = new HashMap<String, List<byte[]>>();
    private static final short imageID = 4;

    private static final short preloadID = 5;
    private static final short finishID = 6;

    public String name;

    public boolean blend = false;
    public Color color = Color.WHITE;

    public Image(String _id, String _name, Color _color, boolean _blend, double _posX, double _posY, double _scaleX, double _scaleY)
    {
        super(_id, _posX, _posY, _scaleX, _scaleY);

        name = _name;

        color = _color;
        blend = _blend;
    }

    public static void preload(String _id, File _stream, Player ... _players)
    {
        if(!preloadedTextures.containsKey(_id))
            _preloadTexture(_id, _stream);
        
        if(_players == null)
            return;
            
        for(byte[] _packet : preloadedTextures.get(_id))
        {
            for(Player _player : _players)
                PMC.sendData(_player, _packet);
        }
    }

    private static void _preloadTexture(String _id, File _file)
    {
        try
        {
            byte[] _data = Files.readAllBytes(_file.toPath());
            ArrayList<byte[]> _packets = (ArrayList<byte[]>)Parser.writeLargeArray(_data, _id, preloadID);

            ByteBuf _buf = Unpooled.buffer();
            
            _buf.writeShort(finishID);
            Parser.writeString(_id, _buf);
            _packets.add(_buf.array());

            preloadedTextures.put(_id, _packets);
        }
        catch(Exception e)
        {
            Bukkit.getLogger().info("Failed to open image with id: " + _id + ". " + e);
        }
    }

    @Override
    protected void _sendData(Player ... _players)
    {
        ByteBuf _buf = Unpooled.buffer();
        _buf.writeShort(imageID);
        Parser.writeString(id, _buf);

        Parser.writeString(name, _buf);

        _buf.writeFloat(color.getRed()/255.0f);
        _buf.writeFloat(color.getGreen()/255.0f);
        _buf.writeFloat(color.getBlue()/255.0f);
        _buf.writeFloat(color.getAlpha()/255.0f);
        _buf.writeBoolean(blend);

        for(Player _player : _players)
            PMC.sendData(_player, _buf.array());

        super._sendData(_players);
    }

    @Override
    protected void _destroy(Player ... _players)
    {
        super._destroy(_players);
    }
}
