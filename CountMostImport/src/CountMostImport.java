/**
 * Created by steamedfish on 17-6-26.
 */
import java.io.*;
import java.util.*;

public class CountMostImport {
    private HashMap<String,Integer> map = new HashMap<String,Integer>();
//  C++ pair
    private class Pair implements Comparable<Pair>{
        String importer;
        int count;
        Pair(String ip,int ct){
            importer = ip;
            count = ct;
        }
        public int compareTo(Pair pth){
            return pth.count-count;
        }
        public String toString(){
            return importer+"----->"+count;
        }
    }
//analyze the java file
    private void numberOfImport(File f)throws Exception{
        FileInputStream in = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str="";
        while((str=reader.readLine())!=null){
            if(str.startsWith("package")||"".equals(str)){
                continue;
            }
            if(str.startsWith("import")){
                str=str.replaceAll("\\s*","");
                str=str.substring(6);
                Integer value=map.get(str);
                if(value==null){
                    map.put(str,1);
                }else{
                    map.put(str,value+1);
                }
            }else{
                continue;
            }
        }
        reader.close();
    }

    //read the directory or java file
    private void searchDirectory(File file)throws FileNotFoundException{
        if(file==null||!file.exists())
            throw new FileNotFoundException(file+" ,file not found!");
        if(file.isDirectory()){
            File[] files=file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".java")||pathname.isDirectory();
                }
            });
            for(File target:files){
                searchDirectory(target);
            }
        }else{
            try {
                numberOfImport(file);
            }catch (Exception e){

            }
        }
    }

    //get the most import file
    private String getMostImport(){
        Iterator<String> iter=map.keySet().iterator();
        String result="";
        int max=0;
        while(iter.hasNext()){
            String key=iter.next();
           // System.out.println(key+" "+map.get(key));
            if(map.get(key)>max){
                max=map.get(key);
                result=key;
            }
        }
        return result;
    }
    //statistics erery import file and sort it use array
    private void frontTenImport(){
        Iterator<String> iter=map.keySet().iterator();
        List<Pair> pairs=new ArrayList<Pair>();
        int cnt=0;
        while(iter.hasNext()) {
            cnt++;
            String key= iter.next();
            Pair pair=new Pair(key,map.get(key));
            pairs.add(pair);
        }
        Pair[] pairArr=new Pair[0];
        pairArr = pairs.toArray(pairArr);
        Arrays.sort(pairArr);
        for(int i = 0;i < 10&&i<cnt;i++){
            System.out.println(pairArr[i].toString());
        }
    }
    public static void main(String[] args){
        CountMostImport cmi=new CountMostImport();
        System.out.println("Please Enter the name of java file or directory");
        Scanner in = new Scanner(System.in);
        String filePath=in.nextLine();
        File file=new File(filePath);
        try {
            cmi.searchDirectory(file);
            String mostImport=cmi.getMostImport();
            System.out.println(mostImport);
            cmi.frontTenImport();
        }catch(FileNotFoundException e) {

        }
    }
}