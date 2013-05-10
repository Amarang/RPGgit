import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

class Load
{	
    public Load() {
	}
	
	public String readFileToString(String fileName) {
		String dataStr = "";
		
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		
			Scanner s = new Scanner(is).useDelimiter("\\A");
			dataStr = s.hasNext() ? s.next() : "";
			
			s.close();
			is.close();
			
		} catch (Exception e) {
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		return dataStr;
	}
	
	public String[][] readFileToArray(String fileName) {
		String dataStr = "";
		
		String[] lineArray;
		
		int numLines = 0;
		int numCols = 0;
		
		
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		
			Scanner s = new Scanner(is).useDelimiter("\\A");
			dataStr = s.hasNext() ? s.next() : "";
			
			s.close();
			is.close();
			
		} catch (Exception e) {
			System.out.println("couldn't get " + fileName); 
			e.printStackTrace();
		}
		
		
		
		dataStr += "\n";
		
		dataStr.replace("\r","");
				
			numLines = dataStr.split("\n").length;
			String[] colCountArray = dataStr.split("\n");
			
			for(int i = 0; i < colCountArray.length; i++) {
				int cols = colCountArray[i].split("\t").length;
				
				if(cols > numCols) numCols = cols;
				
			}


		String[][] lines = new String[numLines][numCols];
		String temp = "";
		int nline = 0;
		for (int i=0; i < dataStr.length(); i++) {
			char c = dataStr.charAt(i);
			if(c == '\n') {
				Pattern pattern = Pattern.compile(Pattern.quote("\t"));
				temp = temp.replace("\n", "");
				lineArray = pattern.split(temp);
				
				if(!(temp.length() < 1 || lineArray.length == 1 || temp.length() == 0 || temp.charAt(0) == '/')) {
					lines[nline] = lineArray;
					nline++;
				}
				temp = "";
			}
			temp += c;
		}
		//System.out.println("nline " + nline + " numCols " + numCols);
		//System.out.println(Arrays.deepToString(lines));
		String[][] linesClean = new String[nline][numCols];
		
		int y = 0;
		for(int j = 0; j < lines.length; j++) {
			//System.out.println("lines[j][0] " + lines[j][0]);
				if(lines[j][0] != null) {
					linesClean[y] = lines[j];
					y++;
				}
		}	
		//System.out.println("y " + y);
		//System.out.println(Arrays.deepToString(linesClean));
		return linesClean;
	}
	
	public void loadSite(String surl) {

		try {
			URL url = new URL(surl);
		    InputStream is = url.openConnection().getInputStream();
		    BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
		    reader.close();
		} catch (Exception e) {
			System.out.println("couldn't load " + surl); 
			e.printStackTrace();
		}
	}

	public String readSiteToString(String surl) {

	    String strbuff = "";
	    
		try {
			URL url = new URL(surl);
		    InputStream is = url.openConnection().getInputStream();
		
		    BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
		    
		
		    String line = null;
		    while( ( line = reader.readLine() ) != null )  {
		       strbuff += line + "\n";
		    }
		    reader.close();
		} catch (Exception e) {
			System.out.println("couldn't load " + surl); 
			e.printStackTrace();
		}
		return strbuff;
	}
	
	public String[][] readSiteToArray(String surl) {

		String[] lineArray;
		
		int numLines = 0;
		int numCols = 0;
		String strbuff = "";
		String[][] linesClean = null;

		try {
			URL url = new URL(surl);
		    InputStream is = url.openConnection().getInputStream();
		
		    BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
		
		    String line = null;
		    while( ( line = reader.readLine() ) != null )  {
		       strbuff += line + "\n";
		    }
		    reader.close();
		    
		    strbuff.replace("\r","");
					
			numLines = strbuff.split("\n").length;
			String[] colCountArray = strbuff.split("\n");
			
			for(int i = 0; i < colCountArray.length; i++) {
				int cols = colCountArray[i].split("\t").length;
				
				if(cols > numCols) numCols = cols;
				
			}


			String[][] lines = new String[numLines][numCols];
			String temp = "";
			int nline = 0;
			for (int i=0; i < strbuff.length(); i++) {
				char c = strbuff.charAt(i);
				if(c == '\n') {
					Pattern pattern = Pattern.compile(Pattern.quote("\t"));
					temp = temp.replace("\n", "");
					lineArray = pattern.split(temp);
					
					if(!(temp.length() < 1 || lineArray.length == 1 || temp.length() == 0 || temp.charAt(0) == '/')) {
						lines[nline] = lineArray;
						nline++;
					}
					temp = "";
				}
				temp += c;
			}
			//System.out.println("nline " + nline + " numCols " + numCols);
			//System.out.println(Arrays.deepToString(lines));
			linesClean = new String[nline][numCols];
			
			int y = 0;
			for(int j = 0; j < lines.length; j++) {
				//System.out.println("lines[j][0] " + lines[j][0]);
					if(lines[j][0] != null) {
						linesClean[y] = lines[j];
						y++;
					}
			}	
			//System.out.println("y " + y);
			//System.out.println(Arrays.deepToString(linesClean));
		    
		    
		} catch (Exception e) {
			System.out.println("couldn't load " + surl); 
			e.printStackTrace();
		}
		

		return linesClean;
	}
}