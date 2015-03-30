package net.minecraftforge.gradle.common;

import groovy.lang.Closure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraftforge.gradle.StringUtils;
import net.minecraftforge.gradle.dev.PatcherExtension;
import net.minecraftforge.gradle.json.version.OS;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

public class Constants
{
    // OS
    public static enum SystemArch
    {
        BIT_32, BIT_64;

        public String toString()
        {
            return StringUtils.lower(name()).replace("bit_", "");
        }
    }

    public static final OS               OPERATING_SYSTEM = OS.CURRENT;
    public static final SystemArch       SYSTEM_ARCH      = getArch();
    public static final Charset          CHARSET = Charsets.UTF_8;
    public static final String           HASH_FUNC        = "MD5";
    public static final String           USER_AGENT       = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    // extension names
    public static final String EXT_NAME_MC      = "minecraft";
    public static final String EXT_NAME_JENKINS = "jenkins";

    @SuppressWarnings("serial")
    public static final Closure<Boolean> CALL_FALSE = new Closure<Boolean>(null){ public Boolean call(Object o){ return false; }};
    
    // replacement strings
    
    /** MC version in form "#.#.#(-appendage)" where the appendage may be -pre# or something. **/
    public static final String REPLACE_MC_VERSION   = "{MC_VERSION}";
    /** the folder where to cache.  ~/.gradle/caches/minecraft **/
    public static final String REPLACE_CACHE_DIR    = "{CACHE_DIR}";
    /** project/build **/
    public static final String REPLACE_BUILD_DIR    = "{BUILD_DIR}";
    /** MCP mapping channel **/
    public static final String REPLACE_MCP_CHANNEL  = "{MAPPING_CHANNEL}";
    /** MCP mapping version **/
    public static final String REPLACE_MCP_VERSION  = "{MAPPING_VERSION}";
    /** AssetIndex name **/
    public static final String REPLACE_ASSET_INDEX  = "{ASSET_INDEX}";
    
    // -------- only for dev
//  patern = patern.replace("{MAPPING_CHANNEL_DOC}", exten.getMappingsChannelNoSubtype());
    
    // -------- only for user
//  patern = patern.replace("{RUN_DIR}", exten.getRunDir().replace('\\', '/'));
    
    // -------- remov entirely?
//  patern = patern.replace("{MC_VERSION_SAFE}", exten.getVersion().replace('-', '_'));
//  patern = patern.replace("{MCP_VERSION}", exten.getMcpVersion());
//  patern = patern.replace("{BUILD_NUM}", build);
//  patern = patern.replace("{PROJECT}", project.getName());
//
//    patern = patern.replace("{JENKINS_SERVER}",        jenk.getServer());
//    patern = patern.replace("{JENKINS_JOB}",           jenk.getJob());
//    patern = patern.replace("{JENKINS_AUTH_NAME}",     jenk.getAuthName());
//    patern = patern.replace("{JENKINS_AUTH_PASSWORD}", jenk.getAuthPassword());

    // urls
    public static final String MC_JSON_URL      = "http://s3.amazonaws.com/Minecraft.Download/versions/" + REPLACE_MC_VERSION + "/" + REPLACE_MC_VERSION + ".json";
    public static final String MC_JAR_URL       = "http://s3.amazonaws.com/Minecraft.Download/versions/" + REPLACE_MC_VERSION + "/" + REPLACE_MC_VERSION + ".jar";
    public static final String MC_SERVER_URL    = "http://s3.amazonaws.com/Minecraft.Download/versions/" + REPLACE_MC_VERSION + "/minecraft_server." + REPLACE_MC_VERSION + ".jar";
    public static final String FF_URL           = "http://files.minecraftforge.net/fernflower-fix-1.0.zip";
    public static final String ASSETS_URL       = "http://resources.download.minecraft.net";
    public static final String LIBRARY_URL      = "https://libraries.minecraft.net/";
    public static final String FORGE_MAVEN      = "http://files.minecraftforge.net/maven";
    public static final String ASSETS_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/" + REPLACE_ASSET_INDEX + ".json";

    public static final String LOG              = ".gradle/gradle.log";

    // MCP things
    public static final String CONFIG_MCP_DATA  = "mcpDataConfig";
    public static final String CONFIG_MAPPINGS  = "mcpSnapshotConfig";
    public static final String MCP_JSON_URL     = "http://export.mcpbot.bspk.rs/versions.json";

    // things in the cache dir.
    public static final String NATIVES_DIR      = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_natives/" + REPLACE_MC_VERSION + "";
    public static final String MCP_DATA_DIR     = "{CACHE_DIR}/minecraft/de/oceanlabs/mcp/mcp/" + REPLACE_MC_VERSION + "/";
    public static final String MCP_MAPPINGS_DIR = "{CACHE_DIR}/minecraft/de/oceanlabs/mcp/mcp_" + REPLACE_MCP_CHANNEL +"/" + REPLACE_MCP_VERSION + "/";
    public static final String JAR_CLIENT_FRESH = "{CACHE_DIR}/minecraft/net/minecraft/minecraft/" + REPLACE_MC_VERSION + "/minecraft-" + REPLACE_MC_VERSION + ".jar";
    public static final String JAR_SERVER_FRESH = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_server/" + REPLACE_MC_VERSION + "/minecraft_server-" + REPLACE_MC_VERSION + ".jar";
    public static final String JAR_MERGED       = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_merged/" + REPLACE_MC_VERSION + "/minecraft_merged-" + REPLACE_MC_VERSION + ".jar";
    public static final String FERNFLOWER       = "{CACHE_DIR}/minecraft/fernflower-fixed.jar";
    public static final String EXCEPTOR         = "{CACHE_DIR}/minecraft/exceptor.jar";
    public static final String ASSETS           = "{CACHE_DIR}/minecraft/assets";
    public static final String JSONS_DIR        = "{CACHE_DIR}/minecraft/versionJsons";
    public static final String VERSION_JSON     = JSONS_DIR + "/" + REPLACE_MC_VERSION + ".json";

    // util
    public static final String NEWLINE = System.getProperty("line.separator");

    // helper methods
    public static List<String> getClassPath()
    {
        URL[] urls = ((URLClassLoader) PatcherExtension.class.getClassLoader()).getURLs();

        ArrayList<String> list = new ArrayList<String>();
        for (URL url : urls)
        {
            list.add(url.getPath());
        }
        return list;
    }
    
    public static URL[] toUrls(FileCollection collection) throws MalformedURLException
    {
        ArrayList<URL> urls = new ArrayList<URL>();

        for (File file : collection.getFiles())
            urls.add(file.toURI().toURL());

        return urls.toArray(new URL[urls.size()]);
    }

    public static File getMinecraftDirectory()
    {
        String userDir = System.getProperty("user.home");

        switch (OPERATING_SYSTEM)
            {
                case LINUX:
                    return new File(userDir, ".minecraft");
                case WINDOWS:
                    String appData = System.getenv("APPDATA");
                    String folder = appData != null ? appData : userDir;
                    return new File(folder, ".minecraft");
                case OSX:
                    return new File(userDir, "Library/Application Support/minecraft");
                default:
                    return new File(userDir, "minecraft");
            }
    }

    private static SystemArch getArch()
    {
        String name = StringUtils.lower(System.getProperty("os.arch"));
        if (name.contains("64"))
        {
            return SystemArch.BIT_64;
        }
        else
        {
            return SystemArch.BIT_32;
        }
    }

    public static String hash(File file)
    {
        if (file.getPath().endsWith(".zip") || file.getPath().endsWith(".jar"))
            return hashZip(file, HASH_FUNC);
        else
            return hash(file, HASH_FUNC);
    }

    public static List<String> hashAll(File file)
    {
        LinkedList<String> list = new LinkedList<String>();

        if (file.isDirectory())
        {
            for (File f : file.listFiles())
                list.addAll(hashAll(f));
        }
        else if (!file.getName().equals(".cache"))
            list.add(hash(file));

        return list;
    }

    public static String hash(File file, String function)
    {

        try
        {
            InputStream fis = new FileInputStream(file);
            byte[] array = ByteStreams.toByteArray(fis);
            fis.close();

            return hash(array, function);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String hashZip(File file, String function)
    {
        try
        {
            MessageDigest hasher = MessageDigest.getInstance(function);

            ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;
            while ((entry = zin.getNextEntry()) != null)
            {
                hasher.update(entry.getName().getBytes());
                hasher.update(ByteStreams.toByteArray(zin));
            }
            zin.close();

            byte[] hash = hasher.digest();


            // convert to string
            String result = "";

            for (int i = 0; i < hash.length; i++)
            {
                result += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String hash(String str)
    {
        return hash(str.getBytes());
    }

    public static String hash(byte[] bytes)
    {
        return hash(bytes, HASH_FUNC);
    }

    public static String hash(byte[] bytes, String function)
    {
        try
        {
            MessageDigest complete = MessageDigest.getInstance(function);
            byte[] hash = complete.digest(bytes);

            String result = "";

            for (int i = 0; i < hash.length; i++)
            {
                result += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static PrintStream getTaskLogStream(Project project, String name)
    {
        final File taskLogs = new File(project.getBuildDir(), "taskLogs");
        taskLogs.mkdirs();
        final File logFile = new File(taskLogs, name);
        logFile.delete(); //Delete the old log
        try
        {
            return new PrintStream(logFile);
        }
        catch (FileNotFoundException ignored)
        {}
        return null; // Should never get to here
    }
    
    /**
     * Throws a null runtime exception if the resource isnt found.
     * @param resource
     * @return URL
     */
    public static URL getResource(String resource)
    {
        ClassLoader loader = BaseExtension.class.getClassLoader();
        
        if (loader == null)
            throw new RuntimeException("ClassLoader is null! IMPOSSIBRU");
        
        URL url = loader.getResource(resource);
        
        if (url == null)
            throw new RuntimeException("Resource "+resource+" not found");
        
        return url;
    }

    /**
     * Resolves the supplied object to a string.
     * If the input is null, this will return null.
     * Closures and callables are called with no arguments.
     * Arrays use Arrays.toString().
     * File objects return their absolute paths.
     * All other objects have their toString run.
     * @return resovled string
     */
    @SuppressWarnings("rawtypes")
    public static String resolveString(Object obj)
    {
        if (obj == null)
            return null;

        // stop early if its the right type. no need to do more expensive checks
        if (obj instanceof String)
            return (String) obj;

        if (obj instanceof Closure)
            return resolveString(((Closure) obj).call()); // yes recursive.
        if (obj instanceof Callable)
        {
            try
            {
                return resolveString(((Callable) obj).call());
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else if (obj instanceof File)
            return ((File) obj).getAbsolutePath();

        // arrays
        else if (obj.getClass().isArray())
        {
            if (obj instanceof Object[])
                return Arrays.toString(((Object[]) obj));
            else if (obj instanceof byte[])
                return Arrays.toString(((byte[]) obj));
            else if (obj instanceof char[])
                return Arrays.toString(((char[]) obj));
            else if (obj instanceof int[])
                return Arrays.toString(((int[]) obj));
            else if (obj instanceof float[])
                return Arrays.toString(((float[]) obj));
            else if (obj instanceof double[])
                return Arrays.toString(((double[]) obj));
            else if (obj instanceof long[])
                return Arrays.toString(((long[]) obj));
            else
                return obj.getClass().getSimpleName();
        }

        else
            return obj.toString();
    }
}
