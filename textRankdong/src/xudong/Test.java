package xudong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String filepath="f:/data";
		String outputPath="f:/dataok1/dataok.txt";
		ReadFiles rf=new ReadFiles();
		
		//1.将文件读到map到，对应的是（filename，content）
		Map<String,String> doc_content=rf.readFileAllContent(filepath);
		
		//2.获取分词map，对应的是（filename，words）
		Map<String,String> doc_words = null;
		int D=0;
		try {
			doc_words=rf.cutWordtoMap((HashMap<String, String>) doc_content);
			D=doc_words.keySet().size();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Float> tf=new HashMap<String, Float>();
		Map<String,Float> tfidf=new HashMap<String, Float>();

		for(Map.Entry<String, String> entry:doc_words.entrySet()){
			
			String dataok="";
			
	    	//正向，负向还是中性情感
			String filename=entry.getKey();
			System.out.println(filename);
			
	    	String[] docname=filename.split("_");
	    	String pAndn=docname[1];
	    	System.out.println();
	    	int label;
	    	if(pAndn.equals("正.txt")){
	    		label=1;
	    	}else if(pAndn.equals("负.txt")){
	    		label=-1;
	    	}else{
	    		label=0;
	    	}	 
	    	
			String words=entry.getValue();
			//获取文档的tf
			tf=Tfidf.tfCalculate(words);
			//获取文档的tfidf
			tfidf=Tfidf.tfidfCalculate(D, doc_words, tf);			
			
			File file=new File(filename);
			int doc_id=Tfidf.docIndex.get(file.getName());
			
			for(Map.Entry<String, Integer> entryw:Tfidf.wordIndex.entrySet()){

		    	String word=entryw.getKey();		
		    	int index=entryw.getValue();
		    	
		    	if(tfidf.containsKey(word)){
		    		dataok =dataok+tfidf.get(word)+"\t";
		    	}else{
		    		dataok =dataok+0+"\t";
		    	}
			}
			
			dataok =doc_id+"\t"+dataok+"\t"+label+"\n";
			FileUtils.save(outputPath, dataok, false);
		}

		System.out.println("success");
		
	}
}
