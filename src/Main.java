import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;
import java.util.HashMap;

public class Main{
  
  /* main functionality of this program */
  public static void moveAllFiles(Path from,Path to,boolean removeDirTrees) throws IOException{
  
    /* gives a Stream<Path> */
    Files.walk(from).forEach(p->{
      Path fn=null;
      if(p.equals(from) || Files.isDirectory(p))
        return;
      try{
        Files.move(p,Path.of(to.toString(),p.getFileName().toString()));
      }catch(IOException ioe){
        System.err.println("[error moving]:\t"+ioe.getMessage());
        return;
      }
    }); 
    
    /* remove the remaining directories */
    if(removeDirTrees){
	    Files.walk(from).filter(Files::isDirectory).forEach(p->{
	      if(p.equals(from))
	        return;
	      try{
	        delFoldersRec(p.toFile());
	        // System.out.println("[deleting]:\t"+p.toString());
	      }catch(Exception ioe){
	        System.err.println("[error delete]:\t"+ioe.getMessage());
	        return;
	      }
	    });
    }

  }
  
  /* recursively find the last folder in a tree and remove from ther to the top */
  public static void delFoldersRec(File f) throws IOException{ 
    File[] fs=f.listFiles();
    for(File file:fs)
      if(file.isDirectory())
        delFoldersRec(file);
    f.delete();
  }
  
  public static void main(String[] args){
    
    /* parse args and print help */
    Map<String,String> argses=parseCommandLine(args);
    if(argses.containsKey("HELP_NEEDED")){
     System.out.println(
      "\nFlatten Folders (ffold) by @AmirHBahrami (github)\n"
      +"\tthis program flattens the files of all folders to only one folder\n"
      +"\tto use this program, simply give the root of the folders with the flag --root or -r\n"
      +"\tand give the destination via the flag --to or -t\n"
      +"\tif no destination is given (no -t flag) the root will be flattened in itself\n"
      +"\tand if you want to delete the subtree remaining folders add -dst or --del-sub-tree\n"
      +"\nExample: "
      +"\n\t\tjava -cp bin Main -r /home/userxyz/Pictures/Wallpapers (-t /home/userxyz/Pictures/Walps) (-dst)\n"
      );
      System.exit(0);
    }

    /* an app to flatten all the folders inside a specific folder */
    Path root=null,to=null;
    boolean delSubTrees=argses.containsKey("dst");
    System.out.println(delSubTrees);
    try{
      
      /* input checking */
      if(argses.containsKey("root"))
        root=Path.of(argses.get("root"));
      
      if(argses.containsKey("to"))
        to=Path.of(argses.get("to"));

      else
        to=Path.of(argses.get("root"));
         
      if(!Files.exists(root))
        throw new IOException("[Root Path is Incorrect]\nRoot Path:"+root.toString());

      if(!Files.exists(to))
        throw new IOException("[Destination Path is Incorrect]\nDestination Path:"+to.toString());

      /* actual operation */
      moveAllFiles(root,to,delSubTrees);

    }catch(Exception e){
      System.err.println(e.getMessage());
      System.exit(1);
    }

  }

  /* up until now, only two flags (arguments are required) */
  public static Map<String,String> parseCommandLine(String[] args){
    
    Map<String,String> m=new HashMap<String,String>();
    
    // for some reason a:args didn't work (jdk 11)
    for(int i=0;i<args.length;i++){
      if(args[i].equals("--root") || args[i].equals("-r"))
        m.put("root",args[i+1]);
      else if(args[i].equals("--to") || args[i].equals("-t"))
        m.put("to",args[i+1]);
      else if(args[i].equals("-dst") || args[i].equals("--del-sub-trees"))
        m.put("dst","TRUE");
      else if(args[i].equals("-h") || args[i].equals("--help")){
        m.put("HELP_NEEDED","TRUE");
        break; // nothing more is needed to know at this point
      }
    }

    return m;
  }

}
