package com.rooxchicken.agnilib.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.rooxchicken.agnilib.AgniLib;

public abstract class Task implements Listener
{
    protected AgniLib plugin;
    
    protected int tickThreshold = 1;
    private int tick = 0;
    protected int t = 0;

    public boolean cancel = false;

    public Task(AgniLib _plugin)
    {
        plugin = _plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void tick()
    {
        if(tickThreshold <= 1)
        {
            run();
            t++;

            return;
        }

        tick++;
        if(tick < tickThreshold-1)
            return;

        run();
        t++;
        tick = 0;
    }

    public void run() {}
    public void onCancel() {}
}
