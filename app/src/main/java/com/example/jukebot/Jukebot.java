package com.example.jukebot;

import io.nut.base.os.OSName;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class Jukebot
{

    static final String COMMAND_LINUX = "mplayer";
    static final String COMMAND_MACOSX = "/opt/homebrew/bin/mplayer";

    public static Jukebot getInstance()
    {
        if (OSName.os.isLinux())
        {
            return new Jukebot(COMMAND_LINUX);
        }
        if (OSName.os.isMacosx())
        {
            return new Jukebot(COMMAND_MACOSX);
        }
        return null;
    }

    private final String command;

    private Jukebot(String command)
    {
        this.command = command;
    }

    private ArrayList<File> mp3 = new ArrayList<>();
    
    public boolean load(File path)
    {
        boolean modified = false;
        if(path.isFile())
        {
            mp3.add(path);
            modified = true;
        }
        else if(path.isDirectory())
        {
            File[] items = path.listFiles();

            for(File item : items)
            {
                modified |= load(item);
            }
        }
        return modified;
    }
    
    public boolean play()
    {
        for(File item : mp3)
        {
            play(item);
        }
        return true;
    }
    
    public boolean play(File file)
    {
        try
        {
            Process proceso = new ProcessBuilder(command, file.getAbsolutePath())
//                    .inheritIO()
                    .start();

            // Esperar a que el proceso termine
            int codigoSalida = proceso.waitFor();

            // Imprimir el código de salida del proceso
            System.out.println("El comando terminó con código: " + codigoSalida);

            return true;
        }
        catch (IOException | InterruptedException ex)
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
