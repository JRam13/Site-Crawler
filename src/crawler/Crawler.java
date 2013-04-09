package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * 
 * 
 * Basics of crawlers: (taken from stackoverflow)
	¥ A list of unvisited URLs - seed this with one or more starting pages
	¥ A list of visited URLs - so you don't go around in circles
	¥ A set of rules for URLs you're not interested in - so you don't index the whole Internet

Put these in persistent storage, so you can stop and start the crawler without losing state.
Algorithm is:

while(list of unvisited URLs is not empty) {
    take URL from list
    fetch content
    record whatever it is you want to about the content
    if content is HTML {
        parse out URLs from links
        foreach URL {
           if it matches your rules
              and it's not already in either the visited or unvisited list
              add it to the unvisited list
        }
    }
}
 */


public class Crawler {
	
	private static String url;
	private static ArrayList<String> unvisitedURLs = new ArrayList<String>();
	private static ArrayList<String> visitedURLs = new ArrayList<String>();
	private static String currentURL;

	public static void main(String[] args) throws IOException {
		
		Scanner scn = new Scanner(System.in); 
		System.out.println("Input url you wish to crawl");
		url = scn.nextLine();
		
		//System.out.println(url);
		unvisitedURLs.add(url);
		
		try {
			returnURLs(unvisitedURLs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unvisited sites: "+unvisitedURLs.size());
			if(unvisitedURLs.size() != 0){
				currentURL = unvisitedURLs.remove(0);
				returnURLs(unvisitedURLs);
			}
			else{
				System.out.println("Done 1");
				System.out.println("Number of Visited Sites: "+visitedURLs.size());
				printArray(visitedURLs);
				System.exit(0);
			}
		}

	}

	private static void returnURLs(ArrayList<String> unvisitedURLs) throws IOException {
		currentURL = null;
		Document doc = null;
		
		while(!unvisitedURLs.isEmpty()){
			currentURL = unvisitedURLs.remove(0);
			visitedURLs.add(currentURL);
			doc = fetchHTML();
			
			//add url to unvisited if it has not been visited yet
			Elements links = null;
			try {
				links = doc.select("a[href]");
			} catch (Exception e) {
				
				if(unvisitedURLs.size() != 0){
					currentURL = unvisitedURLs.remove(0);
					returnURLs(unvisitedURLs);
				}
				else{
					System.out.println("Done 2");
					//printArray(visitedURLs);
				}
				
			}
			try {
				for (Element element : links) {
					String absHref = element.attr("abs:href"); 
					if(isUnique(absHref)){
						unvisitedURLs.add(absHref);
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				if(unvisitedURLs.size() != 0){
					currentURL = unvisitedURLs.remove(0);
					returnURLs(unvisitedURLs);
				}
				else{
					System.out.println("Done 3");
					//printArray(visitedURLs);
					System.out.println("Number of Visited Sites: "+visitedURLs.size());
					printArray(visitedURLs);
					System.exit(0);
					
				}
				
			}
			
		}
		
		
		
		
		
		
	}

	private static void printArray(ArrayList<String> urlList) throws IOException {
		for (int i = 0; i < urlList.size(); i++) {
			System.out.println(urlList.get(i));
			
		}
	}

	private static Document fetchHTML() throws IOException{
		Document doc = null;
		try {
			if(isInternal()){
				return doc = Jsoup.connect(currentURL).get();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(unvisitedURLs.size() != 0){
				currentURL = unvisitedURLs.remove(0);
				returnURLs(unvisitedURLs);
			}
			else{
				System.out.println("Done 4");
				//printArray(visitedURLs);
			}
			
		}
		return doc;
	}
	
	private static boolean isUnique(String url2){
		for (int i = 0; i < unvisitedURLs.size(); i++) {
			//System.out.println("URL Curent: " +url2);
			//System.out.println("URL :"+ visitedURLs.get(i));
			if(url2.equals(unvisitedURLs.get(i))){

				return false;
			}
		}
		
		for (int i = 0; i < visitedURLs.size(); i++) {
			//System.out.println("URL Curent: " +url2);
			//System.out.println("URL :"+ visitedURLs.get(i));
			if(url2.equals(visitedURLs.get(i))){

				return false;
			}
		}
		
		
		return true;
		
	}
	
	private static boolean isInternal(){
		if(currentURL.contains(url)){
			return true;
		}
		return false;
	}

}
